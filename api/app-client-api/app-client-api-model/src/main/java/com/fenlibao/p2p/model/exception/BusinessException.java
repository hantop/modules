package com.fenlibao.p2p.model.exception;

import com.fenlibao.p2p.model.global.ResponseCode;

/**
 * 业务异常
 * （添加对ResponseCode的处理）
 * @author zcai
 * @date 2016年4月21日
 */
public class BusinessException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 2332608236621015980L;

	private String code;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BusinessException(ResponseCode response) {
		super(response.getMessage());
		this.code = response.getCode();
	}
}
