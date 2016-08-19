package com.efeiyi.ec.art.model;

import com.efeiyi.ec.art.organization.model.MyUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/20.
 *
 */
@Entity
@Table(name = "app_organization_artist")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Artist {
    private String id;
    private String brief; // 简介(短)
    private String title; // 头衔/称号
    private String favicon; //网站头像
    private String level; //等级
    private String content; // 简介(长)
    private String presentAddress; //现居地
    private String backgroundUrl;
    private String provinceName;//籍贯
    private String logoUrl;
    private Date createDateTime;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "artist_brief")
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
    @Column(name = "artist_title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Column(name = "artist_favicon")
    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }



    @Column(name = "artist_level")
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    @Column(name = "artist_content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Column(name = "artist_presentAddress")
    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }
    @Column(name = "artist_backgroundUrl")
    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }
    @Column(name = "artist_provinceName")
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }


    @Column(name = "artist_logoUrl")
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    @Column(name = "artist_createDateTime")
    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }
}
