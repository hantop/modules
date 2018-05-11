package com.fenlibao.thirdparty.guoxin.vo.xsd;


/**
 * @author junda.feng
 */
public class IdentityResponseSingle implements java.io.Serializable {
	
	private String authmessageStatus;//0 查询成功  -9011 余额不足 ...
	
	private String authmessageValue;//提示信息
	
	private String messageStatus;//0 信息认证成功 -9012 系统异常 ...
	
	private String messageValue;//提示信息
	
	private String birthday;//出生日期

	private String identitycard;//身份证号码

	private String name;//姓名

//	private String nation;//

	private String photo;//照片的base64编码

	private Integer authStatus;//认证状态  0查询结果一致 1不一致 2库中无此号
	//
	private String authResult;//认证结果
	
	private String yyfzd;//原始发证地

	private String sex;//性别

	
	
	public String getAuthmessageValue() {
		return authmessageValue;
	}

	public void setAuthmessageValue(String authmessageValue) {
		this.authmessageValue = authmessageValue;
	}

	public String getMessageValue() {
		return messageValue;
	}

	public void setMessageValue(String messageValue) {
		this.messageValue = messageValue;
	}

	public String getAuthmessageStatus() {
		return authmessageStatus;
	}

	public void setAuthmessageStatus(String authmessageStatus) {
		this.authmessageStatus = authmessageStatus;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public String getNation() {
//		return nation;
//	}
//
//	public void setNation(String nation) {
//		this.nation = nation;
//	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	public String getAuthResult() {
		return authResult;
	}

	public void setAuthResult(String authResult) {
		this.authResult = authResult;
	}

	public String getYyfzd() {
		return yyfzd;
	}

	public void setYyfzd(String yyfzd) {
		this.yyfzd = yyfzd;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
