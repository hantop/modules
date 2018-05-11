package com.fenlibao.pms.launch.filter;

import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.model.pms.common.global.HttpCode;
import com.fenlibao.model.pms.common.global.HttpMessage;
import com.fenlibao.model.pms.common.global.HttpResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjaxFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		boolean isAjax = RequestUtil.isAjaxRequest(request);
		if (isAjax) {
			Pattern pattern = Pattern.compile("login");
			Matcher matcher = pattern.matcher(request.getRequestURI());
			if (!matcher.find()) {
				Subject currentUser = SecurityUtils.getSubject();
				if (!currentUser.isAuthenticated()) {
					HttpResponse resp = new HttpResponse();
					resp.setCodeMessage(HttpCode.SESSION_TIMEOUT, HttpMessage.SESSION_TIMEOUT);
					ResponseUtil.response(Jackson.getBaseJsonData(resp), response);
					return;
				}
			}
		}
		chain.doFilter(servletRequest, servletResponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
