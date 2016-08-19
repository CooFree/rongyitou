package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/22.
 *
 */
public class MasterWork implements Serializable{
    private String id;
    private String name;//作品名称
    private String type;//是否出售 0 否 1 是
    private String status;//状态
    private String material;//材质
    private long createDatetime;//创建时间
    private UserMaster creator;//大师
    private String pictureUrl;//作品图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public UserMaster getCreator() {
        return creator;
    }

    public void setCreator(UserMaster creator) {
        this.creator = creator;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}


