package com.fenlibao.thirdparty.saiman.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.fenlibao.thirdparty.saiman.config.SmDemoConfig;

/**
 * 有盾报文签名生成工具类.<br>
 * <ul>
 * 提供两种签名方法:
 * </ul>
 * <li>1.对于<code>packageStr字段使用<strong>SHA256</strong>方式进行Hash加密后返回拼接sign字段的packageStr字符串</code></li>
 * <br>
 * <li>2.对于整个Json报文使用<strong>HMAC_SHA1</strong>方式进行报名签名</li>
 *
 * @author hush@udcredit.com
 * @version 1.0
 * @date 10/29/15
 */
public class SmSignatureHelper {

    /**
     * 对调用规则字段使用<strong>SHA256</strong>算法进行Hash加密.
     *
     * @param params 调用接口字段列表
     * @return 拼接后的packageStr字段
     */
    public static String generateSignedStr(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            try {
                sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8").toUpperCase())
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String content = sb.toString();
        if (content.endsWith("&")) {
            content = content.substring(0, content.length() - 1);
        }
        String sign = DigestUtils.sha256Hex(content).toLowerCase();
        if (content.length() > 0) {
            content += "&";
        }
        content = content + "sign=" + sign;
        return content;
    }


    public static String generateSignedBs(Map<String, String> params) {
        String sign = "";
        StringBuffer sb = new StringBuffer();
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            try {
                sb.append(key).append("=")
                        .append(params.get(key))
                        .append("&");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String content = sb.toString();
        if (content.endsWith("&")) {
            content = content.substring(0, content.length() - 1);
        }
        if(!StringUtils.isEmpty(content)){

            String signKey = content + SmDemoConfig.SECRET_KEY;
            sign = DigestUtils.md5Hex(signKey);
        }

        return sign;
    }

    public static String getBsParam(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            try {
                sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8").toUpperCase())
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String content = sb.toString();
        if (content.endsWith("&")) {
            content = content.substring(0, content.length() - 1);
        }

        return content;
    }
}
