package com.efeiyi.ec.art.base.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/11.
 *
 */
@Entity
@Table(name = "app_art_logs")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class LogBean  implements Serializable {
    private String id;
    private String resultCode;
    private String msg;
    private String requestMessage;
    private Date createDate;
    private String apiName;
    private String extend1;//添加一个扩展字段


    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "resultCode")
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    @Column(name = "msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    @Column(name = "requestMessage")
    public String getRequestMessage() {
        return requestMessage;
    }
    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
    @Column(name = "createDate")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "api")
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
    @Column(name = "extend1")
    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }
}
