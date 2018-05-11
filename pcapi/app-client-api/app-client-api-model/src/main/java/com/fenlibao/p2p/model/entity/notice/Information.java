package com.fenlibao.p2p.model.entity.notice;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/9.
 */
public class Information {

    private Integer informationId;

    private String informationTitle;//文章标题f06

    private String source;//来源07

    private String introduction;//文章摘要08

    private Date publishTime;//发布时间12

    private String imgcode;//封面图片文件编码09

    private String channel;//媒体渠道

    private String keyword;//关键字

    private Integer preInformationId;

    private String preInformationTitle;

    private Integer NextInformationId;

    private String NextInformationTitle;

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

    public String getImgcode() {
        return imgcode;
    }

    public void setImgcode(String imgcode) {
        this.imgcode = imgcode;
    }

    public Integer getInformationId() {
        return informationId;
    }

    public void setInformationId(Integer informationId) {
        this.informationId = informationId;
    }

    public String getInformationTitle() {
        return informationTitle;
    }

    public void setInformationTitle(String informationTitle) {
        this.informationTitle = informationTitle;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getPreInformationId() {
        return preInformationId;
    }

    public void setPreInformationId(Integer preInformationId) {
        this.preInformationId = preInformationId;
    }

    public String getPreInformationTitle() {
        return preInformationTitle;
    }

    public void setPreInformationTitle(String preInformationTitle) {
        this.preInformationTitle = preInformationTitle;
    }

    public Integer getNextInformationId() {
        return NextInformationId;
    }

    public void setNextInformationId(Integer nextInformationId) {
        NextInformationId = nextInformationId;
    }

    public String getNextInformationTitle() {
        return NextInformationTitle;
    }

    public void setNextInformationTitle(String nextInformationTitle) {
        NextInformationTitle = nextInformationTitle;
    }
}
