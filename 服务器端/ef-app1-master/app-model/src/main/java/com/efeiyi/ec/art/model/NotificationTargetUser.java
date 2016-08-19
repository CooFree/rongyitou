package com.efeiyi.ec.art.model;

import com.efeiyi.ec.art.organization.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
@Entity
@Table(name = "app_art_notification_targetUsers")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class NotificationTargetUser implements Serializable {
    private String id;
    private User targetUser;//通知目标
    private Notification Notification;//通知目标

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
    @JoinColumn(name = "user_ID")
    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_ID")
    public com.efeiyi.ec.art.model.Notification getNotification() {
        return Notification;
    }

    public void setNotification(com.efeiyi.ec.art.model.Notification notification) {
        Notification = notification;
    }
}
