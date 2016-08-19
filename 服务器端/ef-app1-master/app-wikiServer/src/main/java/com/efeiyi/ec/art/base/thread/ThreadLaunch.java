package com.efeiyi.ec.art.base.thread;

import com.efeiyi.ec.art.base.util.Capture;
import com.efeiyi.ec.art.base.util.ContextUtils;
import com.efeiyi.ec.art.model.Artwork;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by Administrator on 2016/4/16.
 *
 */
public class ThreadLaunch {//implements InitializingBean {


    public static final Queue<Artwork> artworkQueue = new ConcurrentLinkedQueue<Artwork>();
    private static ThreadLaunch threadLaunch;

    private ThreadLaunch() {
        super();
        init();
    }

    public void init() {
        try {

           int count = ((Capture) ContextUtils.getBean("capture")).getnThreads();
            for (int x = 0; x < count; x++) {
                new Thread(new UpdateArtWorkStatusThread((x+1)+"")).start();
                System.out.println("start "+x+1+" thread ...");
            }
        }catch (Exception e){
            System.err.println("UpdateArtWorkStatusThread invoking error...");
            e.printStackTrace();
        }

    }

    public static ThreadLaunch getInstance() {
        if (threadLaunch == null) {
            synchronized (ThreadLaunch.class) {
                if (threadLaunch == null) {
                    threadLaunch = new ThreadLaunch();
                }
            }
        }

        return threadLaunch;
    }

/*    @Override
    public void afterPropertiesSet() throws Exception {//spring ioc全部初始化后执行
        init();
    }*/
}
