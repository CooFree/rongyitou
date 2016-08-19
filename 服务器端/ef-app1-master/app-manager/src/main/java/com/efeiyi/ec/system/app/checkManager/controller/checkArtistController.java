package com.efeiyi.ec.system.app.checkManager.controller;

import com.efeiyi.ec.art.model.Master;
import com.efeiyi.ec.system.app.checkManager.CheckConstant;
import com.ming800.core.base.service.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/4/8.
 * 艺术家审核 Controller
 */
@Controller
@RequestMapping("/checkArtist")
public class checkArtistController {

    @Autowired
    private BaseManager baseManager;

    @RequestMapping("/remove.do")
    public ModelAndView removeCheckProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (null == id || "".equals(id.trim())){
            throw new Exception("删除审核项目失败：审核项目Id为空!");
        }

        Master master = (Master) baseManager.getObject(Master.class.getName(), id);
        master.setTheStatus(CheckConstant.ARTIST_STATUS_REMOVE);
        baseManager.saveOrUpdate(Master.class.getName(), master);

        return new ModelAndView("redirect:/basic/xm.do?qm=plistCheckMaster_checkDefault&checkMaster=checkMaster");
    }

    @RequestMapping("/checkPass.do")
    public ModelAndView checkPassProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String resultPage = request.getParameter("resultPage");
        if (null == id || "".equals(id.trim())){
            if (CheckConstant.ARTIST_STATUS_WAIT.equals(type)){
                throw new Exception("待审核项目通过失败：待审核项目Id为空!");
            }
            throw new Exception("审核项目通过失败：审核项目Id为空!");
        }

        Master master = (Master) baseManager.getObject(Master.class.getName(), id);
        if (CheckConstant.ARTIST_STATUS_WAIT.equals(type)){
            master.setTheStatus(CheckConstant.ARTIST_STATUS_CHECKING);
        }
        if (CheckConstant.ARTIST_STATUS_CHECKING.equals(type)){
            master.setTheStatus(CheckConstant.ARTIST_STATUS_PASS);
        }
        baseManager.saveOrUpdate(Master.class.getName(), master);
        if (null != resultPage && "V".equals(resultPage.trim())){
            return new ModelAndView("redirect:/basic/xm.do?qm=viewCheckMaster&checkMaster=checkMaster&id=" + id);
        }
        return new ModelAndView("redirect:/basic/xm.do?qm=plistCheckMaster_checkDefault&checkMaster=checkMaster");
    }

    @RequestMapping("/checkReject.do")
    public ModelAndView checkRejectProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String resultPage = request.getParameter("resultPage");
        String message = request.getParameter("message");
        if (null == id || "".equals(id.trim())){
            throw new Exception("驳回审核项目失败：审核项目Id为空!");
        }

        Master master = (Master) baseManager.getObject(Master.class.getName(), id);
        master.setTheStatus(CheckConstant.ARTIST_STATUS_REJECT);
        master.setFeedback(message);
        baseManager.saveOrUpdate(Master.class.getName(), master);
        if (null != resultPage && "V".equals(resultPage.trim())){
            return new ModelAndView("redirect:/basic/xm.do?qm=viewCheckMaster&checkMaster=checkMaster&id=" + id);
        }
        return new ModelAndView("redirect:/basic/xm.do?qm=plistCheckMaster_checkDefault&checkMaster=checkMaster");
    }
}
