package com.fenlibao.pms.shiro.web;

import com.alibaba.fastjson.JSON;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import com.fenlibao.common.pms.util.redis.RedisPrefix;
import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.p2p.common.util.http.CookieUtil;
import com.fenlibao.service.pms.idmt.permit.PmsPermitService;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

/**
 * 自定义表单登陆
 * Created by Bogle on 2016/1/22.
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    private UserDetailsService pmsUserService;

    private PmsPermitService pmsPermitService;

    private static final Logger log = LoggerFactory.getLogger(CustomFormAuthenticationFilter.class);

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    public String getCaptchaParam() {

        return captchaParam;

    }

    protected String getCaptcha(ServletRequest request) {

        return WebUtils.getCleanParam(request, getCaptchaParam());

    }

    protected AuthenticationToken createToken(

            ServletRequest request, ServletResponse response) {

        String username = getUsername(request);

        String password = getPassword(request);

        String captcha = getCaptcha(request);

        boolean rememberMe = isRememberMe(request);

        String host = getHost(request);

        return new UsernamePasswordCaptchaToken(username,
                password.toCharArray(), rememberMe, host, captcha);

    }


    /**
     * 登陆成功代码;
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {// 不是ajax请求
            return super.onLoginSuccess(token, subject, request, response);
        } else {
            httpServletResponse.setCharacterEncoding("UTF-8");
            String username = subject.getPrincipal().toString();
            PmsUser user = pmsUserService.findByUsername(username);
            List<PermitVO> menus = pmsPermitService.getPermitsByUser(user.getId(), "topmenu");
            PrintWriter out = httpServletResponse.getWriter();
            if (menus != null && !menus.isEmpty()) {
                String url = menus.get(0).getPermitUrl();
                out.write(JSON.toJSONString(new Result(200, true, "登入成功", url.substring(1))));
            } else {
                out.write(JSON.toJSONString(new Result(200, true, "登入成功", "")));
            }
            out.flush();
            out.close();
        }
        return false;
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {// 不是ajax请求
            return super.onLoginFailure(token, e, request, response);
        }
        try {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            Class message = e.getClass();
            String username = token.getPrincipal().toString();
            PmsUser user = pmsUserService.findByUsername(username);
            int count = user != null ? 5 - user.getErrorNumber() : -1;
           //密码错误了3次或以上，会记录到redis
            String cookie = CookieUtil.getCookieByName((HttpServletRequest) request, Config.get("cookie.session.id"));
            String key="errorNum".concat(cookie);
            String error=null;
            if (IncorrectCredentialsException.class == message) {
                if (count < 0) {
                    out.write(JSON.toJSONString(new Result(401, false, "用户名或密码错误", null)));
                } else if (count > 0) {
                    if(count<3){
                        try (Jedis jedis = RedisFactory.getResource()) {
                                jedis.set(key, "1");
                                jedis.expire(key, Integer.valueOf(Config.get("error.time")));
                                error="error";

                        }

                    }

                        out.write(JSON.toJSONString(new Result(401, false, "用户名或密码错误,当前还剩下" + count + "次机会", error)));

                }
                else {
                    out.write(JSON.toJSONString(new Result(401, false, "用户名或密码错误,您的账号将被锁定", null)));
                }
            } else if (UnknownAccountException.class == message) {
                out.write(JSON.toJSONString(new Result(401, false, e.getMessage(), null)));
            } else if (LockedAccountException.class == message) {
                out.write(JSON.toJSONString(new Result(401, false, e.getMessage(), null)));
            } else if (DisabledAccountException.class == message) {
                out.write(JSON.toJSONString(new Result(401, false, e.getMessage(), null)));
            }else if(CaptchaNameException.class==message){
                out.write(JSON.toJSONString(new Result(401, false, "验证码错误", null)));
            }
            else {
                out.write(JSON.toJSONString(new Result(401, false, "未知错误", null)));
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    private static class Result implements Serializable {
        private boolean success;
        private String message;
        private int code;
        private String url;

        public Result(int code, boolean success, String message, String url) {
            this.success = success;
            this.message = message;
            this.code = code;
            this.url = url;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public void setPmsPermitService(PmsPermitService pmsPermitService) {
        this.pmsPermitService = pmsPermitService;
    }

    public void setPmsUserService(UserDetailsService pmsUserService) {
        this.pmsUserService = pmsUserService;
    }
}
