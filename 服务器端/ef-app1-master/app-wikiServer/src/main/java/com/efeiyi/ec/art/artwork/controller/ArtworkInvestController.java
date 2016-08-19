package com.efeiyi.ec.art.artwork.controller;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.artwork.service.ArtworkInvestManager;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.AppConfig;
import com.efeiyi.ec.art.base.util.DigitalSignatureUtil;
import com.efeiyi.ec.art.base.util.JsonAcceptUtil;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.model.Account;
import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.model.ArtworkInvest;
import com.efeiyi.ec.art.organization.model.MyUser;
import com.efeiyi.ec.art.organization.model.User;
import com.ming800.core.base.controller.BaseController;
import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/4/14.
 *
 */
public class ArtworkInvestController extends BaseController {
    private static Logger logger = Logger.getLogger(ArtworkInvestController.class);

    @Autowired
    BaseManager baseManager;
    @Autowired
    ResultMapHandler resultMapHandler;

    @Autowired
    ArtworkInvestManager artworkInvestManager;



    /**
     * 用户投资接口，此接口不涉及微信、支付宝
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/artworkInvest.do", method = RequestMethod.POST)
    @ResponseBody
    public Map artworkInvest(HttpServletRequest request) {
        LogBean logBean = new LogBean();
        logBean.setApiName("artworkInvest");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        TreeMap treeMap = new TreeMap();
        try {
            JSONObject jsonObj = JsonAcceptUtil.receiveJson(request);
            logBean.setCreateDate(new Date());
            logBean.setRequestMessage(jsonObj.toString());//************记录请求报文
            if ("".equals(jsonObj.getString("signmsg")) || "".equals(jsonObj.getString("userId"))
                    || "".equals(jsonObj.getString("artworkId"))  || "".equals(jsonObj.getString("price"))
                    || "".equals(jsonObj.getString("timestamp"))) {

                return resultMapHandler.handlerResult("10001","必选参数为空，请仔细检查",logBean);
            }

            String signmsg = jsonObj.getString("signmsg");
            treeMap.put("userId", jsonObj.getString("userId"));
            treeMap.put("artworkId", jsonObj.getString("artworkId"));
            treeMap.put("price", jsonObj.getString("price"));
            treeMap.put("timestamp", jsonObj.getString("timestamp"));
            boolean verify = DigitalSignatureUtil.verify(treeMap, signmsg);
            if (verify != true) {

                return resultMapHandler.handlerResult("10002","参数校验不合格，请仔细检查",logBean);
            }
            resultMap = artworkInvestManager.artworkInvest(request,jsonObj,logBean);

        } catch (Exception e) {

            return resultMapHandler.handlerResult("10004","未知错误，请联系管理员",logBean);
        }
        return resultMap;
    }
}
