package com.fenlibao.lianpay.v_1_0.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.lianpay.v_1_0.enums.SignTypeEnum;
import com.fenlibao.lianpay.v_1_0.security.Md5Algorithm;
import com.fenlibao.lianpay.v_1_0.security.TraderRSAUtil;
import com.fenlibao.lianpay.v_1_0.vo.BaseReturnParams;
import com.fenlibao.lianpay.v_1_0.vo.PayDataBean;

/**
 * 常用工具函数
 * 
 * @author guoyx e-mail:guoyx@lianlian.com
 * @date:2013-6-25 下午05:23:05
 * @version :1.0
 *          <p>
 *          updated by zcai
 *          </p>
 */
public class YinTongUtil {

	private static final Logger logger = LogManager.getLogger(YinTongUtil.class);

	private static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	// 签名字段
	public static final String PARAM_NAME_sign = "sign";
	public static final String PARAM_NAME_sign_type = "sign_type";
	public static final String SYMBOL_AND = "&";
	
	public static final String result_pay_SUCCESS = "SUCCESS";

	/**
	 * str空判断
	 * 
	 * @param str
	 * @return
	 * @author guoyx
	 */
	public static boolean isnull(String str) {
		if (null == str || str.equalsIgnoreCase("null") || str.equals("")) {
			return true;
		} else
			return false;
	}

	/**
	 * 获取当前时间str，格式yyyyMMddHHmmss
	 * 
	 * @return
	 * @author guoyx
	 */
	public static String getCurrentDateTimeStr() {
		Date date = new Date();
		String timeString = dataFormat.format(date);
		return timeString;
	}
	
	/**
	 * 获取签名类型
	 * @param reqStr 请求报文
	 * @return
	 */
	public static String getSignType(String reqStr) {
		JSONObject reqObj = JSON.parseObject(reqStr);
		if (reqObj == null) {
			return "";
		}
		return reqObj.getString(PARAM_NAME_sign_type);
	}

	/**
	 * 
	 * 功能描述：获取真实的IP地址
	 * 
	 * @param request
	 * @return
	 * @author guoyx
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (!isnull(ip) && ip.contains(",")) {
			String[] ips = ip.split(",");
			ip = ips[ips.length - 1];
		}
		return ip;
	}

	/**
	 * 生成待签名串
	 * 
	 * @param paramMap
	 * @return
	 * @author guoyx
	 */
	public static String genSignData(JSONObject jsonObject) {
		StringBuffer content = new StringBuffer();

		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>(jsonObject.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if (PARAM_NAME_sign.equals(key)) {
				continue;
			}
			String value = jsonObject.getString(key);
			// 空串不参与签名
			if (isnull(value)) {
				continue;
			}
			content.append((i == 0 ? "" : "&") + key + "=" + value);

		}
		String signSrc = content.toString();
		if (signSrc.startsWith("&")) {
			signSrc = signSrc.replaceFirst("&", "");
		}
		return signSrc;
	}

	/**
	 * 加签
	 * 
	 * @param reqObj
	 * @param key RSA签名-私钥
	 * @return
	 */
	public static String addSign(JSONObject reqObj, String key) {
		if (reqObj == null) {
			return "";
		}
		String sign_type = reqObj.getString(PARAM_NAME_sign_type);
		if (SignTypeEnum.MD5.getCode().equals(sign_type)) {
			return addSignMD5(reqObj, key);
		} else {
			return addSignRSA(reqObj, key);
		}
	}

	/**
	 * 签名验证
	 * @param reqStr
	 * @param key RSA验签-公钥
	 * @return
	 */
	public static boolean checkSign(String reqStr, String key) {
		JSONObject reqObj = JSON.parseObject(reqStr);
		if (reqObj == null) {
			return false;
		}
		String sign_type = reqObj.getString(PARAM_NAME_sign_type);
		if (SignTypeEnum.MD5.getCode().equals(sign_type)) {
			return checkSignMD5(reqObj, key);
		} else {
			return checkSignRSA(reqObj, key);
		}
	}

