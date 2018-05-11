package com.fenlibao.model.pms.da.global.plan;

/**
 */
public enum InvestPlanInfo_CycleType {
    DAY("d"),
    MONTH("m"),
    ;

	private String code;
	
	public String getCode() {
		return code;
	}

	InvestPlanInfo_CycleType(String code){
		this.code=code;
	}
	
    public static InvestPlanInfo_CycleType parse(String code){
    	for(InvestPlanInfo_CycleType item:InvestPlanInfo_CycleType.values()){
    		if(item.getCode().equals(code)){
    			return item;
    		}
    	}
    	return null;
    }
}
