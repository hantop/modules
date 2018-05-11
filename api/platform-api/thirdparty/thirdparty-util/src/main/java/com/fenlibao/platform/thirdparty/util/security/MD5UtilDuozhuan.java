package com.fenlibao.platform.thirdparty.util.security;

import com.fenlibao.platform.common.config.Config;
import org.aeonbits.owner.ConfigFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 * 多赚数据获取签名校验
 * Created by zcai on 2016/5/31.
 */
public class MD5UtilDuozhuan {

    private static Config config = ConfigFactory.create(Config.class);

    public static String getSignature(int page, int pagesize) throws IOException {
        StringBuffer basestring = new StringBuffer();
        basestring.append(page).append(pagesize).append(config.getDuozhuanMD5key());
        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }

        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    /**
     * 校验token
     * @param page
     * @param pagesize
     * @param token
     * @return
     * @throws IOException
     */
    public static boolean validate(int page, int pagesize, String token) throws IOException {
        String mySign = getSignature(page,pagesize);
        if (mySign.equals(token)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String sign = MD5UtilDuozhuan.getSignature(1,20);
        System.out.println(sign);
    }

}
