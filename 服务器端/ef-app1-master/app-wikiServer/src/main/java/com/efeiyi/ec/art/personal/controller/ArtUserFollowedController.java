package com.efeiyi.ec.art.personal.controller;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.DigitalSignatureUtil;
import com.efeiyi.ec.art.base.util.JsonAcceptUtil;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.model.ArtUserFollowed;
import com.efeiyi.ec.art.organization.model.MyUser;
import com.ming800.core.base.controller.BaseController;
import com.ming800.core.does.model.PageInfo;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.taglib.PageEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Administrator on 2016/2/18.
 */
@Controller
public class ArtUserFollowedController extends BaseController {

    private static Logger logger = Logger.getLogger(ArtUserFollowedController.class);

    @Autowired
    private ResultMapHandler resultMapHandler;

    @ResponseBody
    @RequestMapping(value = "/app/userFollowed.do", method = RequestMethod.POST)
    public Map getUserProfile(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        LogBean logBean = new LogBean();
        TreeMap treeMap = new TreeMap();
        JSONObject jsonObj;
        try {
            jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("userId")) ||
                    "".equals(jsonObj.getString("timestamp")) || "".equals(jsonObj.getString("type")) ||
                    "".equals(jsonObj.getString("pageIndex")) || "".equals(jsonObj.getString("pageSize"))) {
                return resultMapHandler.handlerResult("10001", "必选参数为空，请仔细检查", logBean);
            }
            String signmsg = jsonObj.getString("signmsg");
            String userId = jsonObj.getString("userId");
            String type = jsonObj.getString("type");
            String index = jsonObj.getString("pageIndex");
            String size = jsonObj.getString("pageSize");
            treeMap.put("userId", userId);
            treeMap.put("type", type);
            treeMap.put("pageIndex", index);
            treeMap.put("pageSize", size);
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (!verify) {
                return resultMapHandler.handlerResult("10002", "参数校验不合格，请仔细检查", logBean);
            }
            XQuery xQuery = new XQuery("plistArtUserFollowed_default", request);
            xQuery.put("follower_id", userId);
            xQuery.put("type", type);
            PageEntity entity = new PageEntity();
            entity.setSize(Integer.parseInt(size));
            entity.setIndex(Integer.parseInt(index));
            xQuery.setPageEntity(entity);
            PageInfo pageInfo = baseManager.listPageInfo(xQuery);
            List<ArtUserFollowed> followedList = pageInfo.getList();
            resultMapHandler.handlerResult("0", "请求成功", logBean);
            resultMap.put("pageInfoList", followedList);
        } catch (Exception e) {
            return resultMapHandler.handlerResult("10004", "未知错误，请联系管理员", logBean);
        }

        return resultMap;
    }

    public static void main(String[] args) throws Exception {
        long timestamp = System.currentTimeMillis();
        String signmsg;
//        String pageIndex = "1";
//        String pageSize = "3";
        TreeMap map = new TreeMap();
        map.put("userId", "igxhnwhnmhlwkvnw");
//        map.put("pageIndex", pageIndex);
//        map.put("pageSize", pageSize);
        map.put("timestamp", timestamp);
        signmsg = DigitalSignatureUtil.encrypt(map);
        System.out.println(signmsg);
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.1.68:8080/app/applyArtMaster.do";
        HttpPost httppost = new HttpPost(url);

//        String changeFollowStatus = "{\"userId\":\"1\",\"pageIndex\":\"1\",\"pageSize\":\"3\",\"signmsg\":\"" + signmsg + "\",\"timestamp\":\"" + timestamp + "\"}";
        String changeFollowStatus = "{\"userId\":\"igxhnwhnmhlwkvnw\",\"signmsg\":\"" + signmsg + "\",\"timestamp\":\"" + timestamp + "\"}";

        String json = changeFollowStatus;
        JSONObject jsonObj = (JSONObject) JSONObject.parse(json);
        String jsonString = jsonObj.toJSONString();

        StringEntity stringEntity = new StringEntity(jsonString, "utf-8");

        httppost.setEntity(stringEntity);
        System.out.println("url:  " + url);
        byte[] b = new byte[(int) stringEntity.getContentLength()];
        stringEntity.getContent().read(b);
        System.out.println("报文:" + new String(b));
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                entity.getContent(), "UTF-8"));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        System.out.println(stringBuilder.toString());

    }

    @ResponseBody
    @RequestMapping(value = "/app/changeFollowStatus.do", method = RequestMethod.POST)
    public Map changeFollowStatus(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        LogBean logBean = new LogBean();
        TreeMap treeMap = new TreeMap();
        JSONObject jsonObj;
        try {
            jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if (!"".equals(jsonObj.getString("identifier"))){
                if ("0".equals(jsonObj.getString("identifier"))){
                    if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("followId")) ||
                            "".equals(jsonObj.getString("userId")) || "".equals(jsonObj.getString("followType"))) {
                        return resultMapHandler.handlerResult("10001", "必选参数为空，请仔细检查", logBean);
                    }
                    String signmsg = jsonObj.getString("signmsg");
                    String userId = jsonObj.getString("userId");
                    String followId = jsonObj.getString("followId");
                    String identifier = jsonObj.getString("identifier");
                    String followType = jsonObj.getString("followType");
                    treeMap.put("userId", userId);
                    treeMap.put("followId", followId);
                    treeMap.put("identifier", identifier);
                    treeMap.put("followType",followType);
                    treeMap.put("timestamp", jsonObj.getString("timestamp"));
                    boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
                    if (!verify) {
                        return resultMapHandler.handlerResult("10002", "参数校验不合格，请仔细检查", logBean);
                    }
                    ArtUserFollowed userFollowed = new ArtUserFollowed();
                    MyUser myUser = (MyUser) baseManager.getObject(MyUser.class.getName(), userId);
                    MyUser user = (MyUser) baseManager.getObject(MyUser.class.getName(), followId);
                    userFollowed.setUser(myUser);
                    userFollowed.setFollower(user);
                    userFollowed.setCreateDatetime(new Date());
                    userFollowed.setStatus("1");
                    userFollowed.setType(followType);
                    baseManager.saveOrUpdate(ArtUserFollowed.class.getName(),userFollowed);
                    resultMapHandler.handlerResult("0", "请求成功", logBean);
                    resultMap.put("artUserFollowed", userFollowed);
                }else if("1".equals(jsonObj.getString("identifier"))){
                    if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("artUserFollowId"))){
                        return resultMapHandler.handlerResult("10001", "必选参数为空，请仔细检查", logBean);
                    }
                    String signmsg = jsonObj.getString("signmsg");
                    String artUserFollowId = jsonObj.getString("artUserFollowId");
                    String identifier = jsonObj.getString("identifier");
                    treeMap.put("artUserFollowId", artUserFollowId);
                    treeMap.put("identifier", identifier);
                    treeMap.put("timestamp", jsonObj.getString("timestamp"));
                    boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
                    if (!verify) {
                        return resultMapHandler.handlerResult("10002", "参数校验不合格，请仔细检查", logBean);
                    }
                    ArtUserFollowed userFollowed = (ArtUserFollowed) baseManager.getObject(ArtUserFollowed.class.getName(),artUserFollowId);
                    userFollowed.setStatus("0");
                    baseManager.saveOrUpdate(ArtUserFollowed.class.getName(),userFollowed);
                    resultMapHandler.handlerResult("0", "请求成功", logBean);
                    resultMap.put("artUserFollowed", userFollowed);
                }
            } else {
                return resultMapHandler.handlerResult("10001", "必选参数为空，请仔细检查", logBean);
            }
        } catch (Exception e) {
            return resultMapHandler.handlerResult("10004", "未知错误，请联系管理员", logBean);
        }

        return resultMap;
    }

}
