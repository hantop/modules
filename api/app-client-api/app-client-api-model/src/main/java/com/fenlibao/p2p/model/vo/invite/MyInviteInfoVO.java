/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: MyInviteInfo.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.invite 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-13 下午2:39:20 
 * @version: V1.1   
 */
package com.fenlibao.p2p.model.vo.invite;

/** 
 * @ClassName: MyInviteInfo 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午2:39:20  
 */
public class MyInviteInfoVO {
	
	public int myRepackageNum; //我的返现红包数量
	
	public int inviteUserNum; //邀请人数
	
	public int inviteUserRegNum; //注册人数
	
	public int inviteUserInvNum; //投资人数

	/**
	 * @return the myRepackageNum
	 */
	public int getMyRepackageNum() {
		return myRepackageNum;
	}

	/**
	 * @param myRepackageNum the myRepackageNum to set
	 */
	public void setMyRepackageNum(int myRepackageNum) {
		this.myRepackageNum = myRepackageNum;
	}

	/**
	 * @return the inviteUserNum
	 */
	public int getInviteUserNum() {
		return inviteUserNum;
	}

	/**
	 * @param inviteUserNum the inviteUserNum to set
	 */
	public void setInviteUserNum(int inviteUserNum) {
		this.inviteUserNum = inviteUserNum;
	}

	/**
	 * @return the inviteUserRegNum
	 */
	public int getInviteUserRegNum() {
		return inviteUserRegNum;
	}

	/**
	 * @param inviteUserRegNum the inviteUserRegNum to set
	 */
	public void setInviteUserRegNum(int inviteUserRegNum) {
		this.inviteUserRegNum = inviteUserRegNum;
	}

	/**
	 * @return the inviteUserInvNum
	 */
	public int getInviteUserInvNum() {
		return inviteUserInvNum;
	}

	/**
	 * @param inviteUserInvNum the inviteUserInvNum to set
	 */
	public void setInviteUserInvNum(int inviteUserInvNum) {
		this.inviteUserInvNum = inviteUserInvNum;
	}

}
