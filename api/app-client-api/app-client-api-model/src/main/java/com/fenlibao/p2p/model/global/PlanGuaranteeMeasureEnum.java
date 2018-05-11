package com.fenlibao.p2p.model.global;

/**
 * 计划担保措施
 */
public enum PlanGuaranteeMeasureEnum {

	ONE(1, "信用借款");

	private int type;

	private String content;

	private PlanGuaranteeMeasureEnum(int type, String content){
		this.type = type;
		this.content = content;
	}
	
	public static String getGuaranteeMeasure(int type){
		for(PlanGuaranteeMeasureEnum guaranteeMeasure: PlanGuaranteeMeasureEnum.values()){
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
