package com.efeiyi.ec.art.artwork.service;

import com.alibaba.fastjson.JSONObject;
import com.efeiyi.ec.art.base.model.LogBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/14.
 *
 */
public interface ArtworkInvestManager {
    public Map artworkInvest(HttpServletRequest request,JSONObject jsonObj,LogBean logBean);
}
