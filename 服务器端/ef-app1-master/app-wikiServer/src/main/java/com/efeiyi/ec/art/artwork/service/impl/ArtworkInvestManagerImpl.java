package com.efeiyi.ec.art.artwork.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.artwork.service.ArtworkInvestManager;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.JPushConfig;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.jpush.EfeiyiPush;
import com.efeiyi.ec.art.model.Account;
import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.model.ArtworkInvest;
import com.efeiyi.ec.art.organization.model.User;
import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/4/14.
 *
 */
@Service
public class ArtworkInvestManagerImpl implements ArtworkInvestManager {
    private static Logger logger = Logger.getLogger(ArtworkInvestManagerImpl.class);
    @Autowired
    BaseManager baseManager;
    @Autowired
    ResultMapHandler resultMapHandler;
    private Lock lock = new ReentrantLock();
    @Override
    public Map artworkInvest(HttpServletRequest request,JSONObject jsonObj,LogBean logBean) {

        lock.lock();//加锁 可能会涉及修改项目状态
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            User user = (User) baseManager.getObject(User.class.getName(),jsonObj.getString("userId"));
            if (user == null && user.getId()!= null) {//用户存在
                //验证项目
                Artwork artwork = (Artwork)baseManager.getObject(Artwork.class.getName(),jsonObj.getString("artworkId"));
                if(artwork!= null && artwork.getId() != null){
                    BigDecimal investGoalMoney = artwork.getInvestGoalMoney();//融资目标额度
                    //判断项目是否是融资阶段  是否是融资中 状态是否是可用

                    if ("1".equals(artwork.getType()) && "14".equals(artwork.getStep()) && "0".equals(artwork.getStatus())){
                        BigDecimal price = new BigDecimal(jsonObj.getString("price"));

                        //获取用户关联账户
                        XQuery xQuery = new XQuery("listAccount_default",request);
                        xQuery.put("user_id",jsonObj.getString("userId"));
                        Account account = (Account)baseManager.listObject(xQuery).get(0);
                        //判断用户账户当前可用余额
                        if(price.doubleValue() > account.getCurrentUsableBalance().doubleValue()){
                            return resultMapHandler.handlerResult("100015","账户余额不足，请充值",logBean);
                        }


                        if (price.doubleValue()<2.0){
                            return resultMapHandler.handlerResult("100018","投资金额不能小于2元",logBean);
                        }
                        if (price.doubleValue()>investGoalMoney.doubleValue()){
                            return resultMapHandler.handlerResult("100014","投资金额最多"+investGoalMoney.doubleValue()+"元",logBean);
                        }
                        // 查询当前融资总额
                           /*LinkedHashMap<String, Object> paramMap = new LinkedHashMap<String, Object>();
                           paramMap.put("artworkId", jsonObj.getString("artworkId"));
                           BigDecimal investMoney = (BigDecimal)baseManager.getUniqueObjectByConditions(AppConfig.SQL_INVEST_MONEY_ARTWORK, paramMap);*/
                        BigDecimal investMoney = artwork.getInvestsMoney();//当前融资总额
                        BigDecimal  currentInvestMoney = investMoney.add(price);//加上本次投资额度
                        ArtworkInvest artworkInvest = new ArtworkInvest();
                        if ((currentInvestMoney.doubleValue() -investGoalMoney.doubleValue())>=0.0){
                            return resultMapHandler.handlerResult("100013","您好，你最多投资"+(investGoalMoney.doubleValue()-investMoney.doubleValue())+"元",logBean);
                        }else if (investGoalMoney.doubleValue()-currentInvestMoney.doubleValue()<=1.0 ) {//小于等于1 视为融资成功

                            artworkInvest.setStatus("1");
                            artworkInvest.setCreateDatetime(new Date());
                            artworkInvest.setCreator(user);
                            artworkInvest.setArtwork(artwork);
                            artworkInvest.setPrice(price);
                            baseManager.saveOrUpdate(ArtworkInvest.class.getName(),artworkInvest);//保存投资记录
                            //修改账户
                            account.setCurrentBalance(account.getCurrentBalance().multiply(price));
                            account.setCurrentUsableBalance(account.getCurrentUsableBalance().multiply(price));
                            baseManager.saveOrUpdate(Account.class.getName(),account);//更新用户账户

                            //修改项目
                            artwork.setType("1");
                            artwork.setStep("15");//修改项目状态
                            baseManager.saveOrUpdate(Artwork.class.getName(),artwork);//更新项目

                        }else{
                            artworkInvest.setStatus("1");
                            artworkInvest.setCreateDatetime(new Date());
                            artworkInvest.setCreator(user);
                            artworkInvest.setArtwork(artwork);
                            artworkInvest.setPrice(price);
                            baseManager.saveOrUpdate(ArtworkInvest.class.getName(),artworkInvest);//保存投资记录
                            //修改账户
                            account.setCurrentBalance(account.getCurrentBalance().multiply(price));
                            account.setCurrentUsableBalance(account.getCurrentUsableBalance().multiply(price));
                            baseManager.saveOrUpdate(Account.class.getName(),account);//更新用户账户
                        }
                    //发送透传消息，通知客户端更新融资金额  透传消息不需要保存
                        Map<String,Object> map = new HashMap<String,Object>();
                        map.put("msg_content",currentInvestMoney.toString());
                        map.put("content_type","text");
                        map.put("title","msg");
                        map.put("json","");
                        EfeiyiPush.SendPushMessage(JPushConfig.appKey, JPushConfig.masterSecret, map);
                        return resultMapHandler.handlerResult("0","成功",logBean);
                    }else{

                        resultMap = resultMapHandler.handlerResult("100017","非常抱歉，该项目目前不能投资",logBean);
                    }
                }else{

                    resultMap = resultMapHandler.handlerResult("100016","该项目已经下架或已被冻结",logBean);
                }



            }else{

                resultMap = resultMapHandler.handlerResult("10007","用户名不存在",logBean);
            }
        } catch (Exception e) {

            resultMap = resultMapHandler.handlerResult("10005","查询数据出现异常",logBean);
        }finally {
            lock.unlock();
        }
        return resultMap;
    }
}
