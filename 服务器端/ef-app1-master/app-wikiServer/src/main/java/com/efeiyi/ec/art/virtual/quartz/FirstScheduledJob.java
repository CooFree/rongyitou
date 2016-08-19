package com.efeiyi.ec.art.virtual.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by Administrator on 2016/4/1.
 */
public class FirstScheduledJob extends QuartzJobBean {
    private AnotherBean anotherBean;

    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException {
        System.out.println("I am FirstScheduledJob");
        this.anotherBean.printAnotherMessage();

    }

    public void setAnotherBean(AnotherBean anotherBean) {
        this.anotherBean = anotherBean;
    }
}
