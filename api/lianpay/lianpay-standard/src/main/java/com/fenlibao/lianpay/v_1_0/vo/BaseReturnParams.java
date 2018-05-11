package com.fenlibao.lianpay.v_1_0.vo;

/**
 * 请求连连同步返回的基础参数
 * @author zcai
 * @date 2016年4月27日
 */
public class BaseReturnParams {
	
	public static final String SUCCESS_CODE = "0000";

	private String ret_code;//交易结果代码
	private String ret_msg;//交易结果描述
	private String sign_type;//签名方式
	private String sign;//签名
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
