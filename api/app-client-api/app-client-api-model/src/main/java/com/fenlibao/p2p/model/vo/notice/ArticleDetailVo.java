package com.fenlibao.p2p.model.vo.notice;

import java.util.Date;
/** 
 * @author: junda.feng
 */
public class ArticleDetailVo {

	public Integer articleId;
	
	public String articleTitle;//文章标题f06
	
	public String source;//来源07
	
	public String introduction;//文章摘要08
	
	public Long publishTime;//发布时间12
	
	public String imgUrl;//封面图片文件编码09
	
	public String content; //内容
	
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
	public Long getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}


}