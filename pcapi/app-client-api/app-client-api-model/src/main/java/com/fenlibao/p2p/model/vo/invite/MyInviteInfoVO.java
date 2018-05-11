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

import java.math.BigDecimal;

/**
 * @ClassName: MyInviteInfo 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午2:39:20  
 */
public class MyInviteInfoVO {
	public int inviteUserRegNum; //注册人数
	public int inviteUserInvNum; //投资人数
	public BigDecimal inviteAwardSum; //邀请奖励总额

	public int getInviteUserRegNum() {
		return inviteUserRegNum;
	}

	public void setInviteUserRegNum(int inviteUserRegNum) {
		this.inviteUserRegNum = inviteUserRegNum;
	}

	public int getInviteUserInvNum() {
		return inviteUserInvNum;
	}

	public void setInviteUserInvNum(int inviteUserInvNum) {
		this.inviteUserInvNum = inviteUserInvNum;
	}

	public BigDecimal getInviteAwardSum() {
		return inviteAwardSum;
	}

	public void setInviteAwardSum(BigDecimal inviteAwardSum) {
		this.inviteAwardSum = inviteAwardSum;
	}
}
