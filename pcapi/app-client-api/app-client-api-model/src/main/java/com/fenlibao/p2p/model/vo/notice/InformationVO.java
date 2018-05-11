package com.fenlibao.p2p.model.vo.notice;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/9.
 */
public class InformationVO {

    public Integer informationId;

    public String informationTitle;//文章标题f06

    public String source;//来源07

    public String introduction;//文章摘要08

    public Date publishTime;//发布时间12

    public String imgUrl;//封面图片文件编码09

    public String informationDetailUrl; //详情

    public String channel;//媒体渠道

    public String getInformationDetailUrl() {
        return informationDetailUrl;
    }

    public void setInformationDetailUrl(String informationDetailUrl) {
        this.informationDetailUrl = informationDetailUrl;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
