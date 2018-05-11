package com.fenlibao.p2p.model.global;

/**
 * 标担保措施
 */
public enum GuaranteeMeasureEnum {

	ONE(1, "应收款质押"),

	TWO(2, "保证担保"),

	THREE(3, "房产抵押"),

	FOUR(4, "车辆质押"),

	FIVE(5, "信用借款");

	private int type;

	private String content;

	private GuaranteeMeasureEnum(int type, String content){
		this.type = type;
		this.content = content;
	}
	
	public static String getGuaranteeMeasure(int type){
		for(GuaranteeMeasureEnum guaranteeMeasure: GuaranteeMeasureEnum.values()){
			if(guaranteeMeasure.getType() == type){
				return guaranteeMeasure.getContent();
			}
		}
		return "";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
