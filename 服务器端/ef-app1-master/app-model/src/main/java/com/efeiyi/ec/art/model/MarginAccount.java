package com.efeiyi.ec.art.model;

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
@Table(name = "app_art_work_margin_account")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class MarginAccount implements Serializable {//保证金表

    private String id;
    private BigDecimal currentBalance;//当前余额
    private String status;//0 已冻结 1 已使用 3 已解冻
    private Date createDatetime;//创建时间 即冻结时间
    private Date endDatetime;//解冻时间/使用时间
    private Artwork artwork;//关联的项目
    private Account account;//关联的账户
    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
    @Column(name = "createDatetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Column(name = "endDatetime")
    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id")
    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}
