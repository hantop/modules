package com.fenlibao.pms.shiro.web;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Administrator on 2016/4/15.
 */
public class CaptchaNameException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public CaptchaNameException() {

        super();

    }

    public CaptchaNameException(String message, Throwable cause) {

        super(message, cause);

    }

    public CaptchaNameException(String message) {

        super(message);

    }

    public CaptchaNameException(Throwable cause) {

        super(cause);

    }
}
