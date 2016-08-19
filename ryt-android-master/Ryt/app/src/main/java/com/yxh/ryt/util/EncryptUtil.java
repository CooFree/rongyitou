package com.yxh.ryt.util;

import android.util.Log;
import com.yxh.ryt.Constants;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by dev on 2015/12/28.
 */
public class EncryptUtil {
    public static String encrypt(Map<String,String> map)throws  Exception{
        TreeMap<String,String> map2 = new TreeMap();
        for(Iterator<String> it = map.keySet().iterator();it.hasNext();){
            String key = it.next();
            String Value = map.get(key);
            map2.put(key,Value);
        }
        StringBuffer str = new StringBuffer();
        for(Iterator<String> it = map2.keySet().iterator();it.hasNext();){
            String key = it.next();
            String Value = map2.get(key);
            str.append(key).append("=").append(Value).append("&");
        }
        str.append("key=" + Constants.APP_KEY);
        Log.d("before",str.toString());
        String md5Value = MD5(str.toString());
        Log.d("md5Value",md5Value);
        return md5Value;
    }

    public static String MD5(String md5) {
        try {
            MessageDigest md = MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (Exception e) {
        }
        return null;
    }

}
