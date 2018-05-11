package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 账户绑定状态 TG:通过 BTG:不通过
 */
public class AccountCheckStatusVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bPhone;// 手机绑定状态
	private String bIdentitycard;// 身份证绑定状态
	private String bMail;// 邮箱绑定状态
	private String bWX;// 微信绑定状态
	private String bQQ;// QQ绑定状态

	public String getbPhone() {
		return bPhone;
	}

	public void setbPhone(String bPhone) {
		this.bPhone = bPhone;
	}

	public String getbIdentitycard() {
		return bIdentitycard;
	}

	public void setbIdentitycard(String bIdentitycard) {
		this.bIdentitycard = bIdentitycard;
	}

	public String getbMail() {
		return bMail;
	}

	public void setbMail(String bMail) {
		this.bMail = bMail;
	}

	public String getbWX() {
		return bWX;
	}

	public void setbWX(String bWX) {
		this.bWX = bWX;
	}

	public String getbQQ() {
		return bQQ;
	}

	public void setbQQ(String bQQ) {
		this.bQQ = bQQ;
	}

}
