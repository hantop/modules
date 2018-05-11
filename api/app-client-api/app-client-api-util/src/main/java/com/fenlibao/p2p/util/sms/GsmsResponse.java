package com.fenlibao.p2p.util.sms;

import java.util.UUID;

/**
 * 返回结果
 */
public class GsmsResponse {

	private UUID uuid;//响应信息唯一序号
	
	private String message;//提示消息
	
	private int result;//结果代码
	
	private String attrbutes;//其他属性信息

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getAttrbutes() {
		return attrbutes;
	}

	public void setAttrbutes(String attrbutes) {
		this.attrbutes = attrbutes;
	}
	
}
