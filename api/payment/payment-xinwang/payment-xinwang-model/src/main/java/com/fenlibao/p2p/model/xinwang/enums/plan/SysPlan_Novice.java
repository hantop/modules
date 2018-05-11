package com.fenlibao.p2p.model.xinwang.enums.plan;

public enum SysPlan_Novice {
	/**
	 * 新手专享
	 */
    YES(1),
    /**
     * 不是新手专享
     */
    NO(0),
    ;
	
	private int code;
	
	SysPlan_Novice(int code){
		this.code=code;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public static SysPlan_Novice parse(int code){
		for(SysPlan_Novice item:SysPlan_Novice.values()){
			if(item.getCode()==code){
				return item;
			}
		}
		return null;
	}
}
