package com.fenlibao.p2p.launch.interceptor;

import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.util.loader.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private UserTokenService userTokenService;

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object object, Exception exception)
            throws Exception {
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView view)
            throws Exception {
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        boolean result = true;
        String token = request.getParameter("token");
        String userId = request.getParameter("userId");
        String clientType = request.getParameter("clientType");
        if (StringUtils.isBlank(token) ||
                StringUtils.isBlank(userId) ||
                StringUtils.isBlank(clientType) ||
                userTokenService.isInvalidToken(token, userId, clientType)) {
            HttpResponse resp = new HttpResponse();
            resp.setCodeMessage(ResponseCode.COMMON_NOT_VALID_TOKEN);
            ResponseUtil.response(Jackson.getBaseJsonData(resp), response);
            result = false;
        }
        return result;
    }

}
