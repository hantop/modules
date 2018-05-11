package com.fenlibao.p2p.util.sms;

import java.io.Serializable;

public class SmsVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String[] mobiles;//手机号码(群发为字符串数组推荐最多为200个手机号码或以内))
	
	private String content;//短信内容(最多500个汉字或1000个纯英文)
	
	private int priority;//优先级(级别从1到5的正整数，数字越大优先级越高，越先被发送)
	
	private String username;//发送短信时设置的用户名
	
	private String password;//密码
	
	private String serial;//短信类型
	
	private String charset;//编码
	
	private int code;//返回码

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String[] getMobiles() {
		return mobiles;
	}

	public void setMobiles(String[] mobiles) {
		this.mobiles = mobiles;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
