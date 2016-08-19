package com.efeiyi.ec.art.virtual.quartz;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2016/4/1.
 */
public class AppMain {
    public static void main(String args[]){
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-quartz.xml");
    }
}
