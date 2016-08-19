package com.efeiyi.ec.art.artwork.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.artwork.service.ArtworkInvestManager;
import com.efeiyi.ec.art.artwork.service.ArtworkPraiseManager;
import com.efeiyi.ec.art.base.model.LogBean;
import com.efeiyi.ec.art.base.util.AppConfig;
import com.efeiyi.ec.art.base.util.JPushConfig;
import com.efeiyi.ec.art.base.util.ResultMapHandler;
import com.efeiyi.ec.art.jpush.EfeiyiPush;
import com.efeiyi.ec.art.model.*;
import com.efeiyi.ec.art.organization.model.User;
import com.efeiyi.ec.art.organization.util.AuthorizationUtil;
import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/4/14.
 *
 */
@Service
public class ArtworkPraiseManagerImpl implements ArtworkPraiseManager {
    private static Logger logger = Logger.getLogger(ArtworkPraiseManagerImpl.class);
    @Autowired
    BaseManager baseManager;


    @Override
    public  boolean  saveArtWorkPraise(String id,String currentUserId){

        Artwork artwork = (Artwork) baseManager.getObject(Artwork.class.getName(),id);

        User currentUser = (User) baseManager.getObject(User.class.getName(),currentUserId);
        try {

            ArtWorkPraise artWorkPraise = new ArtWorkPraise();

            artWorkPraise.setCreateDateTime(new Date());

            artWorkPraise.setStatus("1");

            artWorkPraise.setUser(currentUser);

            artWorkPraise.setWatch("0");

            artWorkPraise.setArtwork(artwork);

            Notification notification = new Notification();

            notification.setArtwork(artwork);

            notification.setStatus("1");

            notification.setContent("有人点赞了!");

            notification.setCreateDatetime(new Date());

            notification.setFromUser(currentUser);

            notification.setIsWatch("0");

            notification.setTargetUser(artwork.getAuthor());

            baseManager.saveOrUpdate(Notification.class.getName(),notification);

            LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();

            param.put("userId", artwork.getAuthor().getId());

            PushUserBinding pushUserBinding = (PushUserBinding)baseManager.getUniqueObjectByConditions(AppConfig.SQL_USER_BINDING_GET,param);

            EfeiyiPush.SendPushNotification(JPushConfig.appKey,JPushConfig.masterSecret,notification,pushUserBinding.getCid());

        }catch (Exception e){

            e.printStackTrace();

            return false;
        }
        return  true;

    }
}
