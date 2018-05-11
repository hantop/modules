package com.fenlibao.p2p.model.entity.notice;

import java.util.Date;

/**
 * @author: junda.feng
 */
public class KnowMore {
	
	public Integer id;

    public String title;//文章标题f06

    public String source;//来源07
    
    public String introduction;//文章摘要08

	public Date publishTime;//发布时间12

	public String imgcode;//封面图片文件编码09
	
	public String channel;//渠道

	public String content; //内容



	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}