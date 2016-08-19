package com.efeiyi.ec.art.base.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/1/13.
 */
public class JsonAcceptUtil {

    public static JSONObject receiveJson(HttpServletRequest request) throws  Exception{
            request.setCharacterEncoding("utf-8");
            InputStream inputStream = request.getInputStream();
            byte[] bytes = new byte[request.getContentLength()];
            inputStream.read(bytes);
            String param = new String(bytes,"UTF-8");
            JSONObject jsonObj = (JSONObject) JSONObject.parse(param);
            return jsonObj;
    }
}
