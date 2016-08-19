package com.efeiyi.ec.art.base.util;

import com.efeiyi.ec.art.base.model.LogBean;
import com.ming800.core.base.service.BaseManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/9.
 *
 */
@Service
public class ResultMapHandler {
    private static Logger logger = Logger.getLogger(ResultMapHandler.class);
    @Autowired
    BaseManager baseManager;

    public  Map handlerResult(String code,String msg,LogBean logBean) {
        HttpServletRequest request = getRequest();
        String flag = PlatformVersionUtil.CheckAgent(request);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("resultCode", code);
        resultMap.put("resultMsg", msg);
        logBean.setResultCode(code);
        logBean.setMsg(msg);
        logBean.setExtend1(flag);
        baseManager.saveOrUpdate(LogBean.class.getName(), logBean);
        return resultMap;
    }

    public HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    public HttpServletResponse getResponse(){

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }
}
