/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: t.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.entity.invite 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-13 下午3:15:33 
 * @version: V1.1   
 */
package com.fenlibao.p2p.model.entity.invite;

import java.math.BigDecimal;
import java.util.Date;

/** 
 * @ClassName: UserInviteInfo
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午3:11:24  
 */
public class UserInviteInfo {
	
	public String userName;   //邀请用户实名认证姓名
	
	public String userCell;  //邀请用户手机号码
	
	public BigDecimal userInvestSum;  //邀请用户累计投资金额
	
	public Date userRegTime;  //邀请用户注册时间

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userCell
	 */
	public String getUserCell() {
		return userCell;
	}

	/**
	 * @param userCell the userCell to set
	 */
	public void setUserCell(String userCell) {
		this.userCell = userCell;
	}

	/**
	 * @return the userRegTime
	 */
	public Date getUserRegTime() {
		return userRegTime;
	}

	/**
	 * @param userRegTime the userRegTime to set
	 */
	public void setUserRegTime(Date userRegTime) {
		this.userRegTime = userRegTime;
	}

	/**
	 * @return the userInvestSum
	 */
	public BigDecimal getUserInvestSum() {
		return userInvestSum;
	}

	/**
	 * @param userInvestSum the userInvestSum to set
	 */
	public void setUserInvestSum(BigDecimal userInvestSum) {
		this.userInvestSum = userInvestSum;
	}
	
}

