package com.fenlibao.p2p.model.payment.tp.baofoo.enums;

public enum BankCode {
	ICBC("中国工商银行"),
	ABC("中国农业银行"),
	CCB("中国建设银行"),
	BOC("中国银行"),
	BCOM("中国交通银行"),
	CIB("兴业银行"),
	CITIC("中信银行"),
	CEB("中国光大银行"),
	PAB("平安银行"),
	PSBC("中国邮政储蓄银行"),
	SHB("上海银行"),
	SPDB("浦东发展银行"),
	CMBC("中国民生银行"),
	CMB("招商银行"),
	GDB("广发银行"),
	HXB("华夏银行"),
	;
	
	private String chineseName;
	
	BankCode(String chineseName){
		this.chineseName=chineseName;
	}

	public String getChineseName() {
		return chineseName;
	}
	
	public static String getChineseName(String code){
		for(BankCode value:BankCode.values()){
			if(value.name().equals(code)){
				return value.chineseName;
			}
		}
		return null;
	}
}
