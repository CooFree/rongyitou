package com.yxh.ryt.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 */
public class Artist implements Serializable {
    private String author_id;
    private String truename;
    private String username;
    private BigDecimal invest_goal_money;
    private BigDecimal turnover;
    private float bidding_rate;

    public float getBidding_rate() {
        return bidding_rate;
    }

    public void setBidding_rate(float bidding_rate) {
        this.bidding_rate = bidding_rate;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String picture;

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getPicture() {

        return picture;
    }


    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getInvest_goal_money() {
        return invest_goal_money;
    }

    public void setInvest_goal_money(BigDecimal invest_goal_money) {
        this.invest_goal_money = invest_goal_money;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }
}
