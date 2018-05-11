package com.fenlibao.p2p.model.user.vo;

import com.fenlibao.p2p.model.api.enums.GlobalStatus;

/**
 * 用户银行卡信息
 * Created by zcai on 2016/10/19.
 */
public class UserBankCardVO {

	private Integer cardId;
    private Integer userId;
    private String cardNo;//部分信号代替
    private String cardNoEncrypt;//加密
    private String QYStatus;//启用状态
    private String realNameAuthStatus;//实名认证状态
    private String acName;//开户名
    private Integer acType;//开户类型1：个人，2：公司
    private String authStatus;//认证状态
    private Integer bankId;//银行id
    private String bankCode;//银行卡编码
    private String bankName;//银行名称
    private String reservedPhone;//银行预留手机号

	public UserBankCardVO forUser(int userId, String cardNo, String cardNoEncrypt, String acName, Integer bankId,String reservedPhone) {
        this.userId = userId;
        this.cardNo = cardNo;
        this.cardNoEncrypt = cardNoEncrypt;
        this.acName = acName;
        this.QYStatus = GlobalStatus.QY.name();
        this.realNameAuthStatus = GlobalStatus.TG.name();
        this.acType = 1;
        this.bankId = bankId;
        this.reservedPhone= reservedPhone;
        return this;
    }

	public String getReservedPhone() {
		return reservedPhone;
	}

	public void setReservedPhone(String reservedPhone) {
		this.reservedPhone = reservedPhone;
	}

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNoEncrypt() {
        return cardNoEncrypt;
    }

    public void setCardNoEncrypt(String cardNoEncrypt) {
        this.cardNoEncrypt = cardNoEncrypt;
    }

    public String getQYStatus() {
        return QYStatus;
    }

    public void setQYStatus(String QYStatus) {
        this.QYStatus = QYStatus;
    }

    public String getRealNameAuthStatus() {
        return realNameAuthStatus;
    }

    public void setRealNameAuthStatus(String realNameAuthStatus) {
        this.realNameAuthStatus = realNameAuthStatus;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }
    
	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
