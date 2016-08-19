package com.yxh.ryt.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ArtworkInvestTop implements Serializable {
    /* private User user;
        private String money;

        public ArtworkInvestTop() {
        }

        public ArtworkInvestTop(User user, String money) {
            this.user = user;
            this.money = money;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }*/
    private BigDecimal price;

    private User creator;
    private long createDatetime;

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getCreator() {
        return creator;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
