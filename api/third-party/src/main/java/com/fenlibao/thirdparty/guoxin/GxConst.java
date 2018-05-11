package com.fenlibao.thirdparty.guoxin;

/**
 * 国信万源 -实名认证
 * 
 * @author junda.feng 2016-4-18
 * 
 */
public interface GxConst {
	
//	String KEY = Config.get("gx.key");//加密
//	String URL = Config.get("gx.invoke.url");//调用的webservice接口地址
//	String USERNAME = Config.get("gx.username");//用户名
//	String PASSWORD = Config.get("gx.password");//密码
	
	String TYPE = "ID100001";//身份证验证
	
	Integer authStatus_YZ =0;//查询结果一致
	
	Integer authStatus_BYZ =1;//不一致
	
	Integer authStatus_NULL =2;//库中无此号
	
	String  ORDER_FEE="1.1";//费率
	
	String  authmessageStatus_success="0";//0： 查询成功  -9011 余额不足 ...
	
	String  messageStatus_success="0";//0 ：认证成功  -9011 余额不足 ...
}
