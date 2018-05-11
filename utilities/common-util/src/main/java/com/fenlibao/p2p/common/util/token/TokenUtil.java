package com.fenlibao.p2p.common.util.token;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Token工具类
 *
 * @author chenzhixuan
 */
public class TokenUtil {

    /**
     * 生成32位的token，字母加数字
     */
    public static String createToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * token是否超时
     *
     * @param token
     * @return
     */
    public static boolean checkIsValid(Timestamp token) {
        boolean result;
        if (token == null) {
            result = false;
        } else {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (currentTime.before(token)) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 延长token时间
     *
     * @param prolongMillis 延长时间(毫秒)
     * @return
     */
    public static Timestamp prolongToken(int prolongMillis) {
        return new Timestamp(System.currentTimeMillis() + prolongMillis);
    }

    /**
     * 判断token是否过期(过期返回true)
     * @param date
     * @return
     */
    public static boolean isTokenTimeout(Date date) {
        boolean flag = true;
        if (date != null) {
            long now = System.currentTimeMillis();
            long close = date.getTime();
            if (now < close) {
                flag = false;
            }
        }
        return flag;
    }

}
