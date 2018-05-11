package com.fenlibao.platform.common.exception;

import com.fenlibao.platform.model.Response;

/**
 * 自定义异常父类
 * @author yangzengcai
 * @date 2016年3月14日
 */
public abstract class CustomException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Integer code = -1;

	public Integer getCode() {
		return code;
	}

	public CustomException() {
		super();
	}
	
	public CustomException(Response response) {
		super(response.getMessage());
		this.code = response.getCode();
	}

	public CustomException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public CustomException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomException(String message) {
		super(message);
	}

	public CustomException(Throwable cause) {
		super(cause);
	}

}
