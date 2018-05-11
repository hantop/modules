package com.fenlibao.p2p.model.entity.pay;

import com.dimeng.p2p.S61.enums.T6141_F03;

public abstract interface UserInsert {
	/**
	 * 账号名
	 * 
	 * @return
	 */
	public abstract String getAccountName();

	/**
	 * 密码
	 * 
	 * @return
	 */
	public abstract String getPassword();

	/**
	 * 邀请码
	 * 
	 * @return
	 */
	public abstract String getCode();
	
	
	/**
	 * 邀请人手机号
	 * 
	 * @return
	 */
	public abstract String getPhone();
	
	/**
	 * 邀请人姓名
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 兴趣类型（LC:投资;JK:借款）
	 */
	public abstract T6141_F03 getType();
}
