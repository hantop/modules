package com.fenlibao.p2p.model.mp.vo.topup;

import java.io.Serializable;

/**
 * 调用第三方接口所需参数信息
 *
 */
public class InvokeBusinessVO extends MobileTopUpOrder implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 商户号
	 */
	private String agentcode;
	
	/**
	 * 加密KEY
	 */
	private String signKey;
	
	/**
	 * 回调地址
	 */
	private String callbackUrl;

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
}
