package com.yxh.ryt.vo;

import java.io.Serializable;


public class Artworkdirection implements Serializable {

    private String id;
    private String make_instru;//制作说明
    private String financing_aq;//融资答疑
    private Artwork artwork;//关联项目

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake_instru() {
        return make_instru;
    }

    public void setMake_instru(String make_instru) {
        this.make_instru = make_instru;
    }

    public String getFinancing_aq() {
        return financing_aq;
    }

    public void setFinancing_aq(String financing_aq) {
        this.financing_aq = financing_aq;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}
