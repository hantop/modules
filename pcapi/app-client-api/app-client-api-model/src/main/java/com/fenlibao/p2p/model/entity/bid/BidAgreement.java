package com.fenlibao.p2p.model.entity.bid;

import java.util.List;

/**
 * 电子签章合同字段
 */
public class BidAgreement {

	private int bidId;

	private String sensitiveSignId;//脱敏signId

	private String sensitiveDocId; //脱敏DocId
	
	private String noSensitiveSignId;//未脱敏signId
	
	private String noSensitiveDocId; //未脱敏DocId


	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public String getNoSensitiveDocId() {
		return noSensitiveDocId;
	}

	public void setNoSensitiveDocId(String noSensitiveDocId) {
		this.noSensitiveDocId = noSensitiveDocId;
	}

	public String getNoSensitiveSignId() {
		return noSensitiveSignId;
	}

	public void setNoSensitiveSignId(String noSensitiveSignId) {
		this.noSensitiveSignId = noSensitiveSignId;
	}

	public String getSensitiveDocId() {
		return sensitiveDocId;
	}

	public void setSensitiveDocId(String sensitiveDocId) {
		this.sensitiveDocId = sensitiveDocId;
	}

	public String getSensitiveSignId() {
		return sensitiveSignId;
	}

	public void setSensitiveSignId(String sensitiveSignId) {
		this.sensitiveSignId = sensitiveSignId;
	}
}
