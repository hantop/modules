package com.fenlibao.platform.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 * aes for third party
 * 用于与第三方数据传输加密
 * 每个商户的密钥都不一样
 * </pre>
 * Created by zcai on 2016/10/24.
 */
public class AESForTP {

    /**
     * 加密
     * @param secret 分配给商户的密钥
     * @param content
     * @return
     * @throws Throwable
     */
    public static String encode(String secret, String content) throws Exception {
        if (StringUtils.isBlank(secret)) {
            throw new RuntimeException("secret is blank");
        }
        if(StringUtils.isBlank(content)) {
            return content;
        } else {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
            cipher.init(1, keySpec);
            byte[] ciphertext = cipher.doFinal(content.getBytes());
            return Base64.encodeBase64String(ciphertext);
        }
    }

    /**
     * 解密
     * @param secret 分配给商户的密钥
     * @param content
     * @return
     * @throws Throwable
     */
    public static String decode(String secret, String content) throws Exception {
        if (StringUtils.isBlank(secret)) {
            throw new RuntimeException("secret is blank");
        }
        if(StringUtils.isBlank(content)) {
            return content;
        } else {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
            cipher.init(2, keySpec);
            byte[] ciphertext = cipher.doFinal(Base64.decodeBase64(content));
            return new String(ciphertext);
        }
    }

    public static void main(String[] args) throws Exception {
        String a = encode("1234567812345678", "qwert");
        String b = decode("1234567812345678", a);
        System.out.println(a);
        System.out.println(b);
        System.out.println(AES.getInstace().encrypt("1234567812345678"));
    }

}
