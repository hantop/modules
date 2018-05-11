package com.fenlibao.p2p.model.enums.loan;

public enum LoanAmountRange {

	RANGE_5_DOWN("1", "5万以下"), 
	RANGE_5_10("2", "5万-10万"),
	RANGE_10_20("3", "10万-20万"),
	RANGE_20_50("4", "20万-50万"),
	RANGE_50_100("5", "50万-100万"),
	RANGE_100_UP("6", "100万以上"),
	;
	
	private String key;
	private String value;
	
	private LoanAmountRange(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static LoanAmountRange get(String key) {
		for (LoanAmountRange range : LoanAmountRange.values()) {
			if (range.getKey().equals(key)) {
				return range;
			}
		}
		return null;
	}
	
}
