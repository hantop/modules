package com.fenlibao.p2p.model.vo;

import com.fenlibao.p2p.model.vo.creditassignment.ApplyforVO;

public class TransferOutVO extends PagerVO<ApplyforVO> {

	private static final long serialVersionUID = 1L;
	
	private long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
