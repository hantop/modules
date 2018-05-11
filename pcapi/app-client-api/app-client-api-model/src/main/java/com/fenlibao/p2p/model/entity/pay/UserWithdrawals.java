package com.fenlibao.p2p.model.entity.pay;

import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.enums.T6110_F06;

public class UserWithdrawals extends T6130 {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户登录账号
	 */
	public String userName;
	/**
	 * 真实姓名
	 */
	public String realName;

	/**
	 * 用户类型
	 */
	public T6110_F06 userType;

	/**
	 * 提现银行
	 */
	public String extractionBank;

	/**
	 * 开户所在地
	 */
	public String location;

	/**
	 * 所在支行
	 */
	public String subbranch;

	/**
	 * 银行卡号
	 */
	public String bankId;
	/**
	 * 审核人
	 */
	public String shName;

	/**
	 * 提现人
	 */
	public String txName;
	/**
     * 省
     */
    public String shengName;
    /**
     * 市
     */
    public String shiName;
}