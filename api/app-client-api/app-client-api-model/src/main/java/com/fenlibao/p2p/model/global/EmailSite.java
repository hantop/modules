package com.fenlibao.p2p.model.global;

import java.util.HashMap;
import java.util.Map;

public final class EmailSite {

	static Map<String,String> emailMap=new HashMap<String,String>();
	
	static{
		init();
	}
	
	public static void init(){
		emailMap.put("qq.com", "http://mail.qq.com");
		emailMap.put("126.com", "http://www.126.com");
		emailMap.put("163.com", "http://mail.163.com");
		emailMap.put("139.com", "http://mail.10086.cn");
		emailMap.put("yeah.net", "http://www.yeah.net");
		emailMap.put("sohu.com", "http://mail.sohu.com");
		emailMap.put("sina.com", "http://mail.sina.com.cn");
		emailMap.put("vip.163.com", "http://vip.163.com");
		emailMap.put("189.cn", "http://mail.189.cn");
		emailMap.put("aliyun.com", "http://mail.aliyun.com");
		emailMap.put("tom.com", "http://mail.tom.com");
	}

	public static String get(String key){
		return emailMap.get(key);
	}
	
}
