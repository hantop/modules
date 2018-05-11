package com.fenlibao.p2p.model.enums.bid;

public enum GuaranteeMeasureEunm {
	GATHERING("1", "应收款质押"),
	ENSURE("2", "保证担保"),
	HOURSE("3", "房产抵押"),
	CAR("4", "车辆质押"),
	CREDIT("5", "信用借款"),
	;
	
	private String key;
	private String value;
	
	private GuaranteeMeasureEunm(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	


	public static String get(String key) {
		for (GuaranteeMeasureEunm range : GuaranteeMeasureEunm.values()) {
			if (range.getKey().equals(key)) {
				return range.getValue();
			}
		}
		return null;
	}
	
}
