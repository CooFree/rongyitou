package com.efeiyi.ec.art.organization.util;




import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author WuYingbo
 */
public class CommonUtil {


    //验证json是否为空
    public static  boolean jsonObject(JSONObject jsonObject) {

        for(Map.Entry me : jsonObject.entrySet()){
            if("".equals(me.getValue().toString()) || StringUtils.isEmpty(me.getValue().toString())){
                return false;
            }
        }

        return  true;

    }


    //验证签名




}
