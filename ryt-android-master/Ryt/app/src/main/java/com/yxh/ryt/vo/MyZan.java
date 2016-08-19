package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MyZan implements Serializable {

    /**
     * id : qydeyugqqiugd2
     * title : 测试1
     * brief : 这是一个
     * description : 修改后
     * status : 0
     * investGoalMoney : 1000
     * investStartDatetime : 1455005261000
     * investEndDatetime : 1454314064000
     * auctionStartDatetime : 1454400455000
     * auctionEndDatetime : 1454400449000
     * author : {"id":"ieatht97wfw30hfd","username":"15110008479","name":"温群英","pictureUrl":"http://tenant.efeiyi.com/background/蔡水况.jpg","cityId":null,"status":"1","createDatetime":1441684108000,"type":"10000","master":{"id":"ich9th9y00008h8v","brief":"中国工艺美术大师、高级工艺美术师、福建省工艺美术大师、中国工艺美术学会会员、美国海外艺术家协会理事、福建省工艺美术学会常务理事、协会会员、首届厦门工艺美术学会常务付理事长。","title":"","favicon":"photo/20150729144701.jpg","birthday":"1939年","level":"1","content":"","presentAddress":"福建","backgroundUrl":"background/蔡水况.jpg","provinceName":"福建","theStatus":"1","logoUrl":"logo/蔡水况.jpg","masterSpeech":null,"artCategory":null,"titleCertificate":null,"feedback":"份","identityFront":null,"identityBack":null}}
     * createDatetime : 1454314046000
     * picture_url : http://tenant.efeiyi.com/background/蔡水况.jpg
     * step : 23
     * investsMoney : 275
     * creationEndDatetime : 1458285471000
     * type : 3
     * newCreationDate : null
     * auctionNum : null
     * newBidingPrice : 1500
     * newBiddingDate : null
     * sorts : 1
     * winner : null
     * feedback :
     * duration : null
     * startingPrice : null
     * commentNum : 2
     * praiseNUm : 1
     */

    private String id;
    private String title;
    private String brief;
    private String description;
    private String status;
    private int investGoalMoney;
    private long investStartDatetime;
    private long investEndDatetime;
    private long auctionStartDatetime;
    private long auctionEndDatetime;
    private long createDatetime;
    private String picture_url;
    private String step;
    private int investsMoney;
    private long creationEndDatetime;
    private String type;
    private Object newCreationDate;
    private Object auctionNum;
    private int newBidingPrice;
    private Object newBiddingDate;
    private String sorts;
    private Object winner;
    private String feedback;
    private Object duration;
    private Object startingPrice;
    private int commentNum;
    private int praiseNUm;
    private Auther author;
    public String getId() {
        return id;
    }

    public Auther getAuthor() {
        return author;
    }

    public void setAuthor(Auther author) {
        this.author = author;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getInvestGoalMoney() {
        return investGoalMoney;
    }

    public void setInvestGoalMoney(int investGoalMoney) {
        this.investGoalMoney = investGoalMoney;
    }

    public long getInvestStartDatetime() {
        return investStartDatetime;
    }

    public void setInvestStartDatetime(long investStartDatetime) {
        this.investStartDatetime = investStartDatetime;
    }

    public long getInvestEndDatetime() {
        return investEndDatetime;
    }

    public void setInvestEndDatetime(long investEndDatetime) {
        this.investEndDatetime = investEndDatetime;
    }

    public long getAuctionStartDatetime() {
        return auctionStartDatetime;
    }

    public void setAuctionStartDatetime(long auctionStartDatetime) {
        this.auctionStartDatetime = auctionStartDatetime;
    }

    public long getAuctionEndDatetime() {
        return auctionEndDatetime;
    }

    public void setAuctionEndDatetime(long auctionEndDatetime) {
        this.auctionEndDatetime = auctionEndDatetime;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
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

    public int getInvestsMoney() {
        return investsMoney;
    }

    public void setInvestsMoney(int investsMoney) {
        this.investsMoney = investsMoney;
    }

    public long getCreationEndDatetime() {
        return creationEndDatetime;
    }

    public void setCreationEndDatetime(long creationEndDatetime) {
        this.creationEndDatetime = creationEndDatetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getNewCreationDate() {
        return newCreationDate;
    }

    public void setNewCreationDate(Object newCreationDate) {
        this.newCreationDate = newCreationDate;
    }

    public Object getAuctionNum() {
        return auctionNum;
    }

    public void setAuctionNum(Object auctionNum) {
        this.auctionNum = auctionNum;
    }

    public int getNewBidingPrice() {
        return newBidingPrice;
    }

    public void setNewBidingPrice(int newBidingPrice) {
        this.newBidingPrice = newBidingPrice;
    }

    public Object getNewBiddingDate() {
        return newBiddingDate;
    }

    public void setNewBiddingDate(Object newBiddingDate) {
        this.newBiddingDate = newBiddingDate;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    public Object getWinner() {
        return winner;
    }

    public void setWinner(Object winner) {
        this.winner = winner;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public Object getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Object startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getPraiseNUm() {
        return praiseNUm;
    }

    public void setPraiseNUm(int praiseNUm) {
        this.praiseNUm = praiseNUm;
    }
}
