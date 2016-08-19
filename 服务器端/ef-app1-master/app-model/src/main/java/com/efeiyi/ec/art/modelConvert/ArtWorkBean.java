package com.efeiyi.ec.art.modelConvert;

import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.model.Master;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/1/25.
 *
 */
public class ArtWorkBean {

    private Artwork artwork;

    private Master master;

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
