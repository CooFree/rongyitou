package com.yxh.ryt.vo;

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
    private BigDecimal goalMoney;//目标融资金额
    private Long praise;
    private CWUser user;
    private String flag;
    private String level;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public CWUser getUser() {
        return user;
    }

    public void setUser(CWUser user) {
        this.user = user;
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

    public BigDecimal getGoalMoney() {
        return goalMoney;
    }

    public void setGoalMoney(BigDecimal goalMoney) {
        this.goalMoney = goalMoney;
    }

    public Long getPraise() {
        return praise;
    }

    public void setPraise(Long praise) {
        this.praise = praise;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
