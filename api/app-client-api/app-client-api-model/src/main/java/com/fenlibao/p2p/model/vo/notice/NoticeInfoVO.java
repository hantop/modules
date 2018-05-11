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
package com.fenlibao.p2p.model.vo.notice;

/** 
 * @ClassName: NoticeInfoVO 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:39:08  
 */
public class NoticeInfoVO {
	
	public int noticeId;//公告id
	
	public String noticeType;//公告类型
	
	public long createtime;
	
	//2016-06-29 add by:junda.feng
	public String summary; //公告摘要
	
	
	

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String noticeTitle;//公告标题
	
	public String noticeDetailUrl; //公告详情

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

	/**
	 * @return the noticeDetailUrl
	 */
	public String getNoticeDetailUrl() {
		return noticeDetailUrl;
	}

	/**
	 * @param noticeDetailUrl the noticeDetailUrl to set
	 */
	public void setNoticeDetailUrl(String noticeDetailUrl) {
		this.noticeDetailUrl = noticeDetailUrl;
	}
	
}
