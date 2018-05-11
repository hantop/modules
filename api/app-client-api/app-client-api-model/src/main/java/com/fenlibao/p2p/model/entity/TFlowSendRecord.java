package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送流量记录
 * @author Administrator
 *
 */
public class TFlowSendRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int userId;
	
	private String phone;
	
	private String activityType;
	
	private int flowSize;
	
	private Date sendTime;
	
	private Date createTime;
	
	private int sendStatus;
	
	private String requestNo;
	
	private String remark;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public int getFlowSize() {
		return flowSize;
	}

	public void setFlowSize(int flowSize) {
		this.flowSize = flowSize;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
