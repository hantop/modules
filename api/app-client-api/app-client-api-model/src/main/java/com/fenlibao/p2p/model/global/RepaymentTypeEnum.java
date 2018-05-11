package com.fenlibao.p2p.model.global;

/**
 * 还款的类型
 */
public enum RepaymentTypeEnum {

	DEBX("DEBX","等额本息"),

	MYFX("MYFX","每月付息,到期还本"),

	YCFQ("YCFQ","本息到期一次付清"),

	DEBJ("DEBJ","等额本金");

	private String index;

	private String repaymentType;

	private RepaymentTypeEnum(String index, String repaymentType){
		this.index = index;
		this.repaymentType = repaymentType;
	}
	
	public static String getRepaymentType(String index){
		for(RepaymentTypeEnum repaymentType: RepaymentTypeEnum.values()){
			if(repaymentType.getIndex().equals(index)){
				return repaymentType.getRepaymentType();
			}
		}
		return null;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}
}
