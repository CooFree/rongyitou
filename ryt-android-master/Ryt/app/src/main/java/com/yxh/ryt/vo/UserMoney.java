package com.yxh.ryt.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class UserMoney implements Serializable {
    private BigDecimal investMoney;
    private BigDecimal restMoney;
    private BigDecimal rewardMoney;
    private List<Bill> billList;

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public BigDecimal getRestMoney() {
        return restMoney;
    }

    public void setRestMoney(BigDecimal restMoney) {
        this.restMoney = restMoney;
    }

    public BigDecimal getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(BigDecimal rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    public UserMoney(BigDecimal investMoney, List<Bill> billList, BigDecimal rewardMoney, BigDecimal restMoney) {
        this.investMoney = investMoney;
        this.billList = billList;
        this.rewardMoney = rewardMoney;
        this.restMoney = restMoney;
    }

    public UserMoney() {
    }
}
