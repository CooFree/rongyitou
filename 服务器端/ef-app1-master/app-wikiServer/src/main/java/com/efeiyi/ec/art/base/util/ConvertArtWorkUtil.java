package com.efeiyi.ec.art.base.util;

import com.efeiyi.ec.art.model.Artwork;
import com.efeiyi.ec.art.model.ArtworkInvest;
import com.efeiyi.ec.art.modelConvert.ConvertArtWork;
import com.efeiyi.ec.art.modelConvert.ConvertWork;
import com.efeiyi.ec.art.organization.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public class ConvertArtWorkUtil {
    public static ConvertArtWork convert(List<ArtworkInvest> invests, int followedList, int toFollowedList,
                                         List<BigDecimal> investMoney, BigDecimal sumInvestsMoney, BigDecimal reward, User user) {
        ConvertArtWork convert = new ConvertArtWork();
        convert.setFollowNum(followedList <= 0 ? 0 : followedList);
        convert.setNum(toFollowedList <= 0 ? 0 : toFollowedList);
        convert.setYield(reward);
        convert.setSumInvestment(sumInvestsMoney);
        List<ConvertWork> list = new ArrayList<ConvertWork>();
        if (invests != null && invests.size() > 0) {
            for (int i = 0; i < invests.size(); i++) {
                Artwork artwork = invests.get(i).getArtwork();
                ConvertWork work = artWork(artwork, investMoney.get(i));
                list.add(work);
            }
        }
        convert.setArtworks(list);
        convert.setUser(user);
        return convert;
    }

    public static ConvertWork artWork(Artwork artwork, BigDecimal price) {
        ConvertWork work = null;
        if (artwork != null && artwork.getId() != null) {
            work = new ConvertWork();
            work.setId(artwork.getId());
            work.setBrief(artwork.getBrief() == null ? "" : artwork.getBrief());
            work.setInvestsMoney(price);
            work.setPicture_url(artwork.getPicture_url() == null ? "" : artwork.getPicture_url());
            work.setStep(artwork.getStep());
            work.setTitle(artwork.getTitle() == null ? "" : artwork.getTitle());
        }
        return work;
    }

    public static ConvertArtWork convert2(List<Artwork> artworks, int followedList, int toFollowedList,
                                          BigDecimal sumInvestsMoney, BigDecimal reward, User user) {
        ConvertArtWork convert = new ConvertArtWork();
        convert.setFollowNum(followedList <= 0 ? 0 : followedList);
        convert.setNum(toFollowedList <= 0 ? 0 : toFollowedList);
        convert.setYield(reward);
        convert.setSumInvestment(sumInvestsMoney);
        List<ConvertWork> list = new ArrayList<ConvertWork>();
        if (artworks != null && artworks.size() > 0) {
            for (Artwork artwork : artworks) {
                ConvertWork work = artWork(artwork, artwork.getInvestsMoney());
                list.add(work);
            }
        }
        convert.setArtworks(list);
        convert.setUser(user);
        return convert;
    }
}
