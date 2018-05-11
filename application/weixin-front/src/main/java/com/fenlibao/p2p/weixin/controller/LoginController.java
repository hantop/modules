package com.fenlibao.p2p.weixin.controller;

import com.fenlibao.p2p.weixin.component.ConnectionSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/8/20.
 */
@RestController
@RequestMapping("/login")
public class LoginController extends WeixinSuport {


    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ConnectionSettings connectionSettings;


    @RequestMapping(value = "/open")
    public ModelAndView open(@RequestParam(value = "code", required = false) String code, @RequestParam("state") String state, HttpServletRequest request) {
        log.debug("开启免登陆");
        String url = connectionSettings.getPhpAutoLogin();
        return parse(code, request, url, state, true);
    }

    @RequestMapping(value = "/cancel")
    public ModelAndView cancel(@RequestParam(value = "code", required = false) String code, @RequestParam("state") String state, HttpServletRequest request) {
        log.debug("取消面登陆");
        String url = connectionSettings.getPhpCancelLogin();
        return parse(code, request, url, state, true);

    }


    @RequestMapping(value = "/auto")
    public ModelAndView autoLogin(@RequestParam(value = "code", required = false) String code, @RequestParam("state") String state, HttpServletRequest request) {
        log.info("进入首页");
        String url = this.connectionSettings.getPhpHome();
        return parse(code, request, url, state, true);
    }
}
