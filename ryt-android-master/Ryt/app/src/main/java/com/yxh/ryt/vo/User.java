package com.yxh.ryt.vo;


//import com.efeiyi.ec.zero.promotion.model.PromotionPlan;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * id : in9xyax5cagsn8g7
     * username : 13466636718
     * name : null
     * name2 : null
     * password : 9ac5ebe1c16098ece769d3338d81467a339d3cf0
     * status : 1
     * confirmPassword : null
     * oldPassword : null
     * enabled : true
     * accountExpired : false
     * accountLocked : false
     * credentialsExpired : false
     * utype : 10000
     * lastLoginDatetime : null
     * lastLogoutDatetime : null
     * createDatetime : 1461222170000
     * source : null
     * fullName : null[13466636718]
     * accountNonExpired : true
     * accountNonLocked : true
     * credentialsNonExpired : true
     */
//    private String signMessage;
    private String master1;
    private String sex;
    private String id;
    private String username;
    private String name;
    private String name2;
    private String password;
    private int status;
    private String confirmPassword;
    private String oldPassword;
    private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private int utype;
    private int fansNum;
    private long lastLoginDatetime;
    private long lastLogoutDatetime;
    private long createDatetime;
    private String source;
    private String fullName;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private String flag;
    private int rate;
    private int investsMoney;
    private int count;
    private UserBrief userBrief;
    private int roiMoney;
    private int count1;
    private String pictureUrl;
    private String loginState;
    private int masterWorkNum;
    private String type;
    public String getMaster1() {
        return master1;
    }

    public void setMaster1(String master1) {
        this.master1 = master1;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }
    /* public String getSignMessage() {
        return signMessage;
    }

    public void setsignMessage(String signMessage) {
        this.signMessage = signMessage;
    }*/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMasterWorkNum() {
        return masterWorkNum;
    }

    public void setMasterWorkNum(int masterWorkNum) {
        this.masterWorkNum = masterWorkNum;
    }

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getInvestsMoney() {
        return investsMoney;
    }

    public void setInvestsMoney(int investsMoney) {
        this.investsMoney = investsMoney;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRoiMoney() {
        return roiMoney;
    }

    public void setRoiMoney(int roiMoney) {
        this.roiMoney = roiMoney;
    }

    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }


    public UserBrief getUserBrief() {
        return userBrief;
    }

    public void setUserBrief(UserBrief userBrief) {
        this.userBrief = userBrief;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    //    private String id;
//    private String username;
//    private String name;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    //    private String cityId;
//    private String password;
////    private Role role;
//    private String status;
//    protected long createDatetime;
//    private String type; //00000 普通用户 10000 艺术家
    private Master master; //用户关联的大师

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

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

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public int getUtype() {
        return utype;
    }

    public void setUtype(int utype) {
        this.utype = utype;
    }

    public long getLastLoginDatetime() {
        return lastLoginDatetime;
    }

    public void setLastLoginDatetime(long lastLoginDatetime) {
        this.lastLoginDatetime = lastLoginDatetime;
    }

    public long getLastLogoutDatetime() {
        return lastLogoutDatetime;
    }

    public void setLastLogoutDatetime(long lastLogoutDatetime) {
        this.lastLogoutDatetime = lastLogoutDatetime;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public User() {
    }
}
