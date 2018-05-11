package com.fenlibao.common.pms.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
