package com.fenlibao.p2p.model.payment.tp.baofoo.enums;

public enum TxnSubType {
	DIRECT_BIND("01"),
	UN_BIND("02"),
	BIND_QUERY("03"),
	PRE_TOPUP("15"),
	CONFIRM_TOPOP("16"),
	RECHARGE_QUERY("31"),
	;
	
	private String code;
	
	TxnSubType(String code){
		this.code=code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
}
