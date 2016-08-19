package com.efeiyi.ec.system.controller;

import com.ming800.core.base.service.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/13.
 */
@Controller
public class RemoveUtilController {
    @Autowired
    private BaseManager baseManager;
    @RequestMapping("/remove.do")
    @ResponseBody
    public void removeObject(HttpServletRequest request)throws  Exception{
        String id = request.getParameter("id");
        String clazz = request.getParameter("clazz");
        baseManager.remove(clazz, id);
    }

    @RequestMapping("/updateObject.do")
    @ResponseBody
    public void updateObject(HttpServletRequest request)throws  Exception{
        String clazz = request.getParameter("clazz");
        LinkedHashMap queryMap = new LinkedHashMap();
        StringBuilder sql = new StringBuilder("update ").append(clazz).append(" set ");
        for(Map.Entry entry : request.getParameterMap().entrySet()){
            if(!"clazz".equals(entry.getKey()) && !"_".equals(entry.getKey())) {
                queryMap.put(entry.getKey(), entry.getValue());
                sql.append(entry.getKey()).append("=:").append(entry.getKey()).append(",");
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where id =:id");
        baseManager.executeHql("update",sql.toString(),queryMap );
    }

}
