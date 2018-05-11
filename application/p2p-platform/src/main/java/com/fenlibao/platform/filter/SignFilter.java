package com.fenlibao.platform.filter;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fenlibao.platform.common.servlet.RequestUtil;
import com.fenlibao.platform.common.util.IPUtil;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.service.CommonService;

/**
 * 签名校验
 * @author yangzengcai
 * @date 2016年2月24日
 */
@Singleton
public class SignFilter extends AbstractFilter implements Filter {

	@Inject
	private CommonService commonService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("SignFilter initialized.");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (!validateThirdParty(request, response, chain)) {
			Map<String, String> params = RequestUtil.getAllParameters(request);
			String appid = "";
			try {
				appid = params.get("appid");
				if (commonService.verifySign(params)) { //commonService.verifySign(params)
					chain.doFilter(request, response);
				} else {
					signFailure(appid, response, request);
				}
			} catch (RuntimeException e) {
				logger.error(e.getMessage(), e);
				signFailure(appid, response, request);
				return;
			}
		}
	}

	@Override
	public void destroy() {
		logger.warn("SignFilter destroyed.");
	}
	
	private void signFailure(String appid, HttpServletResponse response, HttpServletRequest request) {
		String ip = IPUtil.getIPAddress(request);
		logger.warn("ip[{}], appid[{}] verify sign failure", ip, appid);
		writeResponse(Response.SYSTEM_SIGN_FAILRUE, response);
	}

}
