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

import java.util.Date;

/** 
 * @ClassName: NoticeInfoDetailVO 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:39:21  
 */
public class NoticeInfoDetailVO {
	
	public int noticeId;//公告id
	
	public int noticeType;//公告类型
	
	public String noticeTitle;//公告标题
	
	public Long createTime; //创建时间戳
	
	public String content; //公告内容

	
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
