package com.efeiyi.ec.art.jpush.controller;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.AppConfig;
import com.efeiyi.ec.art.base.util.DigitalSignatureUtil;
import com.efeiyi.ec.art.base.util.JPushConfig;
import com.efeiyi.ec.art.base.util.JsonAcceptUtil;
import com.efeiyi.ec.art.jpush.EfeiyiPush;
import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.model.ArtworkComment;
import com.efeiyi.ec.art.model.Message;
import com.efeiyi.ec.art.model.PushUserBinding;
import com.efeiyi.ec.art.organization.model.User;
import com.ming800.core.base.controller.BaseController;
import com.ming800.core.base.service.BaseManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2016/1/25.
 *
 */
@Controller
public class EPushController extends BaseController {
    private static Logger logger = Logger.getLogger(EPushController.class);




    @Autowired
    BaseManager baseManager;
    @RequestMapping(value = "/app/pushMessage.do", method = RequestMethod.POST)
    @ResponseBody
    public Map pushMesssage(HttpServletRequest request) {
        LogBean logBean = new LogBean();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("content"))
                    || "".equals(jsonObj.getString("targetUserId"))
                    || "".equals(jsonObj.getString("fromUserId")) || "".equals(jsonObj.getString("timestamp"))) {
                logBean.setResultCode("10001");
                logBean.setMsg("必选参数为空，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10001");
                resultMap.put("resultMsg", "必选参数为空，请仔细检查");
                return resultMap;
            }

            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("content", jsonObj.getString("content"));
            treeMap.put("targetUserId", jsonObj.getString("targetUserId"));
            treeMap.put("fromUserId", jsonObj.getString("fromUserId"));
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

            Message message = new Message();
            message.setContent(jsonObj.getString("content"));
            message.setCreateDatetime(new Date());
            User fromUser,targetUser;
            PushUserBinding pushUserBinding;
            try {
                 fromUser = (User)baseManager.getObject(User.class.getName(),jsonObj.getString("fromUserId"));
                 //targetUser = (User)baseManager.getObject(User.class.getName(),jsonObj.getString("targetUserId"));
                LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
                param.put("userId", jsonObj.getString("targetUserId"));
                 pushUserBinding = (PushUserBinding)baseManager.getUniqueObjectByConditions(AppConfig.SQL_USER_BINDING_GET,param);

            }catch (Exception e){
                logBean.setResultCode("10007");
                logBean.setMsg("用户不存在");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10007");
                resultMap.put("resultMsg", "用户不存在");
                return resultMap;
            }
            message.setFromUser(fromUser);
            if(pushUserBinding.getId()!= null){
                message.setTargetUser(pushUserBinding.getUser());
                message.setCid(pushUserBinding.getCid());
            }

            baseManager.saveOrUpdate(Message.class.getName(),message);
                    logBean.setResultCode("0");
                    logBean.setMsg("成功");
                    baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                    resultMap.put("resultCode", "0");
                    resultMap.put("resultMsg", "成功");

            EfeiyiPush.SendPush(JPushConfig.appKey, JPushConfig.masterSecret,message);

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


    @RequestMapping(value = "/app/saveComment.do", method = RequestMethod.POST)
    @ResponseBody
    public Map saveComment(HttpServletRequest request) {
        LogBean logBean = new LogBean();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("content"))
                    || jsonObj.getString("father_comment_id")== null || "".equals(jsonObj.getString("username"))
                    || "".equals(jsonObj.getString("artwork_id")) || "".equals(jsonObj.getString("timestamp"))) {
                logBean.setResultCode("10001");
                logBean.setMsg("必选参数为空，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10001");
                resultMap.put("resultMsg", "必选参数为空，请仔细检查");
                return resultMap;
            }
            String father_comment_id = jsonObj.getString("father_comment_id");

            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("content", jsonObj.getString("content"));
            treeMap.put("father_comment_id", father_comment_id);
            treeMap.put("username", jsonObj.getString("username"));
            treeMap.put("artwork_id", jsonObj.getString("artwork_id"));
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

            ArtworkComment artworkComment = new ArtworkComment();
            artworkComment.setCreateDatetime(new Date());
            Artwork artwork = (Artwork)baseManager.getObject(Artwork.class.getName(),jsonObj.getString("artwork_id"));
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("username", jsonObj.getString("username"));
            User user = (User) baseManager.getUniqueObjectByConditions(AppConfig.SQL_USER_GET_APP, map);
            ArtworkComment fatherArtworkComment =null;

            if (artwork==null || user==null){
                logBean.setResultCode("10002");
                logBean.setMsg("参数校验不合格，请仔细检查");
                baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
                resultMap.put("resultCode", "10002");
                resultMap.put("resultMsg", "参数校验不合格，请仔细检查");
                return resultMap;
            }
            artworkComment.setArtwork(artwork);
            artworkComment.setContent(jsonObj.getString("content"));
            artworkComment.setCreator(user);
            artworkComment.setIsWatch("0");
            artworkComment.setCreateDatetime(new Date());
            artworkComment.setStatus("0");
            if(!"".equals(father_comment_id)){
                fatherArtworkComment = (ArtworkComment)baseManager.getObject(ArtworkComment.class.getName(),father_comment_id);
            }
            artworkComment.setFatherComment(fatherArtworkComment);


            baseManager.saveOrUpdate(Message.class.getName(),artworkComment);
            logBean.setResultCode("0");
            logBean.setMsg("成功");
            baseManager.saveOrUpdate(LogBean.class.getName(),logBean);
            resultMap.put("resultCode", "0");
            resultMap.put("resultMsg", "成功");

            EfeiyiPush.SendPushComment(JPushConfig.appKey, JPushConfig.masterSecret, artworkComment);

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






    public  static void main(String[] args){
        Message message = new Message();
        message.setContent("现年快乐");
        message.setCreateDatetime(new Date());
        //message.setFromUser(new User());
        EfeiyiPush.SendPush(JPushConfig.appKey, JPushConfig.masterSecret,message);

    }
}
