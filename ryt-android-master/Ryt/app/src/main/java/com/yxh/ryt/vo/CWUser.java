package com.yxh.ryt.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/5/5.
 */
public class CWUser implements Serializable {

    /**
     * id : ieatht97wfw30hfd
     * username : 15110008479
     * name : 温群英
     * pictureUrl : http://tenant.efeiyi.com/background/蔡水况.jpg
     * cityId : null
     * status : 1
     * createDatetime : 1441684108000
     * type : 10000
     * master : {"id":"ich9th9y00008h8v","brief":"中国工艺美术大师、高级工艺美术师、福建省工艺美术大师、中国工艺美术学会会员、美国海外艺术家协会理事、福建省工艺美术学会常务理事、协会会员、首届厦门工艺美术学会常务付理事长。","title":"","favicon":"photo/20150729144701.jpg","birthday":"1939年","level":"1","content":"","presentAddress":"福建","backgroundUrl":"background/蔡水况.jpg","provinceName":"福建","theStatus":"1","logoUrl":"logo/蔡水况.jpg","masterSpeech":null,"artCategory":null,"titleCertificate":null,"feedback":"份","identityFront":null,"identityBack":null}
     */

    private String id;
    private String username;
    private String name;
    private String pictureUrl;
    private Object cityId;
    private String status;
    private BigDecimal createDatetime;
    private String type;
    private Master master;
    public String getId() {
        return id;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
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

    public BigDecimal getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(BigDecimal createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
