package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Auther implements Serializable {


    /**
     * id : ieatht97wfw30hfd
     * username : 15110008479
     * name : 温群英
     * pictureUrl : http://tenant.efeiyi.com/background/蔡水况.jpg
     * cityId : null
     * status : 1
     * createDatetime : 1441684108000
     * type : 10000
     * master : null
     */

    private String id;
    private String username;
    private String name;
    private String pictureUrl;
    private Object cityId;
    private String status;
    private long createDatetime;
    private String type;
    private Master master;
    private int masterWorkNum;
    private int fansNum;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getMasterWorkNum() {
        return masterWorkNum;
    }

    public void setMasterWorkNum(int masterWorkNum) {
        this.masterWorkNum = masterWorkNum;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Object getCityId() {
        return cityId;
    }

    public void setCityId(Object cityId) {
        this.cityId = cityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
