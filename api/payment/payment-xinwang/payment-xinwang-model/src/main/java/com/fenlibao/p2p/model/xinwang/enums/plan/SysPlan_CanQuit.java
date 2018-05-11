package com.fenlibao.p2p.model.xinwang.enums.plan;

public enum SysPlan_CanQuit {
	/**
	 * 是随时退出
	 */
    YES(1),
    /**
     * 不是随时退出
     */
    NO(2),
    ;
	
	private int code;
	
	SysPlan_CanQuit(int code){
		this.code=code;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public static SysPlan_CanQuit parse(int code){
		for(SysPlan_CanQuit item:SysPlan_CanQuit.values()){
			if(item.getCode()==code){
				return item;
			}
		}
		return null;
	}
}
