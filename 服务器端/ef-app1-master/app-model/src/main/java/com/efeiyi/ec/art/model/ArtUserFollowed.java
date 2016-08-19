package com.efeiyi.ec.art.model;

import com.efeiyi.ec.art.organization.model.MyUser;
import com.efeiyi.ec.art.organization.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/18.
 *
 */
@Entity
@Table(name="app_art_user_followed")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ArtUserFollowed implements Serializable{
    private String id;
    private MyUser user;//关注着
    private MyUser follower;//被关注者
    private String status;
    private String type;//1.关注艺术家 2.关注普通用户
    private Date createDatetime;
    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
    @Column(name="status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name="type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name="create_datetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    public MyUser getFollower() {
        return follower;
    }

    public void setFollower(MyUser follower) {
        this.follower = follower;
    }
}




