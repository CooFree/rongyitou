package com.yxh.ryt.wxapi;

import java.io.Serializable;

public class Wxuser implements Serializable{

	private static final long serialVersionUID = 1L;

	private String openid;

	private String nickname;

	private String sex;

	private String language;

	private String city;

	private String province;

	private String country;

	private String headimgurl;

	private String unionid;

	public void setOpenid(String openid){
	this.openid = openid;
	}
	public String getOpenid(){
	return this.openid;
	}
	public void setNickname(String nickname){
	this.nickname = nickname;
	}
	public String getNickname(){
	return this.nickname;
	}
	public void setSex(String sex){
	this.sex = sex;
	}
	public String getSex(){
	return this.sex;
	}
	public void setLanguage(String language){
	this.language = language;
	}
	public String getLanguage(){
	return this.language;
	}
	public void setCity(String city){
	this.city = city;
	}
	public String getCity(){
	return this.city;
	}
	public void setProvince(String province){
	this.province = province;
	}
	public String getProvince(){
	return this.province;
	}
	public void setCountry(String country){
	this.country = country;
	}
	public String getCountry(){
	return this.country;
	}
	public void setHeadimgurl(String headimgurl){
	this.headimgurl = headimgurl;
	}
	public String getHeadimgurl(){
	return this.headimgurl;
	}
	public void setUnionid(String unionid){
	this.unionid = unionid;
	}
	public String getUnionid(){
	return this.unionid;
	}
	
	public Wxuser(String openid, String nickname, String sex, String language,
			String city, String province, String country, String headimgurl,
			 String unionid) {
		super();
		this.openid = openid;
		this.nickname = nickname;
		this.sex = sex;
		this.language = language;
		this.city = city;
		this.province = province;
		this.country = country;
		this.headimgurl = headimgurl;
		this.unionid = unionid;
	}
	public Wxuser() {
	}
	
}
