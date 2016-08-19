package com.efeiyi.ec.art.modelConvert;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/2/29.
 */
public class ConvertWork implements Serializable{
    private String id;
    private String title;
    private String brief;
    private String picture_url;
    private String step; //1 : 审核阶段  2 融资阶段  3 制作阶段  4 拍卖阶段  5 抽奖阶段  9 技术
    private BigDecimal investsMoney;//用户投资金额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public BigDecimal getInvestsMoney() {
        return investsMoney;
    }

    public void setInvestsMoney(BigDecimal investsMoney) {
        this.investsMoney = investsMoney;
    }
}
