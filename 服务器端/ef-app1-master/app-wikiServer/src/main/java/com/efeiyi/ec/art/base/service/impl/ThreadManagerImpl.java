package com.efeiyi.ec.art.base.service.impl;

import com.efeiyi.ec.art.base.service.ThreadManager;
import com.efeiyi.ec.art.base.thread.ThreadLaunch;
import com.efeiyi.ec.art.base.thread.UpdateArtWorkStatusThread;
import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.organization.OrganizationConst;
import com.ming800.core.base.dao.hibernate.XdoDaoSupport;
import com.ming800.core.p.model.SolrReactor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/12/10.
 *
 */
@Service
public class ThreadManagerImpl implements ThreadManager {
    private static Logger log = Logger.getLogger(ThreadManagerImpl.class);

  /*  public void startWork(List<Artwork> artworks) throws Exception{
        ExecutorService pool = Executors.newFixedThreadPool(OrganizationConst.THREAD_POOL_CORE_COUNT);
        log.info("开始创建线程池");
        System.out.println("开始创建线程池...");
        int count = artworks.size(),i = 0;
        for(; i <= count-1; i++) {
            System.out.println("启动第一个任务...");
                pool.execute(new Thread(new UpdateArtWorkStatusThread(artworks.get(0))));
        }


        log.info("创建线程池完成");
        System.out.println("创建线程池结束...");
        pool.shutdown();
        log.info("关闭线程池");
        System.out.println("关闭线程池...");
    }*/

    public void startWork(List<Artwork> artworks) throws Exception{

        //ThreadLaunch.getInstance().artworkQueue = putQueue(artworks);
        putQueue(artworks);
        try{
            synchronized(ThreadLaunch.getInstance().artworkQueue){

               if(!ThreadLaunch.getInstance().artworkQueue.isEmpty()) {
                   ThreadLaunch.getInstance().artworkQueue.notifyAll();
               }
                ThreadLaunch.getInstance().artworkQueue.wait();
            }
        }catch (Exception e){
            e.printStackTrace();

        }


    }

   //将所有待处理的artwort 放入任务队列中
    private  void putQueue(List<Artwork> artworks){
        if (artworks!=null && !artworks.isEmpty()){
            synchronized(ThreadLaunch.getInstance().artworkQueue){
                for (Artwork artwork:artworks){
                    ThreadLaunch.getInstance().artworkQueue.offer(artwork);
                }
            }

        }

    }

}
