package com.fenlibao.p2p.model.mp.entity;

public class UserAuthStatus {
	
	private String phoneStatus; //手机认证状态,TG:通过;BTG:不通过

	private String identityStatus; //身份认证状态,TG:通过;BTG:不通过

	private String tradePwdStatus; //交易密码,YSZ:已设置;WSZ:未设置;

	/**
	 * @return the phoneStatus
	 */
	public String getPhoneStatus() {
		return phoneStatus;
	}

	/**
	 * @param phoneStatus the phoneStatus to set
	 */
	public void setPhoneStatus(String phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

	/**
	 * @return the identityStatus
	 */
	public String getIdentityStatus() {
		return identityStatus;
	}

	/**
	 * @param identityStatus the identityStatus to set
	 */
	public void setIdentityStatus(String identityStatus) {
		this.identityStatus = identityStatus;
	}

	/**
	 * @return the tradePwdStatus
	 */
	public String getTradePwdStatus() {
		return tradePwdStatus;
	}

	/**
	 * @param tradePwdStatus the tradePwdStatus to set
	 */
	public void setTradePwdStatus(String tradePwdStatus) {
		this.tradePwdStatus = tradePwdStatus;
	}
	
}
