package com.efeiyi.ec.art.modelConvert;

import com.efeiyi.ec.art.organization.model.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public class ConvertArtWork implements Serializable {
    private List<ConvertWork> artworks;
    private BigDecimal sumInvestment; //全部投资金额
    private BigDecimal yield; //投资收益
    private User user;
    private Integer followNum; // 关注列表
    private Integer num; //被关注列表

    public List<ConvertWork> getArtworks() {
        return artworks;
    }

    public void setArtworks(List<ConvertWork> artworks) {
        this.artworks = artworks;
    }

    public BigDecimal getSumInvestment() {
        return sumInvestment;
    }

    public void setSumInvestment(BigDecimal sumInvestment) {
        this.sumInvestment = sumInvestment;
    }

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
