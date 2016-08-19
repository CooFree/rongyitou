package com.efeiyi.ec.art.base.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/12/10.
 *
 */
@Component
public class Capture {
    @Value("#{configProperties['isCapture']}")
    private String isCapture;
    @Value("#{configProperties['nThreads']}")
    private int  nThreads;

    public String getIsCapture() {
        return isCapture;
    }

    public void setIsCapture(String isCapture) {
        this.isCapture = isCapture;
    }

    public int getnThreads() {
        return nThreads;
    }

    public void setnThreads(int nThreads) {
        this.nThreads = nThreads;
    }
}
