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
package com.fenlibao.p2p.model.entity.notice;

import java.util.Date;

/** 
 * @ClassName: NoticeInfoDetailVO 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午9:39:21  
 */
public class NoticeInfoDetail extends NoticeInfo{
	
	public Date createTime; //创建时间
	
	public String content; //公告内容

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
}
