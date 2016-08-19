package com.efeiyi.ec.art.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/4/7.
 */

public class ArtistTopListVO implements Serializable {
    private String author_id;
    private String truename;
    private BigDecimal invest_goal_money;
    private BigDecimal turnover;
    private String username;
    public ArtistTopListVO(){
        super();
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
