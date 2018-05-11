package com.fenlibao.platform.model.vo;

import java.util.Date;

import com.fenlibao.platform.common.enums.SystemBoolean;

/**
 * 商户信息配置
 * @author yangzengcai
 * @date 2016年3月8日
 */
public class MerchantConfigVO {

	private Integer id;
	private Integer merchantId; //商户ID
	private String appid;
	private String secret;
	private Date createTime;
	private Integer status = SystemBoolean.FALSE.getCode();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
