package com.efeiyi.ec.art.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/4/7.
 */

public class ArtistBiddingTopListVO implements Serializable {
    private String author_id;
    private String truename;
    private BigDecimal bidding_rate;
    private String username;
    public ArtistBiddingTopListVO(){
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

    public BigDecimal getBidding_rate() {
        return bidding_rate;
    }

    public void setBidding_rate(BigDecimal bidding_rate) {
        this.bidding_rate = bidding_rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
