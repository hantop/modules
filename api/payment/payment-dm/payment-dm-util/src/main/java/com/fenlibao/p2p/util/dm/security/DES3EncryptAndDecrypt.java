package com.fenlibao.p2p.util.dm.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.fenlibao.p2p.model.dm.config.HXConfig;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;
import org.apache.commons.codec.binary.Base64;

public class DES3EncryptAndDecrypt {

    private static HXConfig config;

    static {
        config = ConfigCache.getOrCreate(HXConfig.class);
    }

    private static String ALGORITHM="DESede";

    //3des应用加密密码（由华兴银行统一分配)
    private static String Des3Key=config.des3Key();

    public static String  encrypt(String src) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        DESedeKeySpec desKeySpec = new DESedeKeySpec(Des3Key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return new String(Base64.encodeBase64(cipher.doFinal(src.getBytes("UTF-8"))));
    }


    public static String  decrypt(String src) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        DESedeKeySpec desKeySpec = new DESedeKeySpec(Des3Key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte [] dd=cipher.doFinal(Base64.decodeBase64(src.getBytes("UTF-8")));
        return new String(dd,"UTF-8");
    }

    /** 余额查询**/
    public static String ZTSA54Q67() throws Exception{
        String TDATA=""
                +"<TDATA>"
                +"<MERCHANTID>P2P001</MERCHANTID>"
                +"<MERCHANTNAME>P2P测试</MERCHANTNAME>"
                +"<ACNO>6236882299000000435</ACNO>"
                +"<ACNAME>PTP测试7</ACNAME>"
                +"</TDATA>";
        String XMLPARA=encrypt(TDATA);
        return XMLPARA;
    }

    public static void main(String args[]) throws Exception {
        String encrypt=ZTSA54Q67();

        System.out.println("加密后的内容：     "+encrypt);
        System.out.println("解密后的内容：     "+decrypt(encrypt));
    }
}
