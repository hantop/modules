package com.fenlibao.platform.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fenlibao.platform.common.json.Jackson;
import com.fenlibao.platform.common.servlet.ResponseUtil;
import com.fenlibao.platform.common.util.IPUtil;
import com.fenlibao.platform.model.Response;
import com.google.inject.Singleton;

/**
 * 对common的资源只有fenlibao商户才有权限访问
 * @author yangzengcai
 * @date 2016年3月9日
 */
@Singleton
public class ResourceFilter implements Filter {
	
	protected static final Logger logger = LoggerFactory.getLogger(ResourceFilter.class);

	private static final String FLB = "fenlibao"; //pf.pf_merchant_access_config.app_id
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String appid = req.getParameter("appid");
		if (FLB.equals(appid)) {
			chain.doFilter(req, resp);
		} else {
			Map<String, String> params = new LinkedHashMap<>();
			params.put("code", Response.SYSTEM_REQUEST_FORBIDDEN.getCode().toString());
			params.put("message", Response.SYSTEM_REQUEST_FORBIDDEN.getMessage());
			String data = Jackson.getBaseJsonData(params);
			ResponseUtil.response(data, resp);
			String ip = IPUtil.getIPAddress(req);
			logger.warn(">>>>>> ip=[{}],appid=[{}]的商户尝试访问common资源。。。。", ip, appid);
		}
	}

	@Override
	public void destroy() {
		
	}

}
