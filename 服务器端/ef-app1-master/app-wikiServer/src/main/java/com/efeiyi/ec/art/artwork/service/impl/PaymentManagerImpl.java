package com.efeiyi.ec.art.artwork.service.impl;


import cn.beecloud.BCCache;
import cn.beecloud.BeeCloud;
import com.efeiyi.ec.art.artwork.service.PaymentManager;
import com.ming800.core.base.service.BaseManager;
import com.ming800.core.p.service.AutoSerialManager;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.digest.DigestUtils;
import cn.beecloud.*;
import org.apache.log4j.*;

/**
 * Created by Administrator on 2015/8/3.
 */
@Service
public class PaymentManagerImpl implements PaymentManager {

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private AutoSerialManager autoSerialManager;

    static {
        BeeCloud.registerApp("bad9ddf8-b5d8-475d-ae5b-1a244b9b9993","dfbedf37-97d9-4d35-aa32-cd8b3d6fed93","4d164cf7-211f-452f-8d85-417556656577","b5b9b602-6e9c-4e5a-8c37-7071974c3720"); //正式环境
    }


    @Override
    public void payCallback(String purchaseOrderPaymentId, String transactionNumber) {

    }


    /* 功能：BeeCloud服务器异步通知页面
      ***********页面功能说明***********
     创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
     该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
     如果没有收到该页面返回的 success 信息，BeeCloud会在36小时内按一定的时间策略重发通知
     */


    Logger log = Logger.getLogger(this.getClass());

    boolean verify(String sign, String text, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        log.info("mysign:" + mysign);

        long timeDifference = System.currentTimeMillis() - Long.valueOf(key);
        log.info("timeDifference:" + timeDifference);
        if (mysign.equals(sign) && timeDifference <= 300000) {
            return true;
        } else {
            return false;
        }
    }

   public boolean verifySign(String sign, String timestamp) {
        log.info("sign:" + sign);
        log.info("timestamp:" + timestamp);

        return verify(sign, BCCache.getAppID() + BCCache.getAppSecret(),
                timestamp, "UTF-8");

    }

    byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }







}
