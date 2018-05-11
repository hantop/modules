package com.fenlibao.platform.common.exception;

import com.fenlibao.platform.model.Response;

/**
 * 业务逻辑异常
 * @author yangzengcai
 * @date 2016年2月19日
 */
public class BusinessException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6224057783197628756L;
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(Response response) {
		super(response);
	}
	
	public BusinessException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}
	
	public BusinessException(Integer code, String message) {
		super(code, message);
	}
	
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
