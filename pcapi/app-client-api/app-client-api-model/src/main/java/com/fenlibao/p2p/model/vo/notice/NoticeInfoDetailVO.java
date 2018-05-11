/**
 * Copyright © 2015 fenlibao.com. All rights reserved.
 *
 * @Title: NoticeInfoDetailVO.java
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.notice
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:39:21
 * @version: V1.1
 */
package com.fenlibao.p2p.model.vo.notice;

/**
 * @ClassName: NoticeInfoDetailVO
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:39:21
 */
public class NoticeInfoDetailVO {

    private int noticeId;//公告id

    private int noticeType;//公告类型

    private String noticeTitle;//公告标题

    private Long createTime; //创建时间戳

    private String content; //公告内容

    private String keyword;//关键字

    private String introduction;//摘要

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

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the createTime
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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
    public int getNoticeType() {
        return noticeType;
    }

    /**
     * @param noticeType the noticeType to set
     */
    public void setNoticeType(int noticeType) {
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

}
