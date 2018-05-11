package com.fenlibao.p2p.model.global;

/**
 * 排序的类型
 */
public enum SortEnum {

	BIDTIME("BIDTIME","1"),//发标时间
	
	RATE("RATE","2"),//年化利率
	
	PROGRESS("PROGRESS","3"),//投标进度
	
	PERIOD("PERIOD","4"),//借款周期

	REPAYMENT("BIDSTATUS","5");//标状态

	private String orderType;
	
	private String index;
	
	private SortEnum(String orderType,String index){
		this.orderType = orderType;
		this.index = index;
	}
	
	public static String getOrderType(String index){
		for(SortEnum sort:SortEnum.values()){
			if(sort.getIndex().equals(index)){
				return sort.getOrderType();
			}
		}
		return null;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	
}
