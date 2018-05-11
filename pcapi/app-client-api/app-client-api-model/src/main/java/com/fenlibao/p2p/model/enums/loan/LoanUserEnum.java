package com.fenlibao.p2p.model.enums.loan;

public enum LoanUserEnum {

	NATURAL(1, "自然人"),
	LEGAL(2, "法人"),
	OTHER(3, "其他组织机构"),
	;
	private int key;
	private String value;

	private LoanUserEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static String get(int key) {
		for (LoanUserEnum range : LoanUserEnum.values()) {
			if (range.getKey()==key) {
				return range.getValue();
			}
		}
		return null;
	}
	
}
