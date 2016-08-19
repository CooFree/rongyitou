package com.efeiyi.ec.system.app.checkManager.controller;

import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.system.app.checkManager.CheckConstant;
import com.ming800.core.base.service.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/4/7.
 * 项目审核 Controller
 */
@Controller
@RequestMapping("/checkProject")
public class checkProjectController {

    @Autowired
    private BaseManager baseManager;

    @RequestMapping("/remove.do")
    public ModelAndView removeCheckProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (null == id || "".equals(id.trim())){
            throw new Exception("删除审核项目失败：审核项目Id为空!");
        }

        Artwork artwork = (Artwork) baseManager.getObject(Artwork.class.getName(), id);
        artwork.setStatus(CheckConstant.ARTWORK_STATUS_REMOVE);
        baseManager.saveOrUpdate(Artwork.class.getName(), artwork);
        return new ModelAndView("redirect:/basic/xm.do?qm=plistCheckArtwork_checkDefault&checkProject=checkProject");
    }

    @RequestMapping("/checkPass.do")
    public ModelAndView checkPassProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String resultPage = request.getParameter("resultPage");
        if (null == id || "".equals(id.trim())){
            if (CheckConstant.ARTWORK_STEP_WAIT.equals(type)){
                throw new Exception("待审核项目通过失败：待审核项目Id为空!");
            }
            throw new Exception("审核项目通过失败：审核项目Id为空!");
        }

        Artwork artwork = (Artwork) baseManager.getObject(Artwork.class.getName(), id);
        if (CheckConstant.ARTWORK_STEP_WAIT.equals(type)){
            artwork.setStep(CheckConstant.ARTWORK_STEP_CHECKING);
        }
        if (CheckConstant.ARTWORK_STEP_CHECKING.equals(type)){
            artwork.setStep(CheckConstant.ARTWORK_STEP_PASS);
        }
        baseManager.saveOrUpdate(Artwork.class.getName(), artwork);
        if (null != resultPage && "V".equals(resultPage.trim())){
            return new ModelAndView("redirect:/basic/xm.do?qm=viewCheckArtwork&checkProject=checkProject&id=" + id);
        }
        return new ModelAndView("redirect:/basic/xm.do?qm=plistCheckArtwork_checkDefault&checkProject=checkProject");
    }

    @RequestMapping("/checkReject.do")
    public ModelAndView checkRejectProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String resultPage = request.getParameter("resultPage");
        String message = request.getParameter("message");
        if (null == id || "".equals(id.trim())){
            throw new Exception("驳回审核项目失败：审核项目Id为空!");
        }

        Artwork artwork = (Artwork) baseManager.getObject(Artwork.class.getName(), id);
        artwork.setStep(CheckConstant.ARTWORK_STEP_REJECT);
        artwork.setFeedback(message);
        baseManager.saveOrUpdate(Artwork.class.getName(), artwork);
        if (null != resultPage && "V".equals(resultPage.trim())){
            return new ModelAndView("redirect:/basic/xm.do?qm=viewCheckArtwork&checkProject=checkProject&id=" + id);
        }
        return new ModelAndView("redirect:/basic/xm.do?qm=plistCheckArtwork_checkDefault&checkProject=checkProject");
    }
}
