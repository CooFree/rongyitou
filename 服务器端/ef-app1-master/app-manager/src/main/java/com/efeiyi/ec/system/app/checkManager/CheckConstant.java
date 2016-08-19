package com.efeiyi.ec.system.app.checkManager;

/**
 * Created by Administrator on 2016/4/7.
 * 审核管理 状态常量
 */
public class CheckConstant {
    //项目-Artwork状态常量
    public static final String ARTWORK_STATUS_REMOVE = "0";//删除-假删
    public static final String ARTWORK_STATUS_FINANCING = "1";//融资阶段
    public static final String ARTWORK_STATUS_PRODUCTION = "2";//制作阶段
    public static final String ARTWORK_STATUS_SALE = "3";//拍卖阶段
    public static final String ARTWORK_STATUS_DRAW = "4";//抽奖阶段

    //项目审核-Artwork状态(融资阶段)常量
    public static final String ARTWORK_STEP_WAIT = "10";//待审核
    public static final String ARTWORK_STEP_CHECKING= "11";//审核中
    public static final String ARTWORK_STEP_PASS = "12";//审核通过
    public static final String ARTWORK_STEP_REJECT = "13";//审核未通过，已驳回

    //艺术家审核-Artist状态常量
    public static final String ARTIST_STATUS_REMOVE = "0";//删除-假删
    public static final String ARTIST_STATUS_WAIT = "1";//待审核
    public static final String ARTIST_STATUS_CHECKING = "2";//审核中
    public static final String ARTIST_STATUS_PASS = "3";//审核通过
    public static final String ARTIST_STATUS_REJECT = "4";//审核未通过，已驳回

}
