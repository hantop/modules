package com.fenlibao.p2p.model.xinwang.enums.plan;

public enum SysPlan_CycleType {
	/**
	 * 天
	 */
    DAY("d"),
    /**
     * 月
     */
    MONTH("m"),
    ;
	
	private String code;
	
	SysPlan_CycleType(String code){
		this.code=code;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public static SysPlan_CycleType parse(String code){
		for(SysPlan_CycleType item:SysPlan_CycleType.values()){
			if(item.getCode().equals(code)){
				return item;
			}
		}
		return null;
	}
}
