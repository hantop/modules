package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

public class PrivateMessageVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int messageId;//ID

	private String title;//标题
	
	private String content;//内容
	
	private long timestamp;//时间戳 
	
	private int status;//状态

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
