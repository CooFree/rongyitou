package com.efeiyi.ec.art.artwork.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.artwork.service.ArtworkAuctionManager;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.DigitalSignatureUtil;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.model.Account;
import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.model.ArtworkBidding;
import com.efeiyi.ec.art.model.MarginAccount;
import com.ming800.core.base.service.BaseManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/4/15.
 */
@Service
public class ArtworkAuctionManagerImpl implements ArtworkAuctionManager {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private BaseManager baseManager;
    @Autowired
    ResultMapHandler resultMapHandler;
//    private Map<String, SoftReference<Lock>> auctionLockMap = new HashMap<>();
//security come first
    private Lock lock = new ReentrantLock();

    /**
     * 竞拍出价
     *
     * @param request
     * @param jsonObj
     * @param logBean
     * @return
     */
    @Override
    @Transactional
    public Map artworkBidOnAuction(HttpServletRequest request, JSONObject jsonObj, LogBean logBean) {
//        SoftReference<Lock> lock = auctionLockMap.get(jsonObj.get("artworkId"));//以防内存吃紧
//        if (lock == null) {
//            synchronized (this.getClass()) {
//                lock = auctionLockMap.get(jsonObj.get("artworkId"));
//                if (lock == null) {
//                    lock = new SoftReference(new ReentrantLock());
//                    auctionLockMap.put(jsonObj.getString("artworkId"), lock);
//                }
//            }
//        }
        Map resultMap = new HashMap();
        try {
//            lock.get().lock();
            lock.lock();
            //项目信息
            Artwork artwork = (Artwork) baseManager.getObject(Artwork.class.getName(), jsonObj.getString("artworkId"));
            if (!"31".equals(artwork.getStep())  //校验拍卖中
                    || !"0".equals(artwork.getStatus()) //校验拍卖未废弃
                    || new Date().compareTo(artwork.getAuctionEndDatetime()) > 0 //校验拍卖未结束
                    || jsonObj.getBigDecimal("price").compareTo(artwork.getNewBidingPrice()) < 0) {//校验出价大于当前最高价
                return resultMapHandler.handlerResult("10012", "不正确的拍卖状态", logBean);
            }
            LinkedHashMap queryMap = new LinkedHashMap();
            queryMap.put("userId", jsonObj.getString("userId"));
            queryMap.put("artworkId", jsonObj.getString("artworkId"));
            MarginAccount marginAccount = (MarginAccount) baseManager.getUniqueObjectByConditions("From MarginAccount a WHERE a.account.user.id = :userId AND a.artwork.id = :artworkId", queryMap);
            if (marginAccount == null || !"0".equals(marginAccount.getStatus())) {//未冻结拍卖保证金
                return resultMapHandler.handlerResult("10019", "未冻结拍卖保证金", logBean);
            }

            ArtworkBidding artworkBidding = new ArtworkBidding();
            artworkBidding.setArtwork(artwork);
            artworkBidding.setCreateDatetime(new Date());
            artworkBidding.setCreator(marginAccount.getAccount().getUser());
            artworkBidding.setStatus("1");
            artworkBidding.setPrice(jsonObj.getBigDecimal("price"));
            getCurrentSession().saveOrUpdate(artworkBidding);

            resultMap = resultMapHandler.handlerResult("0", "成功", logBean);
        } catch (Exception e) {
            e.getMessage();
            return resultMapHandler.handlerResult("10004", "未知错误，请联系管理员", logBean);
        } finally {
//            lock.get().unlock();
            lock.unlock();

        }
        return resultMap;
    }

