package com.fenlibao.model.pms.da.cs;

/**
 * 查询相应用户信息
 * @author Administrator
 *
 */
public class UserDetail {

	private int userId;//用户ID
	 private String phoneNum;//手机号
	 private String name;//姓名
	 private String idCard;//身份证号
	 private String idCardSec;//身份证号加星存储
	 public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getIdCardSec() {
		return idCardSec;
	}
	public void setIdCardSec(String idCardSec) {
		this.idCardSec = idCardSec;
	}
	private String nickName;//昵称
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	 
}
