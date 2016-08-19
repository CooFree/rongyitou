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
 * Created by Administrator on 2016/1/25.
 * 投资收益表
 *
 */
@Deprecated
@Entity
@Table(name = "app_invest_reward")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class InvestReward implements Serializable {
    private String id;
    private User investUser;//投资人
    private Artwork artwork;//投资项目
    private Date createDatetime;//收益时间
    private BigDecimal reward;//投资回报金额
    private String status;//状态
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
    @JoinColumn(name = "invest_user_id")
    public User getInvestUser() {
        return investUser;
    }

    public void setInvestUser(User investUser) {
        this.investUser = investUser;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_work_id")
    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    @Column(name = "create_datetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Column(name = "reward")
    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
