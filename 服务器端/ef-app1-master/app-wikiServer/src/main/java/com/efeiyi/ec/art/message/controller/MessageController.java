package com.efeiyi.ec.art.message.controller;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.AppConfig;
import com.efeiyi.ec.art.base.util.DigitalSignatureUtil;
import com.efeiyi.ec.art.base.util.JsonAcceptUtil;
import com.efeiyi.ec.art.message.dao.MessageDao;
import com.efeiyi.ec.art.model.ArtworkComment;
import com.efeiyi.ec.art.model.Message;
import com.efeiyi.ec.art.model.Notification;
import com.efeiyi.ec.art.modelConvert.ArtworkCommentBean;
import com.efeiyi.ec.art.modelConvert.FatherArtworkCommentBean;
import com.ming800.core.base.controller.BaseController;
import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import org.apache.commons.beanutils.BeanUtils;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2015/12/22.
 *
 */
@Controller
public class MessageController extends BaseController {
    private static Logger logger = Logger.getLogger(MessageController.class);
    @Autowired
    BaseManager baseManager;

    @Autowired
    private MessageDao messageDao;
    /**
     * 点击首页消息时 通知 评论 私信等的未读条数展示
     * @param request
     * @return
     */

    @RequestMapping(value = "/app/informationList.do", method = RequestMethod.POST)
    @ResponseBody
    public Map informationList(HttpServletRequest request) throws IOException {


        LogBean logBean = new LogBean();//日志记录
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);//入参
            logBean.setCreateDate(new Date());//操作时间
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("timestamp"))) {
                logBean.setResultCode("10001");
                logBean.setMsg("必选参数为空，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10001");
                resultMap.put("resultMsg", "必选参数为空，请仔细检查");
                return resultMap;
            }
            //校验数字签名
            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("userId",jsonObj.getString("userId"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                logBean.setResultCode("10002");
                logBean.setMsg("参数校验不合格，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10002");
                resultMap.put("resultMsg", "参数校验不合格，请仔细检查");
                return resultMap;
            }
            //查询数据参数
            String userId = jsonObj.getString("userId");

            try {
                if("".equals(userId)){
                    logBean.setResultCode("10001");
                    logBean.setMsg("必选参数为空，请仔细检查");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "10001");
                    resultMap.put("resultMsg", "必选参数为空，请仔细检查");
                    return resultMap;
                }
                //私信 未读数
                XQuery xQuery = new XQuery("listMessage_default",request);
                xQuery.put("targetUser_id",userId);
                resultMap.put("messageNum",baseManager.listObject(xQuery).size());

                //通知 未读数
                xQuery = new XQuery("lisNotification_default",request);
                xQuery.put("targetUser_id",userId);
                resultMap.put("noticeNum",baseManager.listObject(xQuery).size());


                //评论 未读数  当前登录人是否为项目发起者  默认为 普通用户
                xQuery = new XQuery("listArtworkComment_default",request);
                xQuery.put("fatherComment_creator_id",userId);
                resultMap.put("commentNum",baseManager.listObject(xQuery).size());

                logBean.setResultCode("0");
                logBean.setMsg("成功");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "0");
                resultMap.put("resultMsg", "成功");


            } catch (Exception e) {
                e.printStackTrace();
                logBean.setResultCode("10005");
                logBean.setMsg("查询数据出现异常");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10005");
                resultMap.put("resultMsg", "查询数据出现异常");
            }
        } catch (Exception e) {
            logBean.setResultCode("10004");
            logBean.setMsg("未知错误，请联系管理员");
            baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
            resultMap.put("resultCode", "10004");
            resultMap.put("resultMsg", "未知错误，请联系管理员");
            return resultMap;
        }
        return resultMap;
    }


    /**
     * 通知 私信 评论的详情页
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/information.do", method = RequestMethod.POST)
    @ResponseBody
    public Map information(HttpServletRequest request) {
        LogBean logBean = new LogBean();//日志记录
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);//入参
            logBean.setCreateDate(new Date());//操作时间
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("timestamp"))) {
                logBean.setResultCode("10001");
                logBean.setMsg("必选参数为空，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10001");
                resultMap.put("resultMsg", "必选参数为空，请仔细检查");
                return resultMap;
            }
         //校验数字签名
            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("userId",jsonObj.getString("userId"));
            treeMap.put("type",jsonObj.getString("type"));
            treeMap.put("pageSize",jsonObj.getString("pageSize"));
            treeMap.put("pageNum",jsonObj.getString("pageNum"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                logBean.setResultCode("10002");
                logBean.setMsg("参数校验不合格，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10002");
                resultMap.put("resultMsg", "参数校验不合格，请仔细检查");
                return resultMap;
            }
            //查询数据参数
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("userId", jsonObj.getString("userId"));
            //pageNum从1开始
//            map.put("pageNum", (jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize")));
//            map.put("pageSize", jsonObj.getInteger("pageSize"));

           //0 通知 1 评价 2 私信
            String type = jsonObj.getString("type");
            //查询结果
            List objectList = null;
            try {
                if("".equals(type)){
                    logBean.setResultCode("10001");
                    logBean.setMsg("必选参数为空，请仔细检查");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "10001");
                    resultMap.put("resultMsg", "必选参数为空，请仔细检查");
                    return resultMap;
                }else if("0".equals(type)){
                    String hql = "from Notification WHERE targetUser.id=" +"'"+jsonObj.getString("userId").toString()+"'" + " AND status<>'0'  order by isWatch asc,createDatetime desc";
                    objectList =  (List<Notification>)messageDao.getPageList(hql,(jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize")),jsonObj.getInteger("pageSize"));
//                    objectList =  (List<Notification>)baseManager.listObject(AppConfig.SQL_NOTICE_GET_APP, map);
                }else if("1".equals(type)){
                    objectList = new ArrayList();
                    String hql = "from ArtworkComment WHERE fatherComment.creator.id= "+"'"+jsonObj.getString("userId").toString()+"'" + " AND status<>'0'  order by isWatch asc, createDatetime desc";
                    List<ArtworkComment> objectTempList = (List<ArtworkComment>)messageDao.getPageList(hql,(jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize")),jsonObj.getInteger("pageSize"));
                    for (ArtworkComment artworkComment : objectTempList){
                        ArtworkCommentBean artworkCommentBean = new ArtworkCommentBean();
                        FatherArtworkCommentBean fatherArtworkCommentBean = new FatherArtworkCommentBean();
                        if(artworkComment.getFatherComment()!=null){
                            fatherArtworkCommentBean.setId(artworkComment.getFatherComment().getId());
                            fatherArtworkCommentBean.setContent(artworkComment.getFatherComment().getContent());
                            fatherArtworkCommentBean.setArtwork(artworkComment.getFatherComment().getArtwork());
                            fatherArtworkCommentBean.setCreateDatetime(artworkComment.getFatherComment().getCreateDatetime());
                            fatherArtworkCommentBean.setCreator(artworkComment.getFatherComment().getCreator());
                            fatherArtworkCommentBean.setIsWatch(artworkComment.getFatherComment().getIsWatch());
                            fatherArtworkCommentBean.setStatus(artworkComment.getFatherComment().getStatus());
                        }
                        BeanUtils.copyProperties(artworkCommentBean,artworkComment);
                        artworkCommentBean.setFatherArtworkCommentBean(fatherArtworkCommentBean);
                        objectList.add(artworkCommentBean);
                    }
//                    objectList.add("d");
//                    objectList.add("dd");
//                    objectList =  (List<ArtworkComment>)messageDao.getPageList(hql,(jsonObj.getInteger("pageNum")-1)*(jsonObj.getInteger("pageSize")),jsonObj.getInteger("pageSize"));
//                    objectList =  (List<ArtworkComment>)baseManager.listObject(AppConfig.SQL_REPLY_GET_APP, map);
                }else if("2".equals(type)){
                    objectList = new ArrayList();
                    List<Message> objectTempList =  (List<Message>)baseManager.listObject(AppConfig.SQL_MESSAGE_GET_APP, map);
                    List<Long> numberList = (List<Long>)baseManager.listObject(AppConfig.SQL_MESSAGE_GET_NUM_APP, map);
                    for(int i=0;i<objectTempList.size();i++){
                        Message message =  objectTempList.get(i);
                        message.setIsRead(numberList.get(i));
                        objectList.add(message);
                    }
                }else {
                    logBean.setResultCode("10002");
                    logBean.setMsg("参数校验不合格，请仔细检查");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "10002");
                    resultMap.put("resultMsg", "参数校验不合格，请仔细检查");
                    return resultMap;
                }


                if (objectList!=null) {
                    logBean.setResultCode("0");
                    logBean.setMsg("成功");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "0");
                    resultMap.put("resultMsg", "成功");
                    resultMap.put("objectList",objectList);
                }else{
                    logBean.setResultCode("10008");
                    logBean.setMsg("查无数据,稍后再试");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "10008");
                    resultMap.put("resultMsg", "查无数据,稍后再试");
                    resultMap.put("objectList",null);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                logBean.setResultCode("10005");
                logBean.setMsg("查询数据出现异常");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10005");
                resultMap.put("resultMsg", "查询数据出现异常");
            }
        } catch (Exception e) {
            logBean.setResultCode("10004");
            logBean.setMsg("未知错误，请联系管理员");
            baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
            resultMap.put("resultCode", "10004");
            resultMap.put("resultMsg", "未知错误，请联系管理员");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 点击评论的详情页
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/commentDetail.do", method = RequestMethod.POST)
    @ResponseBody
    public Map commentDetail(HttpServletRequest request) {
        LogBean logBean = new LogBean();//日志记录
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);//入参
            logBean.setCreateDate(new Date());//操作时间
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("timestamp"))) {
                logBean.setResultCode("10001");
                logBean.setMsg("必选参数为空，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10001");
                resultMap.put("resultMsg", "必选参数为空，请仔细检查");
                return resultMap;
            }
            //校验数字签名
            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("userId",jsonObj.getString("userId"));//当前用户
            treeMap.put("fromUserId",jsonObj.getString("fromUserId"));//私信用户
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {
                logBean.setResultCode("10002");
                logBean.setMsg("参数校验不合格，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10002");
                resultMap.put("resultMsg", "参数校验不合格，请仔细检查");
                return resultMap;
            }
            //查询数据参数
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("userId", jsonObj.getString("userId"));
            map.put("fromUserId", jsonObj.getString("fromUserId"));

            //查询结果
            List objectList = null;

            try {
                objectList =  (List<Message>)baseManager.listObject(AppConfig.SQL_MESSAGE_DETAIL_GET_APP, map);
                if (objectList!=null) {
                    logBean.setResultCode("0");
                    logBean.setMsg("成功");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "0");
                    resultMap.put("resultMsg", "成功");
                    resultMap.put("objectList",objectList);
                }else{
                    logBean.setResultCode("10008");
                    logBean.setMsg("查无数据,稍后再试");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "10008");
                    resultMap.put("resultMsg", "查无数据,稍后再试");
                    resultMap.put("objectList",null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logBean.setResultCode("10005");
                logBean.setMsg("查询数据出现异常");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10005");
                resultMap.put("resultMsg", "查询数据出现异常");
            }
        } catch (Exception e) {
            logBean.setResultCode("10004");
            logBean.setMsg("未知错误，请联系管理员");
            baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
            resultMap.put("resultCode", "10004");
            resultMap.put("resultMsg", "未知错误，请联系管理员");
            return resultMap;
        }
        return resultMap;
    }

    public  static  void  main(String [] arg) throws Exception {


        String appKey = "BL2QEuXUXNoGbNeHObD4EzlX+KuGc70U";
        long timestamp = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<String, Object>();
        /**informationList.do测试加密参数**/
