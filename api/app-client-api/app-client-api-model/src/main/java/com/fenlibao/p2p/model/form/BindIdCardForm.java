package com.fenlibao.p2p.model.form;

import com.fenlibao.p2p.model.global.BaseRequestFormExtend;

/**
 * 绑定身份证
 * 
 * @author chenzhixuan
 *
 */
public class BindIdCardForm extends BaseRequestFormExtend {
	private String idCardFullName;
	private String idCardNum;

	public String getIdCardFullName() {
		return idCardFullName;
	}

	public void setIdCardFullName(String idCardFullName) {
		this.idCardFullName = idCardFullName;
	}

	public String getIdCardNum() {
		return idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

}
