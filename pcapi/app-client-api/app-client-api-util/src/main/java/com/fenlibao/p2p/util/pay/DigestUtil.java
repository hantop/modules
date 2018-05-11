package com.fenlibao.p2p.util.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.util.loader.Payment;

/**
 * 连连代付公共处理类
 * @author yangzengcai
 * @date 2015年8月29日
 */
public class DigestUtil {

	protected static final Logger logger = LogManager.getLogger(DigestUtil.class);
	
    // 签名字段
    protected static final String REQUEST_SIGN_NAME = "sign";
    
    /**
     * 系统时间
     * @param format 时间格式
     * @return String 返回时间
     */
    public static String sysTime(String format){
        return new SimpleDateFormat(format).format( new Date() );
    }
    
    
    /**
     * 加密
     * @param param 加密参数
     * @return String 加密字符串
     * @throws Throwable
     */
    public static String createSendSign(Map<String, String> param)throws Throwable {
        
        String result = null;
        // 拼接
        String sb = getSignStr(param);
        // 加密方式
        String sign_type = param.get("sign_type");
        // 密钥
        String key = getKey(sign_type, true);
        logger.info("sign_type is:" + sign_type + ";  sign string is:" + sb);
       // 加密
        if ("MD5".equals(sign_type)) {
            // Md5
            sb += "&key=" + key;
            result = MD5Util.getMD5ofStr(sb).toLowerCase();
        } else {
            // rsa
            result = RSAUtil.sign(key, sb);
        }
        return result;
    }
    


    /**
     * 集合排序aA-zZ
     * <空串不参加>
     * @param param 集合参数
     * @return String 返回
     */
    private static String getSignStr(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>(param.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            String value = param.get(key);
            // 空串不参与签名
            if (isnull(value)) {
                continue;
            }
            sb.append((i == 0 ? "" : "&") + key + "=" + value); //这里的value不能trim()
        }
        String signSrc = sb.toString();
        if (signSrc.startsWith("&")) {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }
    
    /**
     * 加密方式(MD5)
     * @param sign_type 加密方式
     * @return String 返回加密私钥
     * @throws Throwable
     */
    public static String getKey(String sign_type, boolean isAdd) throws Throwable {
    	String isTestAccount = Payment.get(Payment.IS_ACCOUNT_TEST);
        String jmkey = "";
        if ("MD5".equalsIgnoreCase(sign_type)) {
        	jmkey = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY)); //configureProvider.getProperty(LianLianPayVariable.KEY);
        	if ("true".equals(isTestAccount)) {
        		jmkey = AES.getInstace().decrypt(Payment.get(Payment.MD5_KEY_TEST));
        	}
        } else {
            // RSA
        	if (isAdd) {
        		jmkey = AES.getInstace().decrypt(Payment.get(Payment.RSA_P_KEY)); //configureProvider.getProperty(LianLianPayVariable.RSA_P_KEY);
        	} else {
        		jmkey = AES.getInstace().decrypt(Payment.get(Payment.RSA_PB_KEY)); //configureProvider.getProperty(LianLianPayVariable.RSA_P_KEY);
        	}
        }
        return jmkey;
    }
    
    /**
     * null/""（字符串）指针判断
     * @param str 字符串
     * @return  boolean 返回
     */
    public static boolean isnull(String str) {
        if (null == str || str.equalsIgnoreCase("null") || str.equals("")) {
            return true;
        } else
            return false;
    }
    
    
    /**
     * 时间格式转化处理
     * @param time 原时间格式
     * @return String 需要时间格式
     * @throws ParseException
     */
    public static String timeFormat(String time ) throws ParseException{
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date = (Date) sdf1.parse(time);  
       SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss"); 
       return sdf2.format(date.getTime());
    }
    
    
    /**
     * 验签
     * @param param
     * @return
     * @throws Throwable
     */
    public static boolean checkReturnSign(Map<String, String> param)throws 
    Throwable {
        boolean result = false;
        // 接接字段串
        String sb = "";
        String mySign = "";
        String sign = param.get(REQUEST_SIGN_NAME);
        sb = getSignStr(param);
        // 加密方式
        String sign_type = param.get("sign_type");
        String key = getKey(sign_type, false);
        logger.debug("sign_type is:" + sign_type + ";  sign string is:" + sb);
        if ("MD5".equals(sign_type)) {
            // Md5 加密
            sb += "&key=" + key;
            mySign = MD5Util.getMD5ofStr(sb).toLowerCase();
            if (mySign.equals(sign)) {
                result = true;
            }
            logger.debug("sign equals is:" + result + "; sign is:" + sign+ "; mysign is:" + mySign);
        } else {
            // rsa 加密
            String pubkeyvalue = Payment.get(Payment.RSA_PB_KEY); //configureProvider.getProperty(LianLianPayVariable.RSA_PB_KEY);
            result = RSAUtil.checksign(pubkeyvalue, sb, sign);
        }
        return result;
    }
    
    /**
     * 返回的是一个json字符串 转换成一个map<String,String>
     * 
     * @param request
     * @return map<String,String>
     * @throws Throwable
     */
    public Map<String, String> readReqStr(InputStream is) throws Throwable {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        	logger.error(e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {

            }
        }
        ObjectMapper objm = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, String> params = objm.readValue(sb.toString(), Map.class);
        return params;
    }
	
}
