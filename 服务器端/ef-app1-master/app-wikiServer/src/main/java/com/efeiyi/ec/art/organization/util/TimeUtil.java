package com.efeiyi.ec.art.organization.util;


import com.efeiyi.ec.art.organization.model.MyUser;
import com.efeiyi.ec.art.organization.model.Role;
import com.efeiyi.ec.art.organization.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author WuYingbo
 */
public class TimeUtil {

    public static  String DAY = "DAY";
    public static  String HOUR = "HOUR";
    public static  String MIN = "MIN";
    public static  String SECOND = "SECOND";

    //时间比较
    public static  String getDistanceTimes(String str1, String str2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
            one = sdf.parse(str1);
            two = sdf.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;

            diff = time1 - time2;

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min};
        String time = day+"日"+hour+"时"+min+"分";
        return time;
    }



    //时间比较

    /**
     *
     * @param firstTime 第一个时间
     * @param endTime   第二个时间
     * @param format    时间格式 默认为时分秒
     * @param exactValue   精确值
     * @return
     */
    public static Map getDistanceTimes2(Date firstTime, Date endTime, String format, String exactValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Map map = new HashMap();
        map.put("sign","+");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long second = 0;
        String time = "";
        try {

            one = sdf.parse(sdf.format(firstTime));
            two = sdf.parse(sdf.format(endTime));
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;

            diff = time1 - time2;

            if(diff<0){
                diff = -diff;
                map.put("sign","-");
            }

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            second = (diff-day*24*60*60*1000-hour*60*60*1000-min*60*1000)/1000;
            if("HOUR".equals(exactValue)){

                time = day + ("".equals(format)?"日":format)+hour+("".equals(format)?"时":"");

            }else if("MIN".equals(exactValue)){

                time = day + ("".equals(format)?"日":format)+hour+("".equals(format)?"时":format)+min+("".equals(format)?"分":"");

            }else if ("SECOND".equals(exactValue)){

                time = day + ("".equals(format)?"日":format)+hour+("".equals(format)?"时":format)+min+("".equals(format)?"分":format)+second+("".equals(format)?"秒":"");

            }else if ("DAY".equals(exactValue)){
                time = day + ("".equals(format)?"日":"");
            }

            map.put("time",time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
