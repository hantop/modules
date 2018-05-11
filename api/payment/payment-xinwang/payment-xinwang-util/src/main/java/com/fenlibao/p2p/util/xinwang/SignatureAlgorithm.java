package com.fenlibao.p2p.util.xinwang;

public enum SignatureAlgorithm {

	SHA1WithRSA("SHA1WithRSA"),
	
	MD5WithRSA("MD5WithRSA");
	
	private String signAlgorithm;

	private SignatureAlgorithm(String signAlgorithm) {
		this.signAlgorithm = signAlgorithm;
	}

	public String getSignAlgorithm() {
		return signAlgorithm;
	}
}
