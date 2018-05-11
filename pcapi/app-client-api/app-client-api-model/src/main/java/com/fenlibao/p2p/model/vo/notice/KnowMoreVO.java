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
public class KnowMoreVO {
	
	public int informationId;//公告id

	public String informationTitle;//公告标题
	
	public Long createTime; //创建时间戳
	
	public String content; //公告内容

	public String source;//来源07

	public String introduction;//文章摘要08

	public Long publishTime;//发布时间12

	public String imgUrl;


	
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


	public int getInformationId() {
		return informationId;
	}

	public void setInformationId(int informationId) {
		this.informationId = informationId;
	}

	public String getInformationTitle() {
		return informationTitle;
	}

	public void setInformationTitle(String informationTitle) {
		this.informationTitle = informationTitle;
	}
}
