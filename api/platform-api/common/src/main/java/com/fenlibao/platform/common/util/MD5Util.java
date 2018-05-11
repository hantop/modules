package com.fenlibao.platform.common.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * md5签名工具类
 * @author yangzengcai
 * @date 2016年2月20日
 */
public class MD5Util {

	/**
	 * 签名生成算法
	 * 
	 * @param HashMap<String,String>
	 *            params 请求参数集，所有参数必须已转换为字符串类型
	 * @param String
	 *            secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(Map<String, String> params, String secret) throws IOException {
		// 先将参数以其参数名的字典序升序进行排序
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		for (Entry<String, String> param : entrys) {
			basestring.append(param.getKey()).append("=").append(param.getValue());
		}
		basestring.append(secret);

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
			sign.append(hex);
		}
		return sign.toString();
	}

	
	
	public static void main(String[] args) {
		String secret = "develop"; //"097c0d192d084413aae2571638fe965e" "develop";
		Map<String, String> params = new HashMap<>();

//		params.put("appid", "d3e2d9a1c44a43e4");
//		params.put("phone_num", "13751763776");
		
//		params.put("openid", "595f455c58f145799c40b6259c1be717");
//		params.put("amount", "10");
//		params.put("typecode", "MINISO_CONSUME_POINTS");
//		params.put("pos_sn", "121323424343");
		
		params.put("appid", "develop");
		params.put("merchant_appid", "d3e2d9a1c44a43e4");
//		params.put("secret", "1234567890");
//		params.put("status", "1");
		
		params.put("ip", "0:0:0:0:0:0:0:1");
		
		try {
			String sign = getSignature(params, secret);
			System.out.println(sign);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
