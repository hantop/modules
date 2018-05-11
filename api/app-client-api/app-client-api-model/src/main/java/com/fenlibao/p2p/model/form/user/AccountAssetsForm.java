package com.fenlibao.p2p.model.form.user;

/**
 * 账户资产
 * @author yangzengcai
 * @date 2015年11月27日
 */
public class AccountAssetsForm {

	public static final String TOTAL = "合计";
	public static final String INVESTMENT = "出借资产";
	public static final String CASH = "现金资产";
	
	public static final String KDB = "开店王";
	public static final String QRB = "企融宝";
	public static final String XJB = "薪金宝";
	public static final String HQB = "活期宝";
	public static final String ZQZR = "债权转让";
	
	public static final String BALANCE = "账户余额";
	public static final String INVESTMENT_FREEZE = "出借中冻结金额";
	public static final String WITHDRAW_FREEZE = "提现中冻结金额";
	
	public static final String KEY_TOTAL = "total";
	public static final String KEY_INVESTMENT = "investment";
	public static final String KEY_CASH = "cash";
	
	/**
	 * 资产名称
	 */
	private String name;
	
	/**
	 * 资产金额
	 */
	private String amount;

	
	public AccountAssetsForm() {
		super();
	}

	public AccountAssetsForm(String name, String amount) {
		super();
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}
