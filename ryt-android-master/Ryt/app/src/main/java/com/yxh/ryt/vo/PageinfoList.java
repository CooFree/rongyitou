package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class PageinfoList implements Serializable {
    private String id;
    private long createDateTime;
    private String status;
    private String watch;
    private MyZan artwork;
    public String getId() {
        return id;
    }

    public MyZan getArtwork() {
        return artwork;
    }

    public void setArtwork(MyZan artwork) {
        this.artwork = artwork;
    }

    public void setId(String id) {
        this.id = id;
    }


    public long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }
}
