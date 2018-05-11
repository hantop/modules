package com.fenlibao.p2p.model.payment.tp.common.entity;

public class PayExtend {
	/**
	 * 用户id
	 */
	private int userId;
	/**
	 * 连连协议号
	 */
	private String lianlianAgreement;
	/**
	 * 宝付协议号
	 */
	private String baofooBindId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLianlianAgreement() {
		return lianlianAgreement;
	}

	public void setLianlianAgreement(String lianlianAgreement) {
		this.lianlianAgreement = lianlianAgreement;
	}

	public String getBaofooBindId() {
		return baofooBindId;
	}

	public void setBaofooBindId(String baofooBindId) {
		this.baofooBindId = baofooBindId;
	}

}
