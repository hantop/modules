package com.fenlibao.common.pms.util.context;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

/**
 * Created by Lullaby on 2015/7/28.
 */
public class ShiroSessionUtil {

    /**
     * 获取shiro当前用户
     * @return
     */
    public static Subject getCurrentSession() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return subject;
        } else {
            throw new AuthenticationException();
        }
    }

}
