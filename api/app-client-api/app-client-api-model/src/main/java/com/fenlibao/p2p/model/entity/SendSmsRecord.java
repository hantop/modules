package com.fenlibao.p2p.model.entity;

import java.util.Date;

/**
 *待发送短信表(s1._1040)
 */
public class SendSmsRecord {

	private int id;

    private int type;//发送类型
	
	private String content;//短信内容
	
	private Date createTime;//创建时间
	
	private String status;//发送状态,W:未发送，Z:正在发送, Y:已发送
	
	private Date outTime;//过期时间
	
	private Integer userId;//用户ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
