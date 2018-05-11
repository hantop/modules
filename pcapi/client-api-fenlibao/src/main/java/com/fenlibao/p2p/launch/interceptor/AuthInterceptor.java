package com.fenlibao.p2p.launch.interceptor;

import com.fenlibao.p2p.common.util.encrypt.AESInstanceEnum;
import com.fenlibao.p2p.common.util.encrypt.Base64Decoder;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Lullaby on 2015/9/5.
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(AuthInterceptor.class.getName());

    private static final String API_SERVICE_USERNAME;
    private static final String API_SERVICE_PASSWORD;

    static {
        API_SERVICE_USERNAME = Config.get("api.service.username");
        API_SERVICE_PASSWORD = Config.get("api.service.password");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!checkIfNotAuthorized(request)) {
            returnErrorStatus(response);
            return false;
        }
        return true;
    }

    private void returnErrorStatus(HttpServletResponse response) {
        response.setStatus(401);
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("WWW-authenticate", "Basic Realm=\"Authorization\"");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            //out.append("{\"code\":\"401\",\"message\":\"Unauthorized\"}");
            out.append("{\"code\":"+ResponseCode.NOT_AUTHORIZED.getCode()+",\"message\":\""+ResponseCode.NOT_AUTHORIZED.getMessage()+"\"}");
        } catch (IOException e) {
            logger.error("[AuthInterceptor.returnErrorStatus]", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private boolean checkIfNotAuthorized(HttpServletRequest request) {
        String base64_str = request.getHeader(Config.get("header.authorization"));
        if (StringUtils.isNotEmpty(base64_str)) {
            try {
                String plain_str = AESInstanceEnum.INSTANCE.getInstance().decrypt(Base64Decoder.decode(base64_str));
                if (checkStrFormat(plain_str)) {
                    return false;
                }
                if (weatherAuthorizedUser(plain_str)) {
                    return false;
                }
            } catch (Exception e) {
                logger.error("[AuthInterceptor.checkIfNotAuthorized]", e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean checkStrFormat(String plain_str) throws Exception {
        return plain_str.length() == 0 || (!plain_str.contains(":"));
    }

    private boolean weatherAuthorizedUser(String plain_str) throws Exception {
        String[] arr = plain_str.split(":");
        if (arr.length < 2) {
            return false;
        }
        String username_from_client = arr[0];
        String password_from_client = arr[1];
        return !(API_SERVICE_USERNAME.equals(username_from_client) && API_SERVICE_PASSWORD.equals(password_from_client));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
