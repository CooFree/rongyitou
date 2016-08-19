package com.efeiyi.ec.art.artwork.controller;


import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.AppConfig;
import com.efeiyi.ec.art.base.util.DigitalSignatureUtil;
import com.efeiyi.ec.art.base.util.JsonAcceptUtil;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.message.dao.MessageDao;
import com.efeiyi.ec.art.model.*;
import com.efeiyi.ec.art.modelConvert.ArtWorkBean;
import com.efeiyi.ec.art.modelConvert.ArtWorkInvestBean;
import com.efeiyi.ec.art.organization.model.User;
import com.efeiyi.ec.art.organization.util.TimeUtil;
import com.ming800.core.base.controller.BaseController;
import com.ming800.core.does.model.PageInfo;
import com.ming800.core.does.model.XQuery;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/1/29.
 *
 */
@Controller
public class ArtWorkCreationController extends BaseController {
    private static Logger logger = Logger.getLogger(ArtWorkCreationController.class);

    @Autowired
    private MessageDao messageDao;

    @Autowired
    ResultMapHandler resultMapHandler;


    /**
     * 创作首页
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/artWorkCreationList.do", method = RequestMethod.POST)
    @ResponseBody
    public Map ArtWorkCreation(HttpServletRequest request) {
        LogBean logBean = new LogBean();//日志记录
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        List<Artwork> artworkList = null;
        try{
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);//入参
            logBean.setCreateDate(new Date());//操作时间
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            logBean.setApiName("artWorkCreationList");
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("timestamp"))) {
                return resultMapHandler.handlerResult("10001","必选参数为空，请仔细检查",logBean);
            }
            //校验数字签名
            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("pageNum",jsonObj.getString("pageNum"));
            treeMap.put("pageSize",jsonObj.getString("pageSize"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                return resultMapHandler.handlerResult("10002","参数校验不合格，请仔细检查",logBean);
            }

            String hql = "from Artwork WHERE 1=1 and status = '1' and type = '2' order by investStartDatetime asc";
            artworkList =  (List<Artwork>)messageDao.getPageList(hql,(jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize")),jsonObj.getInteger("pageSize"));
//            List<ArtWorkBean> objectList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String str1 = sdf.format(new Date());
            for (Artwork artwork : artworkList){
                //项目动态
                XQuery xQuery = new XQuery("listArtworkMessage_default",request);
                xQuery.put("artwork_id",jsonObj.getString("artWorkId"));
                List<ArtworkMessage> artworkMessageList = (List<ArtworkMessage>)baseManager.listObject(xQuery);
                if(artworkMessageList!=null&&artworkMessageList.size()!=0){
                    artwork.setNewCreationDate(TimeUtil.getDistanceTimes(str1,sdf.format(artworkMessageList.get(0).getCreateDatetime())));
                }else {
                    artwork.setNewCreationDate("暂无更新状态");
                }
//                ArtWorkBean artWorkBean = new ArtWorkBean();
//             //   artWorkBean.setMaster((Master)baseManager.getObject(Master.class.getName(),artwork.getAuthor().getId()));
//                artWorkBean.setArtwork(artwork);
//                objectList.add(artWorkBean);
            }
            resultMap = resultMapHandler.handlerResult("0","成功",logBean);
            resultMap.put("objectList",artworkList);
        } catch(Exception e){
            e.printStackTrace();
            return resultMapHandler.handlerResult("10004","未知错误，请联系管理员",logBean);
        }

        return resultMap;
    }


    /**
     * 创作详情页
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/artWorkCreationView.do", method = RequestMethod.POST)
    @ResponseBody
    public Map ArtWorkCreationView(HttpServletRequest request) {
        LogBean logBean = new LogBean();//日志记录
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        List<Artwork> artworkList = null;
        try{
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);//入参
            logBean.setCreateDate(new Date());//操作时间
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            logBean.setApiName("artWorkCreationView");
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("timestamp"))) {
                return resultMapHandler.handlerResult("10001","必选参数为空，请仔细检查",logBean);
            }
            //校验数字签名
            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("artWorkId",jsonObj.getString("artWorkId"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                return resultMapHandler.handlerResult("10002","参数校验不合格，请仔细检查",logBean);
            }

            Artwork artwork = (Artwork)baseManager.getObject(Artwork.class.getName(),jsonObj.getString("artWorkId"));
            //项目 艺术家信息
//            ArtWorkBean artWorkBean = new ArtWorkBean();
//            artWorkBean.setArtwork(artwork);
           // artWorkBean.setMaster((Master)baseManager.getObject(Master.class.getName(),artwork.getAuthor().getId()));
           //项目动态
            XQuery xQuery = new XQuery("listArtworkMessage_default",request);
            xQuery.put("artwork_id",jsonObj.getString("artWorkId"));
            List<ArtworkMessage> artworkMessageList = (List<ArtworkMessage>)baseManager.listObject(xQuery);
            //已创作时长
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String str1 = sdf.format(new Date());
            String str2 = sdf.format(artwork.getInvestEndDatetime());
            String str3 = sdf.format(artwork.getCreationEndDatetime());
            String createdTime = TimeUtil.getDistanceTimes(str1,str2);
            //剩余时长
            String restTime = TimeUtil.getDistanceTimes(str3,str1);
            resultMap = resultMapHandler.handlerResult("0","成功",logBean);
            resultMap.put("object",artwork);
            resultMap.put("artworkMessageList",artworkMessageList);
            resultMap.put("createdTime",createdTime);
            resultMap.put("restTime",restTime);
        } catch(Exception e){
            e.printStackTrace();
            return resultMapHandler.handlerResult("10004","未知错误，请联系管理员",logBean);
        }

        return resultMap;
    }


    public  static  void  main(String [] arg) throws Exception {


        String appKey = "BL2QEuXUXNoGbNeHObD4EzlX+KuGc70U";
        long timestamp = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();

        /**artWorkCreationList.do测试加密参数**/
//        map.put("pageNum","1");
//        map.put("pageSize","5");
        /**artWorkCreationView.do测试加密参数**/
        map.put("artWorkId","qydeyugqqiugd2");
        map.put("timestamp", timestamp);
        String signmsg = DigitalSignatureUtil.encrypt(map);
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.1.80:8001/app/artWorkCreationView.do";
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/json;charset=utf-8");

        /**json参数  artWorkCreationList.do测试 **/
//        String json = "{\"pageNum\":\"1\",\"pageSize\":\"5\",\"signmsg\":\"" + signmsg+"\",\"timestamp\":\""+timestamp+"\"}";
        /**json参数  artWorkCreationView.do测试 **/
        String json = "{\"artWorkId\":\"qydeyugqqiugd2\",\"signmsg\":\"" + signmsg+"\",\"timestamp\":\""+timestamp+"\"}";
        JSONObject jsonObj = (JSONObject)JSONObject.parse(json);
        String jsonString = jsonObj.toJSONString();

        StringEntity stringEntity = new StringEntity(jsonString,"utf-8");
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

        }catch (Exception e){

        }
    }



}
