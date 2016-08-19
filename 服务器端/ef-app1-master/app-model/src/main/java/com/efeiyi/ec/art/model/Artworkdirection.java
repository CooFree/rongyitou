package com.efeiyi.ec.art.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by Administrator on 2016/4/12.
 *
 */
@Entity
@Table(name = "app_art_work_direction")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Artworkdirection implements Serializable {

    private String id;
    private String make_instru;//制作说明
    private String financing_aq;//融资答疑
    private Artwork artwork;//关联项目
    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "make_instru")
    public String getMake_instru() {
        return make_instru;
    }

    public void setMake_instru(String make_instru) {
        this.make_instru = make_instru;
    }
    @Column(name = "financing_aq")
    public String getFinancing_aq() {
        return financing_aq;
    }

    public void setFinancing_aq(String financing_aq) {
        this.financing_aq = financing_aq;
    }

    @JsonIgnore
    @OneToOne(mappedBy = "artworkdirection")
    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}
