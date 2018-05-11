package com.fenlibao.pms.launch.interceptor;

import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.model.pms.common.global.Constant;
import com.fenlibao.model.pms.common.global.HttpCode;
import com.fenlibao.model.pms.common.global.HttpMessage;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object,
			ModelAndView exception) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
			throws Exception {
		boolean isAjax = RequestUtil.isAjaxRequest(request);
		PmsUser user = (PmsUser) request.getSession().getAttribute(Constant.SESSION_USERNAME);
		if (isAjax) {
			if (user != null) {
				return true;
			} else {
				HttpResponse resp = new HttpResponse();
				resp.setCodeMessage(HttpCode.SESSION_TIMEOUT, HttpMessage.SESSION_TIMEOUT);
				ResponseUtil.response(Jackson.getBaseJsonData(resp), response);
				return false;
			}
		} else {
			if (user != null) {
				return true;
			} else {
				response.sendRedirect("/" + Constant.APPLICATION_ROOT_URI + "/login");
				return false;
			}
		}
	}

}
