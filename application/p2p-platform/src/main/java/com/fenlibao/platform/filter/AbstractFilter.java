package com.fenlibao.platform.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fenlibao.platform.common.json.Jackson;
import com.fenlibao.platform.common.servlet.ResponseUtil;
import com.fenlibao.platform.model.Response;

public abstract class AbstractFilter {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String THIRD_PARTY_CONTEXT = "/platform/tp"; //该URI不进行IP、签名校验！！
	
	/**
	 * 第三方数据获取不做ip过滤、签名校验
	 * (网贷之家、多赚、融360、网贷天眼等)
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @throws IOException 
	 */
	protected boolean validateThirdParty(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean isThirdParty = false;
		String uri = request.getRequestURI();
		if (StringUtils.isNotBlank(uri)) {
			if (uri.startsWith(THIRD_PARTY_CONTEXT)) {
				chain.doFilter(request, response);
				isThirdParty = true;
			}
		}
		return isThirdParty;
	}
	
	
	protected final void writeResponse(Response resp, HttpServletResponse response) {
		Map<String, Object> messages = new LinkedHashMap<>();
		messages.put(Response.CODE_KEY, resp.getCode());
		messages.put(Response.MESSAGE_KEY, resp.getMessage());
		ResponseUtil.response(Jackson.getBaseJsonData(messages), response);
	}
}
