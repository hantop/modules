package com.fenlibao.p2p.model.form.user;

public class Auth {
	
	/**
	 * 认证状态：通过
	 */
	public static final String AUTH_STATUS_TG = "TG";
	/**
	 * 设置状态：已设置
	 */
	public static final String SETTINGS_STATUS_YSZ = "YSZ";
	
	/**
	 * 手机是否认证
	 */
	public boolean PHONE = false;
	/**
	 * 是否实名认证
	 */
	public boolean IDENTITY = false;
	/**
	 * 是否设置交易密码
	 */
	public boolean TRADE_PWD = false;
	
	private String phoneStatus;
	private String identityStatus;
	private String tradePwdStatus;
	public String getPhoneStatus() {
		return phoneStatus;
	}
	public void setPhoneStatus(String phoneStatus) {
		this.phoneStatus = phoneStatus;
		this.PHONE = AUTH_STATUS_TG.equals(this.phoneStatus);
	}
	public String getIdentityStatus() {
		return identityStatus;
	}
	public void setIdentityStatus(String identityStatus) {
		this.identityStatus = identityStatus;
		this.IDENTITY = AUTH_STATUS_TG.equals(this.identityStatus);
		
	}
	public String getTradePwdStatus() {
		return tradePwdStatus;
	}
	public void setTradePwdStatus(String tradePwdStatus) {
		this.tradePwdStatus = tradePwdStatus;
		this.TRADE_PWD = SETTINGS_STATUS_YSZ.equals(this.tradePwdStatus);
	}
	
}
