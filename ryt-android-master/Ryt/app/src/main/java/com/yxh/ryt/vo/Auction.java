package com.yxh.ryt.vo;

import java.io.Serializable;

public class Auction implements Serializable {
    private String isSubmitDepositPrice;

    public void setIsSubmitDepositPrice(String isSubmitDepositPrice) {
        this.isSubmitDepositPrice = isSubmitDepositPrice;
    }

    public void setArtWorkBidding(String artWorkBidding) {
        this.artWorkBidding = artWorkBidding;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    private String artWorkBidding;

    private Artwork artwork;
    public String getIsSubmitDepositPrice() {
        return isSubmitDepositPrice;
    }

    public String getArtWorkBidding() {
        return artWorkBidding;
    }

    public Artwork getArtwork() {
        return artwork;
    }
}



