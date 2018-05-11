package com.fenlibao.p2p.controller.global;

import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

	private static final Logger logger = LogManager.getLogger(ExceptionController.class.getName());

	@ExceptionHandler(APIException.class)
	public @ResponseBody HttpResponse handleAPIException(APIException ex){
		logger.info("================API业务返回码："+ex.getCode());
		logger.info("================API业务返回信息："+ex.getMessage());
		HttpResponse response = new HttpResponse();
		response.setCodeMessage(ex.getCode(), ex.getMessage());
		return response;
	}

	@ExceptionHandler(BusinessException.class)
	public @ResponseBody HttpResponse handleBusinessException(HttpServletRequest request, BusinessException ex){
	    logger.info("================接口业务返回码："+ex.getCode());
	    logger.info("================接口业务返回信息："+ex.getMessage());
		HttpResponse response = new HttpResponse();
	    response.setCodeMessage(ex.getCode(), ex.getMessage());
	    return response;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public @ResponseBody HttpResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		HttpResponse response = new HttpResponse();
		response.setCodeMessage(ResponseCode.OTHER_REQUEST_METHOD_NOT_SUPPORTED);
		return response;
	}
	
	@ExceptionHandler(Throwable.class)
	public @ResponseBody HttpResponse handleServerException(HttpServletRequest request, Throwable ex){
		logger.error("[接口异常返回信息]:"+ex.getMessage(), ex);
		HttpResponse response = new HttpResponse();
	    response.setCodeMessage(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage());
	    return response;
	}
}