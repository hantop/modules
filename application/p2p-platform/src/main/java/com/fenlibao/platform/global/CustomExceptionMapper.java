package com.fenlibao.platform.global;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eclipse.jetty.http.HttpStatus;

import com.fenlibao.platform.common.exception.CustomException;
import com.fenlibao.platform.resource.ParentResource;

/**
 * 处理没有处理的异常
 * @author yangzengcai
 * @date 2016年3月12日
 */
@Provider
public class CustomExceptionMapper extends ParentResource implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		Map<String, Object> entity = failure();
		if (exception instanceof CustomException) {
			entity = failure((CustomException) exception);
		}
		logger.error(exception.getMessage(), exception);
		return Response
				.status(HttpStatus.OK_200)
				.entity(jackson(entity))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
}
