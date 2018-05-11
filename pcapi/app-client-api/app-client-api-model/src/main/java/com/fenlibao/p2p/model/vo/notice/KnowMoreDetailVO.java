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
public class KnowMoreDetailVO {
	
	public int id;//公告id

	public String title;//公告标题
	
	public Long createTime; //创建时间戳
	
	public String content; //公告内容

	public String source;//来源07

	public String introduction;//文章摘要08

	public Long publishTime;//发布时间12

	public String imgUrl;

	private String preId;

	private String preTitle;

	private String nextId;

	private String nextTitle;

	private String url;


	
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



	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	public String getPreId() {
		return preId;
	}

	public void setPreId(String preId) {
		this.preId = preId;
	}

	public String getNextTitle() {
		return nextTitle;
	}

	public void setNextTitle(String nextTitle) {
		this.nextTitle = nextTitle;
	}


	public String getPreTitle() {
		return preTitle;
	}

	public void setPreTitle(String preTitle) {
		this.preTitle = preTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
