package com.fenlibao.lianpay.share.util;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.lianpay.share.config.SignType;
import com.fenlibao.lianpay.share.security.Md5Algorithm;
import com.fenlibao.lianpay.share.security.RSAUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TrustUtil {
    // 商户编号
    public static final String MERCH_ID    = "W20150821000000194";
    // 业务类型
    public static final String PRODUCT_ID    = "B10002";
    // 签名方式 RSA或MD5
    public static final String SIGN_TYPE      = "MD5";
    // MD5 加密签名串
    public static final String SIGN     = "385754d65f1a4320abb6f857b663a79d";
    // 接口版本号，固定1.0
    public static final String VERSION        = "1.0";

    /**
     * 生成待签名串
     * @param jsonObject
     * @return
     */
    public static String genSignData(JSONObject jsonObject){
        StringBuffer content = new StringBuffer();

        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);
            // sign 和ip_client 不参与签名
            if ("sign".equals(key))
            {
                continue;
            }
            String value = (String) jsonObject.getString(key);
            // 空串不参与签名
            if (FuncUtils.isNull(value))
            {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);

        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&"))
        {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }

    /**
     * 加签
     * @param sign_src
     * @param rsa_private
     * @param md5_key
     * @return
     */
    public static String addSign(String sign_src, String sign_type,String rsa_private, String md5_key) {
        if (sign_src == null || sign_type == null) {
            return "";
        }
        if (SignType.MD5.getSignTypeCode().equals(sign_type)) {
            return addSignMD5(sign_src, md5_key);
        } else {
            return addSignRSA(sign_src, rsa_private);
        }
    }

    /**
     * MD5加签名
     *
     * @param sign_src
     * @param md5_key
     * @return
     */
    public static String addSignMD5(String sign_src, String md5_key) {
        if (sign_src == null) {
            return "";
        }

        sign_src += "&key=" + md5_key;

        try {
            return Md5Algorithm.getInstance().md5Digest(
                    sign_src.getBytes("utf-8"));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * RSA加签名
     *
     * @param sign_src
     * @param rsa_private
     * @return
     */
    public static String addSignRSA(String sign_src, String rsa_private) {
        if (sign_src == null) {
            return "";
        }


        try {
            return RSAUtil.sign(rsa_private, sign_src);
        } catch (Exception e) {
            return "";
        }
    }
}
