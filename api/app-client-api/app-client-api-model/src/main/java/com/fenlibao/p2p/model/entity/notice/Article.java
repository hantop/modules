package com.fenlibao.p2p.model.entity.notice;

import java.util.Date;
/** 
 * @author: junda.feng
 */
public class Article {
	
	public Integer articleId;

    public String articleTitle;//文章标题f06

    public String source;//来源07
    
    public String introduction;//文章摘要08

	public Date publishTime;//发布时间12

	public String imgcode;//封面图片文件编码09
	
	public String channel;//新闻渠道


	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getImgcode() {
		return imgcode;
	}

	public void setImgcode(String imgcode) {
		this.imgcode = imgcode;
	}


	
	

}