package com.fenlibao.p2p.common.util.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by Lullaby on 2015-11-05 11:57
 */
public class CookieUtil {

    /**
     * 通过request对象从cookie中获取客户端sessionId
     *
     * @param request
     * @return
     */
    public static String getSessionId(HttpServletRequest request) {
        String sessionId = "";
        Cookie[] cooks = request.getCookies();
        if(cooks!=null) {
            for (Cookie cook : cooks) {
                if ("JSESSIONID".equals(cook.getName())) {
                    sessionId = cook.getValue();
                    break;
                }
            }
            return sessionId;
        }
        return  null;
    }

    /**
     * 用UUID随机生成一个会话ID
     *
     * @return
     */
    public static String getRandomSessionId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 通过cookie name获取cookie value
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieByName(HttpServletRequest request, String cookieName) {
        String cookie_value = null;
        Cookie[] cooks = request.getCookies();
        if (cooks != null) {
            for (Cookie cook : cooks) {
                if (cookieName != null && cookieName.equals(cook.getName())) {
                    cookie_value = cook.getValue();
                    break;
                }
            }
        }
        return cookie_value;
    }

}
