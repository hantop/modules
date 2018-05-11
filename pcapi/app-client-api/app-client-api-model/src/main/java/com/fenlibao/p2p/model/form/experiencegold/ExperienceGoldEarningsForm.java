package com.fenlibao.p2p.model.form.experiencegold;

/**
 * 体验金收益form
 * @author yangzengcai
 * @date 2015年11月18日
 */
public class ExperienceGoldEarningsForm {

	/**
	 * 收益类型
	 * (说不用变)
	 */
	private String typeName = "体验金利息";
	
	/**
	 * 收益日期
	 */
	private String date;
	
	/**
	 * 收益金额
	 */
	private String amount;
	

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
