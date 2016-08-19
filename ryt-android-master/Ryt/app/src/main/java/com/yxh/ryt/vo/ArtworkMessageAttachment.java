package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;

public class ArtworkMessageAttachment implements Serializable{//项目和动态附件
    private String id;
    private ArtworkMessage artworkMessage;
    private String FileUri;
    private String FileType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArtworkMessage getArtworkMessage() {
        return artworkMessage;
    }

    public void setArtworkMessage(ArtworkMessage artworkMessage) {
        this.artworkMessage = artworkMessage;
    }

    public String getFileUri() {
        return FileUri;
    }

    public void setFileUri(String fileUri) {
        FileUri = fileUri;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }
}


