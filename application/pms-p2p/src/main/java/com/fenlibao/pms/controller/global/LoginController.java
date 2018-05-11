package com.fenlibao.pms.controller.global;

import com.fenlibao.common.pms.util.constant.RedisConst;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import com.fenlibao.common.pms.util.redis.RedisPrefix;
import com.fenlibao.p2p.common.util.http.CookieUtil;
import com.fenlibao.service.pms.idmt.user.UserDetailsService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping
public class LoginController extends KaptchaServlet {

    private static final Logger logger = LogManager.getLogger(LoginController.class.getName());

    private Producer captchaProducer;

    @Resource
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @Resource
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/login")
    public ModelAndView showLoginForm(HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        String cookie = CookieUtil.getCookieByName(req, Config.get("cookie.session.id"));
        String errorNum="";
        if (subject.isAuthenticated()) {
            return new ModelAndView("redirect:index");
        } else {
        if (StringUtils.isNotBlank(cookie)) {

            try (Jedis jedis = RedisFactory.getResource()) {
                errorNum = jedis.get("errorNum".concat(cookie));
            }
        }
            return new ModelAndView("login").addObject("errorNum",errorNum);
        }
    }



    @RequestMapping(value = "captcha", method = RequestMethod.GET)
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();
        logger.info("验证码：" + capText);

        // set captcha to redis
        try (Jedis jedis = RedisFactory.getResource()) {
            String cookie = CookieUtil.getCookieByName(request, Config.get("cookie.session.id"));
            if (StringUtils.isNotBlank(cookie)) {
                String key = RedisPrefix.LOGIN_CAPTCHA.getPrefix().concat(cookie);
                jedis.set(key, capText);
                jedis.expire(key, Integer.valueOf(Config.get("kaptcha.timeout")));
            } else {
                return;
            }
        }

        // store the text in the session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);

        ServletOutputStream out = response.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public void logout(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.del(RedisConst.$SESSION_PREFIX + currentUser.getSession().getId());
        }
        response.sendRedirect("login");
    }

}
