package com.fenlibao.p2p.model.vo.invite;

import java.math.BigDecimal;

/** 
 * @author: junda.feng
 * @date: 2016-6-20
 * @version: V2.0
 */
public class CountMyInviteInfoVO {
	
	public int inviteUserRegNum; //邀请人数
	
	public BigDecimal aword; //奖励

	public int getInviteUserRegNum() {
		return inviteUserRegNum;
	}

	public void setInviteUserRegNum(int inviteUserRegNum) {
		this.inviteUserRegNum = inviteUserRegNum;
	}

	public BigDecimal getAword() {
		return aword;
	}

	public void setAword(BigDecimal aword) {
		this.aword = aword;
	}


	

}
