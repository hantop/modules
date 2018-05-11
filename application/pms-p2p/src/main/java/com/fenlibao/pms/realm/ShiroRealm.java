package com.fenlibao.pms.realm;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import com.fenlibao.common.pms.util.redis.RedisPrefix;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.p2p.common.util.http.CookieUtil;
import com.fenlibao.pms.shiro.web.CaptchaNameException;
import com.fenlibao.pms.shiro.web.UsernamePasswordCaptchaToken;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.subject.WebSubject;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class ShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LogManager.getLogger(ShiroRealm.class.getName());

    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("[ShiroRealm.doGetAuthorizationInfo]");
        String username = (String) principals.getPrimaryPrincipal();
        PmsUser user = userDetailsService.findByUsername(username);
        SimpleAuthorizationInfo author = new SimpleAuthorizationInfo();
        if (user != null) {
            author.setRoles(userDetailsService.getUserRoles(user.getId()));
            author.setStringPermissions(userDetailsService.getUserPermissions(user.getId()));
        }
        return author;
    }

    /**
     * 认证回调函数, 登录时调用
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authentoken)
            throws AuthenticationException {
        logger.info("[ShiroRealm.doGetAuthenticationInfo]");
        //UsernamePasswordToken token = (UsernamePasswordToken) authentoken;
        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authentoken;
        String username = token.getUsername();
        PmsUser user = userDetailsService.findByUsername(username);
        ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();

        //验证码检测
        String captcha = token.getCaptcha();
        String exitCode = "";
        String cookie = CookieUtil.getCookieByName((HttpServletRequest)request, Config.get("cookie.session.id"));
        String error = null;
        try (Jedis jedis = RedisFactory.getResource()) {
            if (StringUtils.isNotBlank(cookie)) {
                String key = RedisPrefix.LOGIN_CAPTCHA.getPrefix().concat(cookie);
                exitCode= jedis.get(key);
                error= jedis.get("errorNum".concat(cookie));
            }
        }
        if(user.getErrorNumber()>=3&&error!=null) {
            if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
                throw new CaptchaNameException("验证码错误");
            }
        }

        if (user == null) {
            throw new UnknownAccountException("账号不存在");//没找到帐号
        }
        if (Boolean.TRUE.equals(user.isLock())) {
            throw new LockedAccountException("账号被锁定，请联系管理员"); //帐号锁定
        }
        if(user.getStatus() == 0) {
            throw new DisabledAccountException("账号被禁用，请联系管理员"); //帐号锁定
        }




        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