	/**
	 * RSA签名验证
	 * 
	 * @param reqObj
	 * @return
	 * @author guoyx
	 */
	private static boolean checkSignRSA(JSONObject reqObj, String rsa_public) {
		String sign = reqObj.getString(PARAM_NAME_sign);
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		try {
			if (TraderRSAUtil.checksign(rsa_public, sign_src, sign)) {
				return true;
			} else {
				logger.warn("RSA签名验证未通过, sign[{}], sign_src[{}]", sign, sign_src);
				return false;
			}
		} catch (Exception e) {
			logger.error("RSA签名验证异常, sign[{}], sign_src[{}]", sign, sign_src);
			logger.error("RSA签名验证异常", e);
			return false;
		}
	}

	/**
	 * MD5签名验证
	 * 
	 * @param signSrc
	 * @param sign
	 * @return
	 * @author guoyx
	 */
	private static boolean checkSignMD5(JSONObject reqObj, String md5_key) {
		String sign = reqObj.getString(PARAM_NAME_sign);
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		String wait_sign_str = sign_src + "&key=" + md5_key;
		String mySign = "";
		try {
			mySign = Md5Algorithm.getInstance().md5Digest(wait_sign_str.getBytes("utf-8"));
			if (sign.equals(mySign)) {
				return true;
			} else {
				logger.warn("MD5签名验证不通过,mySign[{}],sign[{}],sign_src[{}]", mySign, sign, sign_src);
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("MD5签名验证异常,mySign[{}],sign[{}],sign_src[{}]", mySign, sign, sign_src);
			return false;
		}
	}

	/**
	 * RSA加签名
	 * 
	 * @param reqObj
	 * @param rsa_private
	 * @return
	 * @author guoyx
	 */
	private static String addSignRSA(JSONObject reqObj, String rsa_private) {
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		try {
			return TraderRSAUtil.sign(rsa_private, sign_src);
		} catch (Exception e) {
			logger.error("RSA加签名异常,sign_src[], exception >>> ", sign_src, e);
			return "";
		}
	}

	/**
	 * MD5加签名
	 * 
	 * @param reqObj
	 * @param md5_key
	 * @return
	 * @author guoyx
	 */
	private static String addSignMD5(JSONObject reqObj, String md5_key) {
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		sign_src += "&key=" + md5_key;
		try {
			return Md5Algorithm.getInstance().md5Digest(sign_src.getBytes("utf-8"));
		} catch (Exception e) {
			logger.error("MD5加签名异常,sign_src[], exception >>> ", sign_src, e);
			return "";
		}
	}

	/**
	 * 读取request流
	 * 
	 * @param req
	 * @return
	 * @author guoyx
	 */
	public static String readReqStr(HttpServletRequest request) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取支付请求数据
	 * @param params
	 * @param key
	 * @return 提交给连连的参数
	 * @author zcai
	 */
	public static String getPayReqData(Map<String,String> params, String key) {
		JSONObject reqObj = JSON.parseObject(JSON.toJSONString(params));
	    String sign = YinTongUtil.addSign(reqObj, key);
	    params.put("sign", sign);
		return JSON.toJSONString(params);
	}
	
	/**
	 * 对请求参数设置签名
	 * @param params
	 * @param key
	 * @return 提交给连连的参数
	 * @author zcai
	 */
	public static void putSign(Map<String,String> params, String key) {
		JSONObject reqObj = JSON.parseObject(JSON.toJSONString(params));
		String sign = YinTongUtil.addSign(reqObj, key);
		params.put("sign", sign);
	}
	
	/**
	 * 判断连连回调交易是否成功
	 * @param callbackData
	 * @return
	 */
	public static boolean isTradeSuccess(PayDataBean payDataBean) {
		if (result_pay_SUCCESS.equals(payDataBean.getResult_pay())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 连连返回的字符串转成bean
	 * @param callbackData
	 * @return
	 */
	public static PayDataBean getCallbackBean(String callbackData) {
		return JSON.parseObject(callbackData, PayDataBean.class);
	}
	/**
	 * 从流里获取PayDataBean
	 * @param callbackData
	 * @return
	 */
	public static PayDataBean getCallbackBean(HttpServletRequest request) {
		return JSON.parseObject(readReqStr(request), PayDataBean.class);
	}
	
	/**
	 * 从连连同步返回的数据里获取基础数据
	 * @param returnParams
	 * @return
	 */
	public static BaseReturnParams getBaseReturnParams(String returnParams) {
		return JSON.parseObject(returnParams, BaseReturnParams.class);
	}
}
