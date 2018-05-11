package com.fenlibao.platform.thirdparty.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 按第三方要求对响应简单封装
 * @date 2016年5月28日
 */
public class TPResponseUtil {
	
	private static final String RESULT_KEY = "result";
	private static final String MESSAGE_KEY = "message";
	
	private static final String RESULT_SUCCESS = "1";
	private static final String RESULT_FAILURE = "-1";
	
	private static final String MESSAGE_SUCCESS = "请求成功";
	private static final String MESSAGE_FAILURE = "请求失败";

	public static Map<String, Object> success() {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(RESULT_KEY, RESULT_SUCCESS);
		response.put(MESSAGE_KEY, MESSAGE_SUCCESS);
		return response;
	}
	
	public static Map<String, Object> failure() {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(RESULT_KEY, RESULT_FAILURE);
		response.put(MESSAGE_KEY, MESSAGE_FAILURE);
		return response;
	}
	
}
