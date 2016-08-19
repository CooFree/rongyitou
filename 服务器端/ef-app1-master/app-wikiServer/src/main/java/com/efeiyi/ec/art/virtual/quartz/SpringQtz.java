package com.efeiyi.ec.art.virtual.quartz;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/3/31.
 */
@Component("springQtz")
public class SpringQtz {
    private static int counter = 0;
    public void execute()  {
        counter++;
        System.out.println("第 " + counter +" 次，hello");

    }
}
