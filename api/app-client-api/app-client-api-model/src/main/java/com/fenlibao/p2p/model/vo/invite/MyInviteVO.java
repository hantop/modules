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
import java.util.Date;

/** 
 * @author: junda.feng
 * @date: 2015-11-13 下午3:11:24  
 */
public class MyInviteVO {
	
	public String userName;   //邀请用户实名认证姓名
	
	public String userCell;  //邀请用户手机号码
	
	public boolean hasInvest; //是否投资
	
	public Long userRegTime;  //邀请用户注册时间戳

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCell() {
		return userCell;
	}

	public void setUserCell(String userCell) {
		this.userCell = userCell;
	}

	public boolean isHasInvest() {
		return hasInvest;
	}

	public void setHasInvest(boolean hasInvest) {
		this.hasInvest = hasInvest;
	}

	public Long getUserRegTime() {
		return userRegTime;
	}

	public void setUserRegTime(Long userRegTime) {
		this.userRegTime = userRegTime;
	}



}
