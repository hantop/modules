package com.fenlibao.p2p.model.payment.tp.baofoo.enums;

public enum WithdrawState {
	TRANSFERING(0, "转帐中"), SUCCESS(1, "转帐成功"), FAIL(-1, "转帐失败"), REFUND(2, "转帐退款"),;

	private int value;
	private String explain;

	WithdrawState(int value, String explain) {
		this.value = value;
		this.explain = explain;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

}
