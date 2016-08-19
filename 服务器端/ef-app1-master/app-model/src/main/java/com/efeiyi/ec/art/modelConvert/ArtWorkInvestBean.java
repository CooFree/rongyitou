package com.efeiyi.ec.art.modelConvert;

import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.model.Master;
import com.efeiyi.ec.art.organization.model.User;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/25.
 *
 */
public class ArtWorkInvestBean {

    private Artwork artwork;

    private BigDecimal investMoney;

    private Master master;

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
