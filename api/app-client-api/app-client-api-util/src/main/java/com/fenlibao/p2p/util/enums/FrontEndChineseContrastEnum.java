package com.fenlibao.p2p.util.enums;

/**
 * 还款的类型
 */
public enum FrontEndChineseContrastEnum {

	projectName("projectName", "项目名称"),
	borrowAmount("borrowAmount", "借款金额"),
	borrowTimeLimit("borrowTimeLimit", "借款期限"),
	exceptAnnualised("exceptAnnualised", "参考年化"),
	borrowPurpose("borrowPurpose", "借款用途"),
	authentication("authenticationType", "认证类型"),
	repaymentSource("repaymentSource", "还款来源"),
	repaymentType("repaymentType", "还款方式"),
	interestWay("interestWay", "计息方式"),
	interestTime("interestTime", "起息时间"),
	earlyQuit("earlyQuit", "提前退出"),
	guaranteeMeasure("guaranteeMeasure", "担保措施"),

	planAmount("planAmount", "组合金额"),
	planTimeLimit("planTimeLimit", "组合期限"),

	borrower("borrower", "借款人"),
	identificationNumber("identificationNumber", "证件号码"),
	address("address", "地址"),
	themeProperty("subjectCharacter", "主体性质"),
	whatTrade("whatTrade", "所属行业"),
	incomeAndDebt("incomeAndDebt", "收入及负债情况"),
	creditInvestigation("creditInvestigation", "征信报告"),
	bankFlow("bankFlow", "银行流水"),
	otherBorrowerInfo("otherBorrowerInfo", "其他借款情况"),
	enterpriseName("enterpriseName", "企业名称"),
	enterpriseRegisterNo("enterpriseRegisterNo", "注册号"),

	useDetail("useDetail", "借款资金运用情况"),
	borrowerManageAndFinanceCondition("borrowerManageAndFinanceCondition", "借款人经营状况及财务状况"),
	borrowerRepayAbilityVarietyCondition("borrowerRepayAbilityVarietyCondition", "借款人还款能力变化情况"),
	borrowerOverdueCondition("borrowerOverdueCondition", "借款人逾期情况"),
	borrowerSueCondition("borrowerSueCondition", "借款人涉诉情况"),
	borrowerAdministrativePenaltyCondition("borrowerAdministrativePenaltyCondition", "借款人受行政处罚情况");

	private String key;

	private String value;

	private FrontEndChineseContrastEnum(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public static String getFrontEndChineseContrastEnum(String key){
		for(FrontEndChineseContrastEnum frontEndChineseContrastEnum: FrontEndChineseContrastEnum.values()){
			if(frontEndChineseContrastEnum.getKey().equals(key)){
				return frontEndChineseContrastEnum.getValue();
			}
		}
		return key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
