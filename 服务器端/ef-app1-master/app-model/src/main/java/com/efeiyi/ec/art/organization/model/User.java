package com.efeiyi.ec.art.organization.model;



//import com.efeiyi.ec.zero.promotion.model.PromotionPlan;
import com.efeiyi.ec.art.model.Master;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

//不要把jsonIgnore注释了
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "organization_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {

    private String id;
    private String username;
    private String name;
    private String pictureUrl;
    private String cityId;
    private String password;
    private Role role;
    private String status;
    protected Date createDatetime;
    private String type; //00000 普通用户 10000 艺术家
    private Master master; //用户关联的大师


    @OneToOne(mappedBy="user",fetch=FetchType.LAZY)
    @JoinColumn(name = "master_id")
    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    @JsonIgnore
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "city_id")
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Column(name = "picture")
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Column(name = "truename")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BigUser user = (BigUser) o;

        if (!id.equals(user.id)) return false;
        if (!username.equals(user.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if(id!=null) {
            result  = id.hashCode();
            result = 31 * result;
            if (username != null) {
                result += username.hashCode();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", cityId='" + cityId + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", status='" + status + '\'' +
                ", createDatetime=" + createDatetime +
                ", type='" + type + '\'' +
                '}';
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
}
