package com.fenlibao.p2p.model.xinwang.enums.plan;

public enum SysPlan_Status {
	/**
	 * 1:待提交 
	 */
    DTJ(1,"待提交"),
    /**
     * 2:待审核
     */
    DSH(2,"待审核"),
    /**
     *  3:待发布
     */
    DFB(3,"待发布"),
    /**
     *  4: 投资中
     */
    TBZ(4,"出借中"),
    /**
     *  5:还款中 
     */
    HKZ(5,"还款中"),
    /**
     * 6:已结清
     */
    YJQ(6,"已结清"),
    /**
     *  7:已作废 
     */
    YZF(7,"已作废"),
    /**
     * 8:预发布
     */
    YFB(8,"预发布"),
    ;
	
	private int code;
	private String name;

	SysPlan_Status(int code,String name){
		this.code=code;
		this.name=name;
	}
	
	public int getCode(){
		return this.code;
	}

	public String getName(){
		return this.name;
	}
	
	public static SysPlan_Status parse(int code){
		for(SysPlan_Status item:SysPlan_Status.values()){
			if(item.getCode()==code){
				return item;
			}
		}
		return null;
	}
}
