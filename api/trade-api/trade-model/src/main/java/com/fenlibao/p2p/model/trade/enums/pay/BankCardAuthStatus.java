package com.fenlibao.p2p.model.trade.enums.pay;

public enum BankCardAuthStatus {
	   WRZ("未认证"),
	   YRZ("已认证"),
	   KTX("可提现"),
	   ;
	    protected final String chineseName;

	    private BankCardAuthStatus(String chineseName){
	        this.chineseName = chineseName;
	    }
	    /**
	     * 获取中文名称.
	     * 
	     * @return {@link String}
	     */
	    public String getChineseName() {
	        return chineseName;
	    }
}
