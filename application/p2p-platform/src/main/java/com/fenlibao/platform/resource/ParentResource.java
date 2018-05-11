package com.fenlibao.platform.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fenlibao.platform.common.exception.CustomException;
import com.fenlibao.platform.common.json.Jackson;
import com.fenlibao.platform.model.Response;

/**
 * 所有resource将继承这个
 * @author yangzengcai
 * @date 2016年2月18日
 */
public abstract class ParentResource {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 获取成功的响应信息
	 * @return Map
	 */
	protected final Map<String, Object> success() {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(Response.CODE_KEY, Response.RESPONSE_SUCCESS.getCode());
		response.put(Response.MESSAGE_KEY, Response.RESPONSE_SUCCESS.getMessage());
		return response;
	}
	
	/**
	 * 获取失败的响应信息
	 * @return Map
	 */
	protected final Map<String, Object> failure() {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(Response.CODE_KEY, Response.RESPONSE_FAILURE.getCode());
		response.put(Response.MESSAGE_KEY, Response.RESPONSE_FAILURE.getMessage());
		return response;
	}
	
	protected final Map<String, Object> failure(String code, String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(Response.CODE_KEY, code);
		response.put(Response.MESSAGE_KEY, message);
		return response;
	}
	
	protected final Map<String, Object> failure(Response resp) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(Response.CODE_KEY, resp.getCode());
		response.put(Response.MESSAGE_KEY, resp.getMessage());
		return response;
	}
	
	protected final Map<String, Object> failure(CustomException e) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(Response.CODE_KEY, e.getCode());
		response.put(Response.MESSAGE_KEY, e.getMessage());
		return response;
	}
	
	/**
	 * 将响应的信息转json返回
	 * @param params
	 * @return
	 */
	protected final String jackson(Map<String, Object> params) {
		return Jackson.getBaseJsonData(params);
	}
	
}
