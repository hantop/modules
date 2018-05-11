package com.fenlibao.p2p.util.pay;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sun.misc.BASE64Decoder;

public class Base64 {

	protected static final Logger logger = LogManager.getLogger(Base64.class);
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		try {
			return (new sun.misc.BASE64Encoder()).encode(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e, e);
		}
		return null;
	}

	public static String getBASE64(byte[] b) {
		return (new sun.misc.BASE64Encoder()).encode(b);
	}

	// BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	// BASE64 编码的字符串 s 进行解码
	public static byte[] getBytesBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return b;
		} catch (Exception e) {
			return null;
		}
	}
	
}
