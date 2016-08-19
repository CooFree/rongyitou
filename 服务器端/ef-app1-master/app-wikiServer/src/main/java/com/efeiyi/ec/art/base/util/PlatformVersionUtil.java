package com.efeiyi.ec.art.base.util;



import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/3/14.
 *
 */
public class PlatformVersionUtil {
    // 根据 Agent 判断是否是智能手机

    public static String CheckAgent(HttpServletRequest request)
    {
       String flag ="";
        String agent = request.getHeader("User-Agent");
        String[] keywords = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser","CFNetwork" };

        //排除 Windows 桌面系统
        if (!agent.contains("Windows NT") || (agent.contains("Windows NT") && agent.contains("compatible; MSIE 9.0;")))
        {
            //排除 苹果桌面系统
            if (!agent.contains("Windows NT") && !agent.contains("Macintosh"))
            {
                for (String item : keywords)
                {
                    if (agent.contains(item))
                    {

                        flag = item;
                        if (item.equals("CFNetwork")){
                            flag ="ios";
                        }
                        if (item.equals("okhttp")){
                            flag ="Android";
                        }
                        break;
                    }
                }
            }
        }

        return flag;
    }
}
