package com.efeiyi.ec.art.model;

import com.efeiyi.ec.art.organization.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/2/2.
 *
 */
@Entity
@Table(name = "app_art_notification")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Notification implements Serializable {//系统通知
    private String id;
    private String content;
    private User targetUser;//通知目标
    //private List<User> targetUsers;//通知目标
    private Date createDatetime;
    private Artwork artwork;
    private String isWatch;// 0 未读 1 已读
    private String status; // 是否删除
    private User fromUser;//触发用户 比如此人关注了targetUser

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
    @Column(name = "create_datetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_work_id")
    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    @Column(name = "is_watch")
    public String getIsWatch() {
        return isWatch;
    }

    public void setIsWatch(String isWatch) {
        this.isWatch = isWatch;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

 /*   @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "app_art_notification_targetUsers",
            joinColumns = {@JoinColumn(name = "notification_ID", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_ID", referencedColumnName ="id")})
    public List<User> getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(List<User> targetUsers) {
        this.targetUsers = targetUsers;
    }*/
}


