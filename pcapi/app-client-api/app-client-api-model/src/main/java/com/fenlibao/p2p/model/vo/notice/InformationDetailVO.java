package com.fenlibao.p2p.model.vo.notice;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/10.
 */
public class InformationDetailVO {

    private Integer informationId;

    private String informationTitle;//文章标题f06

    private String source;//来源07

    private String introduction;//文章摘要08

    private Date publishTime;//发布时间12

    private String imgUrl;//封面图片文件编码09

    private String content; //内容

    private String channel;//媒体渠道

    private String keyword;//关键字

    private Integer preInformationId;

    private String preInformationTitle;

    private Integer NextInformationId;

    private String NextInformationTitle;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
}
