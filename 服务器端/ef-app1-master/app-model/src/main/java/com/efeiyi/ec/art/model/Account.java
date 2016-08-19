package com.efeiyi.ec.art.model;

import com.efeiyi.ec.art.organization.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/2/20.
 *
 */
@Entity
@Table(name = "app_art_work_account")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Account implements Serializable {
    private String id;
    private User user;//关联用户
    private String type;//账户类别
    private String number;//账号
    private String password;//提现或交易密码
    private BigDecimal currentBalance;//当前余额
    private BigDecimal currentUsableBalance;//当前可用余额
    private String status;
    private Date createDatetime;
    private List<BankCard> bankCards;//关联的银行卡
    private List<MarginAccount> marginAccounts;//关联的保证金账户
    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "create_datetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "account")
    public List<BankCard> getBankCards() {
        return bankCards;
    }

    public void setBankCards(List<BankCard> bankCards) {
        this.bankCards = bankCards;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "current_balance")
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
    @Column(name = "current_usable_balance")
    public BigDecimal getCurrentUsableBalance() {
        return currentUsableBalance;
    }

    public void setCurrentUsableBalance(BigDecimal currentUsableBalance) {
        this.currentUsableBalance = currentUsableBalance;
    }
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "account")
    public List<MarginAccount> getMarginAccounts() {
        return marginAccounts;
    }

    public void setMarginAccounts(List<MarginAccount> marginAccounts) {
        this.marginAccounts = marginAccounts;
    }
}
