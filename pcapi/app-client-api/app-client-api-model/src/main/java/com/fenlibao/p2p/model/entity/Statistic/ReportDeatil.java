package com.fenlibao.p2p.model.entity.Statistic;

/**
 * 财务审计报告
 * Created by chen on 2018/3/15.
 */
public class ReportDeatil {

    private  Integer id;

    private  String title;

    private  String url;

    private  int Status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
