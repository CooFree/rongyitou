package com.efeiyi.ec.art.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/2/25.
 */
@Entity
@Table(name = "master_work_picture")
public class ArtMasterAttachmentPicture {
    private String id;
    private String pictureUrl;
    private String status;
    private ArtMasterAttachment attachment;
    private String pictureType;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "picture_url")
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    public ArtMasterAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(ArtMasterAttachment attachment) {
        this.attachment = attachment;
    }

    @Column(name = "picture_type")
    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }
}
