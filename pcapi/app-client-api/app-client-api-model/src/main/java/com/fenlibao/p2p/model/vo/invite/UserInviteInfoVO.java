/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: t.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.invite 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-13 下午3:11:24 
 * @version: V1.1   
 */
package com.fenlibao.p2p.model.vo.invite;

import java.math.BigDecimal;

/** 
 * @ClassName: t 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午3:11:24  
 */
public class UserInviteInfoVO {
	
	public String userName;   //邀请用户实名认证姓名
	
	public String userCell;  //邀请用户手机号码
	
	public BigDecimal userInvestSum; //邀请用户累计投资金额 
	
	public long userRegTime;  //邀请用户注册时间戳

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
	public long getUserRegTime() {
		return userRegTime;
	}

	/**
	 * @param userRegTime the userRegTime to set
	 */
	public void setUserRegTime(long userRegTime) {
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
