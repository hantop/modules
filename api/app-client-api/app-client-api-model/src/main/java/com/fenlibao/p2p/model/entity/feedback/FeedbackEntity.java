package com.fenlibao.p2p.model.entity.feedback;

import java.io.Serializable;
import java.util.Date;

/**
 * 意见反馈实体
 * <p>table：feedback
 * @author yangzengcai
 * 2015年7月23日
 */
public class FeedbackEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7722282938675677409L;

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 用户ID
	 */
	private Integer userId;
	
	/**
	 * 创建时间
	 */
	private Date createTime = new Date();
	
	/**
	 * 反馈内容
	 */
	private String content;
	
	/**
	 * 联系方式
	 */
	private String contact;
	
	/**
	 * 版本号
	 */
	private String version;
	
	/**
	 * 设备唯一标识(UUID)
	 */
	private String deviceId;
	
	/**
	 * 客户端类型(0-未知客户端类型1-IPhone客户端2-Android客户端3-Ipad客户端4-WP客户端)
	 */
	private Integer clientType;
	
	/**
	 * 屏幕类型
	 */
	private Integer screenType;

	
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public Integer getScreenType() {
		return screenType;
	}

	public void setScreenType(Integer screenType) {
		this.screenType = screenType;
	}

	@Override
	public String toString() {
		return "FeedbackEntity [userId=" + userId + ", createTime="
				+ createTime + ", content=" + content + ", contact=" + contact
				+ ", version=" + version + ", deviceId=" + deviceId
				+ ", clientType=" + clientType + ", screenType=" + screenType
				+ "]";
	}

}