    /**
     * 竞拍缴保证金
     *
     * @param request
     * @param jsonObj
     * @param logBean
     * @return
     */
    @Override
    @Transactional
    public Map artWorkAuctionPayDeposit(HttpServletRequest request, JSONObject jsonObj, LogBean logBean) {
        //项目信息
        Artwork artwork = (Artwork) baseManager.getObject(Artwork.class.getName(), jsonObj.getString("artworkId"));
        if (!"31".equals(artwork.getStep())  //校验拍卖中
                || !"0".equals(artwork.getStatus()) //校验拍卖未废弃
                || new Date().compareTo(artwork.getAuctionEndDatetime()) > 0) {//校验拍卖未结束
            return resultMapHandler.handlerResult("10012", "不正确的拍卖状态", logBean);
        }
        LinkedHashMap queryMap = new LinkedHashMap();
        queryMap.put("userId", jsonObj.getString("userId"));
        queryMap.put("artworkId", jsonObj.getString("artworkId"));
        MarginAccount marginAccount = (MarginAccount) baseManager.getUniqueObjectByConditions("From MarginAccount a WHERE a.account.user.id = :userId AND a.artwork.id = :artworkId", queryMap);
        if (marginAccount != null) {//已缴保证金
            return resultMapHandler.handlerResult("0", "成功", logBean);
        }
        queryMap.remove("artworkId");
        Account account = (Account) baseManager.getUniqueObjectByConditions("From Account a WHERE a.user.id = :userId", queryMap);
        BigDecimal deposit = artwork.getStartingPrice().movePointLeft(1);
        if (deposit.compareTo(account.getCurrentUsableBalance()) > 0 //保证金充足?
                || deposit.compareTo(account.getCurrentBalance()) > 0) {
            return resultMapHandler.handlerResult("100015", "账户余额不足，请充值", logBean);
        }
        account.setCurrentUsableBalance(account.getCurrentUsableBalance().subtract(deposit));
        getCurrentSession().saveOrUpdate(account);

        marginAccount = new MarginAccount();
        marginAccount.setStatus("0");
        marginAccount.setCreateDatetime(new Date());
//        marginAccount.setEndDatetime(artwork.getAuctionEndDatetime());
        marginAccount.setArtwork(artwork);
        marginAccount.setAccount(account);
        marginAccount.setCurrentBalance(account.getCurrentBalance());
        getCurrentSession().saveOrUpdate(marginAccount);

        Map resultMap = resultMapHandler.handlerResult("0", "成功", logBean);
        return resultMap;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public static void main(String[] a) throws Exception {

//        String appKey = "BL2QEuXUXNoGbNeHObD4EzlX+KuGc70U";
        long timestamp = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();

        /**artWorkAuctionList.do测试加密参数**/
//        map.put("pageNum", "1");
//        map.put("pageSize", "5");
        /**artWorkAuctionView.do测试加密参数**/
        //map.put("artWorkId","qydeyugqqiugd2");
        map.put("timestamp", timestamp);
        map.put("userId", "igxhnwhnmhlwkvnw");
        map.put("artworkId", "qydeyugqqiugd7");
//        map.put("price", "500");

        String signmsg = DigitalSignatureUtil.encrypt(map);
        map.put("signmsg", signmsg);
        HttpClient httpClient = new DefaultHttpClient();
//        String url = "http://192.168.1.41:8080/app/artworkBid.do";
        String url = "http://192.168.1.41:8080/app/artWorkAuctionPayDeposit.do";
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/json;charset=utf-8");

        /**json参数  artWorkAuctionView.do测试 **/
        //String json = "{\"artWorkId\":\"qydeyugqqiugd2\",\"signmsg\":\"" + signmsg+"\",\"timestamp\":\""+timestamp+"\"}";
        /**json参数  artWorkAuctionList.do测试 **/
        String json = JSONObject.toJSONString(map);
//        JSONObject jsonObj = (JSONObject) JSONObject.parse(json);
//        String jsonString = jsonObj.toJSONString();

        StringEntity stringEntity = new StringEntity(json, "utf-8");
        stringEntity.setContentType("text/json");
        stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        System.out.println("url:  " + url);
        try {
            byte[] b = new byte[(int) stringEntity.getContentLength()];
            System.out.println(stringEntity);
            stringEntity.getContent().read(b);
            System.out.println("报文:" + new String(b, "utf-8"));
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    entity.getContent(), "UTF-8"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            System.out.println(stringBuilder);
        } catch (Exception e) {

        }

    }
}
