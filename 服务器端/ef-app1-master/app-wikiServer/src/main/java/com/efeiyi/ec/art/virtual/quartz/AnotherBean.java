package com.efeiyi.ec.art.virtual.quartz;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/4/1.
 */
@Component("anotherBean")
public class AnotherBean {

    public void printAnotherMessage(){
        System.out.println("I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean");
    }

}
