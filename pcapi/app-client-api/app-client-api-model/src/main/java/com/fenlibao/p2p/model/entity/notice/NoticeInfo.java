/**
 * Copyright © 2015 fenlibao.com. All rights reserved.
 *
 * @Title: NoticeInfoVO.java
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.notice
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:39:08
 * @version: V1.1
 */
package com.fenlibao.p2p.model.entity.notice;

import java.util.Date;

/**
 * @ClassName: NoticeInfoVO
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:39:08
 */
public class NoticeInfo {

    private int noticeId;//公告id

    private String noticeType;//公告类型

    private String noticeTitle;//公告标题

    private String keyword;//关键字

    private String introduction;//摘要

    private Date createtime;

    private Integer preNoticeId;

    private String preNoticeTitle;

    private Integer nextNoticeId;

    private String nextNoticeTitle;

    public Integer getPreNoticeId() {
        return preNoticeId;
    }

    public void setPreNoticeId(Integer preNoticeId) {
        this.preNoticeId = preNoticeId;
    }

    public String getPreNoticeTitle() {
        return preNoticeTitle;
    }

    public void setPreNoticeTitle(String preNoticeTitle) {
        this.preNoticeTitle = preNoticeTitle;
    }

    public Integer getNextNoticeId() {
        return nextNoticeId;
    }

    public void setNextNoticeId(Integer nextNoticeId) {
        this.nextNoticeId = nextNoticeId;
    }

    public String getNextNoticeTitle() {
        return nextNoticeTitle;
    }

    public void setNextNoticeTitle(String nextNoticeTitle) {
        this.nextNoticeTitle = nextNoticeTitle;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * @return the noticeId
     */
    public int getNoticeId() {
        return noticeId;
    }

    /**
     * @param noticeId the noticeId to set
     */
    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    /**
     * @return the noticeType
     */
    public String getNoticeType() {
        return noticeType;
    }

    /**
     * @param noticeType the noticeType to set
     */
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * @return the noticeTitle
     */
    public String getNoticeTitle() {
        return noticeTitle;
    }

    /**
     * @param noticeTitle the noticeTitle to set
     */
    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
