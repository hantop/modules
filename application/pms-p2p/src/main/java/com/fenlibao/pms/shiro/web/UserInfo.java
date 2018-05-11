package com.fenlibao.pms.shiro.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by Bogle on 2016/2/2.
 */
public class UserInfo {

    public static String getUsername() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        return principal.toString();
    }
}
