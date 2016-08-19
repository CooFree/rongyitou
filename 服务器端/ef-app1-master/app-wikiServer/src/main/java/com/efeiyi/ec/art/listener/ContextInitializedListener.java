package com.efeiyi.ec.art.listener;

import com.efeiyi.ec.art.base.util.ContextUtils;
import com.efeiyi.ec.art.base.util.Capture;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/12/10.
 *
 */
@Service
public class ContextInitializedListener implements ApplicationListener<ContextRefreshedEvent>,ApplicationContextAware {
    private static Logger log = Logger.getLogger(ContextInitializedListener.class);


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextUtils.setApplicationContext(applicationContext);
        log.debug("ApplicationContext registed");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){//root application context 没有parent
            String exe;
            try{
            exe = ((Capture)ContextUtils.getBean("capture")).getIsCapture();
            if (exe!=null && !"".equals(exe)){
                if ("on".equals(exe)){//去处理项目拍卖状态
                    log.info("数据开关已经打开");
                    System.out.println("数据开关:"+exe);
                    //((ThreadManagerImpl)ContextUtils.getBean("threadManagerImpl")).startWork();
                }else{
                    log.info("i don not  need do anything");
                }
            }
            }catch (Exception e){
                log.error(e);
            }
        }

    }
}
