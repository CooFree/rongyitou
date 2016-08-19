package com.yxh.ryt.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/5/6.
 */
public class HomeYSJArtWork implements Serializable {

    /**
     * id : in5z7r5f2w2f73so
     * title : 逐鹿顺义铜雕111
     * status : 1
     * investGoalMoney : 20000
     * investStartDatetime : 1460982461000
     * investEndDatetime : null
     * auctionStartDatetime : null
     * auctionEndDatetime : null
     * author : {"id":"ieatht97wfw30hfd","username":"15110008479","name":"温群英","pictureUrl":"http://tenant.efeiyi.com/background/蔡水况.jpg","cityId":null,"status":"1","createDatetime":1441684108000,"type":"10000","master":{"id":"ich9th9y00008h8v","brief":"中国工艺美术大师、高级工艺美术师、福建省工艺美术大师、中国工艺美术学会会员、美国海外艺术家协会理事、福建省工艺美术学会常务理事、协会会员、首届厦门工艺美术学会常务付理事长。","title":"","favicon":"photo/20150729144701.jpg","birthday":"1939年","level":"1","content":"","presentAddress":"福建","backgroundUrl":"background/蔡水况.jpg","provinceName":"福建","theStatus":"1","logoUrl":"logo/蔡水况.jpg","masterSpeech":null,"artCategory":null,"titleCertificate":null,"feedback":"份","identityFront":null,"identityBack":null}}
     * createDatetime : 1460982425000
     * picture_url : http://rongyitou2.efeiyi.com/artwork/1460982425138.jpeg
     * step : 100
     * investsMoney : 0
     * creationEndDatetime : null
     * type : 2
     * newCreationDate : null
     * auctionNum : null
     * newBidingPrice : null
     * newBiddingDate : null
     * sorts : null
     * winner : null
     * feedback : null
     * duration : 30
     * startingPrice : null
     * commentNum : 9
     * praiseNUm : 0
     */

    private String id;
    private String title;
    private String status;
    private BigDecimal investGoalMoney;
    private long investStartDatetime;
    private long investEndDatetime;
    private long auctionStartDatetime;
    private long auctionEndDatetime;
    private long createDatetime;
    private String picture_url;
    private String step;
    private BigDecimal investsMoney;
    private long creationEndDatetime;
    private String type;
    private long newCreationDate;
    private Object auctionNum;
    private Object newBidingPrice;
    private Object newBiddingDate;
    private Object sorts;
    private Object winner;
    private Object feedback;
    private int duration;
    private Object startingPrice;
    private int commentNum;
    private int praiseNUm;
    private String brief;
    private String description;
    private Auther author;
    public String getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public BigDecimal getInvestGoalMoney() {
        return investGoalMoney;
    }

    public void setInvestGoalMoney(BigDecimal investGoalMoney) {
        this.investGoalMoney = investGoalMoney;
    }

    public BigDecimal getInvestsMoney() {
        return investsMoney;
    }

    public void setInvestsMoney(BigDecimal investsMoney) {
        this.investsMoney = investsMoney;
    }

    public Object getCreationEndDatetime() {
        return creationEndDatetime;
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

    public void setNewCreationDate(long newCreationDate) {
        this.newCreationDate = newCreationDate;
    }

    public Object getAuctionNum() {
        return auctionNum;
    }

    public void setAuctionNum(Object auctionNum) {
        this.auctionNum = auctionNum;
    }

    public Object getNewBidingPrice() {
        return newBidingPrice;
    }

    public void setNewBidingPrice(Object newBidingPrice) {
        this.newBidingPrice = newBidingPrice;
    }

    public Object getNewBiddingDate() {
        return newBiddingDate;
    }

    public void setNewBiddingDate(Object newBiddingDate) {
        this.newBiddingDate = newBiddingDate;
    }

    public Object getSorts() {
        return sorts;
    }

    public void setSorts(Object sorts) {
        this.sorts = sorts;
    }

    public Object getWinner() {
        return winner;
    }

    public void setWinner(Object winner) {
        this.winner = winner;
    }

    public Object getFeedback() {
        return feedback;
    }

    public void setFeedback(Object feedback) {
        this.feedback = feedback;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public void setCreationEndDatetime(long creationEndDatetime) {
        this.creationEndDatetime = creationEndDatetime;
    }
}
