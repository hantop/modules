package com.fenlibao.platform.model.thirdparty.entity;

import java.util.Date;

/**
 * 第三方用户实体
 * <p>用于第三方用户登录获取平台指定的数据
 * @author zcai
 * @date 2016年5月28日
 */
public class TPUserEntity {

	private Integer id;
	private Date createTime;
	private String username;
	private String password;
	private Integer status;
	private String resourceURI;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getResourceURI() {
		return resourceURI;
	}

	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}
}
