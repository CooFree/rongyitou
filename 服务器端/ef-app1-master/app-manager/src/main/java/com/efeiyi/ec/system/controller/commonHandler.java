package com.efeiyi.ec.system.controller;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.service.DoHandler;
import com.ming800.core.util.ApplicationContextUtil;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/7/22.
 */
public class commonHandler implements DoHandler {

    private BaseManager baseManager = (BaseManager) ApplicationContextUtil.getApplicationContext().getBean("baseManagerImpl");

    @Override
    public ModelMap handle(ModelMap modelMap, HttpServletRequest request) throws Exception {

        modelMap.put("groupName" , request.getParameter("groupName"));
        modelMap.put("view" , request.getParameter("view"));

        return modelMap;
    }
}