//        map.put("userId","2");
//        map.put("timestamp",timestamp);
        /**information.do测试加密参数**/
        map.put("userId","iijq9f1r7apprtab");
        map.put("type","0");
        map.put("pageSize","5");
        map.put("pageNum","1");
        map.put("timestamp", timestamp);
       /**commentDetail.do测试加密参数**/
//        map.put("userId","2");
//        map.put("fromUserId","1");
//        map.put("timestamp", timestamp);
//        String signmsg = "userId=2&timestamp="+timestamp+"&appkey="+appKey ;
        String signmsg = DigitalSignatureUtil.encrypt(map);


        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.1.69:8001/app/information.do";
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/json;charset=utf-8");
        /**json参数  informationList.do测试 **/
//         String json = "{\"userId\":\"2\",\"signmsg\":\"" + signmsg+"\",\"timestamp\":\""+timestamp+"\"}";
        /**json参数  information.do测试 **/
        String json = "{\"userId\":\"iijq9f1r7apprtab\",\"type\":\"0\",\"pageSize\":\"5\",\"pageNum\":\"1\",\"signmsg\":\"" + signmsg+"\",\"timestamp\":\""+timestamp+"\"}";
        /**json参数  commentDetail.do测试 **/
//        String json = "{\"userId\":\"2\",\"fromUserId\":\"1\",\"signmsg\":\"" + signmsg+"\",\"timestamp\":\""+timestamp+"\"}";
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