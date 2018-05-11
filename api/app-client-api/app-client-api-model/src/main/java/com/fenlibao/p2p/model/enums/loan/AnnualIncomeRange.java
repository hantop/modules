package com.fenlibao.p2p.model.enums.loan;

public enum AnnualIncomeRange {

	RANGE_2K_DOWN("1", "2000元以下"), 
	RANGE_2K_4K("2", "2000-4000元"),
	RANGE_4K_6K("3", "4000-6000元"),
	RANGE_6K_10K("4", "6000-10000元"),
	RANGE_10K_20K("5", "10000-20000元"),
	RANGE_20K_50K("6", "20000-50000元"),
	RANGE_50K_UP("7", "50000元以上"),
	;
	
	private String key;
	private String value;
	
	private AnnualIncomeRange(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
	public static AnnualIncomeRange get(String key) {
		for (AnnualIncomeRange range : AnnualIncomeRange.values()) {
			if (range.getKey().equals(key)) {
				return range;
			}
		}
		return null;
	}
	
}
