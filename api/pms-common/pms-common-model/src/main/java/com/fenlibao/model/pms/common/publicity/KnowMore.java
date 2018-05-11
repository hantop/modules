package com.fenlibao.model.pms.common.publicity;

import java.util.Date;

/**
 * 网贷知多点
 * Created by Administrator on 2018/3/5.
 */
public class KnowMore {

    private int id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 位置顺序
     */
    private int sorting;

    /**
     * 原文链接
     */
    private String url;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 文章摘要
     */
    private String digest;

    /**
     * 文章正文
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 发布状态,0未发布，1已发布
     */
    private int status;

    /**
     * 发布者id
     */
    private String publisherId;

    private String allPicUploade;

    private String newUploaded;

    public String getNewUploaded() {
        return newUploaded;
    }

    public void setNewUploaded(String newUploaded) {
        this.newUploaded = newUploaded;
    }

    public String getAllPicUploade() {
        return allPicUploade;
    }

    public void setAllPicUploade(String allPicUploade) {
        this.allPicUploade = allPicUploade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }
}
