package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.List;


public class Master implements Serializable{
    private String id;
    private String brief; // 简介(短)
    private String title; // 头衔/称号
    private String favicon; //网站头像
    private String birthday; //出生年月
    private String level; //等级
    private String content; // 简介(长)
    private String presentAddress; //现居地
    private String backgroundUrl;
    private String provinceName;//籍贯/详细地址
//    private AddressProvince originProvince; //籍贯（省）
    private String theStatus;         // 正常，删除，停止，隐藏
    private String logoUrl;
    private String masterSpeech;
    private User user; //大师跟用户的关系映射
//    private List<Artwork> artworks;
    private String artCategory;
    private String titleCertificate;
//    private List<ArtMasterAttachment> workShopPhotos;//工作室照片
//    private List<ArtMasterAttachment> worksPhotos;//作品照片


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getTheStatus() {
        return theStatus;
    }

    public void setTheStatus(String theStatus) {
        this.theStatus = theStatus;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getMasterSpeech() {
        return masterSpeech;
    }

    public void setMasterSpeech(String masterSpeech) {
        this.masterSpeech = masterSpeech;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getArtCategory() {
        return artCategory;
    }

    public void setArtCategory(String artCategory) {
        this.artCategory = artCategory;
    }

    public String getTitleCertificate() {
        return titleCertificate;
    }

    public void setTitleCertificate(String titleCertificate) {
        this.titleCertificate = titleCertificate;
    }
}
