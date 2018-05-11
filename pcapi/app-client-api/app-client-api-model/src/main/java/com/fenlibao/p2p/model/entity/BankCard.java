package com.fenlibao.p2p.model.entity;

import java.io.Serializable;

/**
 * 银行卡信息(T6114)
 */
public class BankCard implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bankCardId;// 银行卡记录ID
	private String bankName;// 银行名称
	private String bankNum;// 银行卡号
	private String bankNumEncrypt;// 银行卡号，加密存储
	private String bankCode;// 银行编码
	private int districtId;// 开户行所在地区域ID
	private String brabankName;// 开户支行名称
	private String bankAuthStatus;// 银行卡认证状态

	public String getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNum() {
		return bankNum;
	}

	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}

	public String getBankNumEncrypt() {
		return bankNumEncrypt;
	}

	public void setBankNumEncrypt(String bankNumEncrypt) {
		this.bankNumEncrypt = bankNumEncrypt;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public String getBrabankName() {
		return brabankName;
	}

	public void setBrabankName(String brabankName) {
		this.brabankName = brabankName;
	}

	public String getBankAuthStatus() {
		return bankAuthStatus;
	}

	public void setBankAuthStatus(String bankAuthStatus) {
		this.bankAuthStatus = bankAuthStatus;
	}
}
