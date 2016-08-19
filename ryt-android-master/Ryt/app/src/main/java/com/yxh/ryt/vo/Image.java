package com.yxh.ryt.vo;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Image implements Serializable {

    /**
     * id : imycmhjo27gzgcau
     * fileType : .png
     * fileName : http://rongyitou2.efeiyi.com/artwork/1460521216818Screenshot_2016-04-13-06-53-26.png
     */

    private String id;
    private String fileType;
    private String fileName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
