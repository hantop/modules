package com.fenlibao.p2p.model.vo.dbfile;

import java.sql.Timestamp;

/**
 * db文件
 * Created by chenzhixuan on 2015/9/8.
 */
public class DBFileVO {
    private int type;
    private String dbVersion;
    private String url;
    private String fileName;
    private Timestamp lastChangetime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Timestamp getLastChangetime() {
        return lastChangetime;
    }

    public void setLastChangetime(Timestamp lastChangetime) {
        this.lastChangetime = lastChangetime;
    }
}
