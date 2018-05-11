package com.fenlibao.platform.common.util;


public enum AESInstanceEnum {
	INSTANCE;

	private AES aes;

	AESInstanceEnum() {
		aes = new AES();
	}

	public AES getInstance() {
		return aes;
	}
}
