package com.efeiyi.ec.art.model;

import com.efeiyi.ec.art.organization.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/6.
 *
 */
@Entity
@Table(name = "app_art_work_roi_record")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ROIRecord implements Serializable {//投资收益记录表
    private String id;
    private BigDecimal currentBalance;//回报金额
    private String status;//1 成功 0 失败
    private Date createDatetime;//收益时间
    private Account account;//关联的账户
    private User user;//关联用户
    private String accountNum;//from 账号
    private String details;//收益详情
    private Artwork artwork;//投资项目
    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "details")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    @Column(name = "accountNum")
    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    @Column(name = "createDatetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "currentBalance")
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_work_id")
    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}
