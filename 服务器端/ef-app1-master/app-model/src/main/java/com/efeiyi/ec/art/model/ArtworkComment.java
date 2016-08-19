package com.efeiyi.ec.art.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import com.efeiyi.ec.art.organization.model.User;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 *
 */
@Entity
@Table(name = "app_art_work_comment")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class ArtworkComment implements Serializable {

    private String id;
    private Artwork artwork;
    private String content;
    private User creator;
    private Date createDatetime;
    private String status;
    private String isWatch;
    private ArtworkComment fatherComment;
    private List<ArtworkComment> subComment;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL,CascadeType.REFRESH })
    @JoinColumn(name = "art_work_id")
    @JsonIgnore
    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
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
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
    @Column(name = "create_datetime")
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "is_watch")
    public String getIsWatch() {
        return isWatch;
    }

    public void setIsWatch(String isWatch) {
        this.isWatch = isWatch;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_comment_id")
    @JsonIgnore
    public ArtworkComment getFatherComment() {
        return fatherComment;
    }

    public void setFatherComment(ArtworkComment fatherComment) {
        this.fatherComment = fatherComment;
    }
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "fatherComment", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<ArtworkComment> getSubComment() {
        return subComment;
    }

    public void setSubComment(List<ArtworkComment> subComment) {
        this.subComment = subComment;
    }
}
