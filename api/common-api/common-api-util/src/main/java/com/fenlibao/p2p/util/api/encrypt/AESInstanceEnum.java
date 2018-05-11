package com.fenlibao.p2p.util.api.encrypt;


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
