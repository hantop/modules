package com.fenlibao.p2p.model.entity.notice;

import java.util.Date;

/**
 * @author: junda.feng
 */
public class Article {

    private Integer articleId;

    private String articleTitle;//文章标题f06

    private String source;//来源07

    private String introduction;//文章摘要08

    private Date publishTime;//发布时间12

    private String imgcode;//封面图片文件编码09

    private String channel;//媒体渠道

    private String keyword;

    private Integer preArticleId;

    private String preArticleTitle;

    private Integer nextArticleId;

    private String nextArticleTitle;

    public Integer getNextArticleId() {
        return nextArticleId;
    }

    public void setNextArticleId(Integer nextArticleId) {
        this.nextArticleId = nextArticleId;
    }

    public Integer getPreArticleId() {
        return preArticleId;
    }

    public void setPreArticleId(Integer preArticleId) {
        this.preArticleId = preArticleId;
    }

    public String getPreArticleTitle() {
        return preArticleTitle;
    }

    public void setPreArticleTitle(String preArticleTitle) {
        this.preArticleTitle = preArticleTitle;
    }

    public String getNextArticleTitle() {
        return nextArticleTitle;
    }

    public void setNextArticleTitle(String nextArticleTitle) {
        this.nextArticleTitle = nextArticleTitle;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getImgcode() {
        return imgcode;
    }

    public void setImgcode(String imgcode) {
        this.imgcode = imgcode;
    }
}