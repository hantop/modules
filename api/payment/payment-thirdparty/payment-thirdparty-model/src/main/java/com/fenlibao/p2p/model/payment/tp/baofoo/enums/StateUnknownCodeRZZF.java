package com.fenlibao.p2p.model.payment.tp.baofoo.enums;

/**
 * 认证支付应答码为未知状态
 * @author Administrator
 *
 */
public enum StateUnknownCodeRZZF {
	BF00100,
	BF00112,
	BF00113,
	BF00115,
	BF00144,
	BF00202;
	
	public static boolean contain(String code){
		for(StateUnknownCodeRZZF item:StateUnknownCodeRZZF.values()){
			if(item.name().equals(code)){
				return true;
			}
		}
		return false;
	}
}
