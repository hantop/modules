package com.fenlibao.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

public class PlatformInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private long platformSum;//平台投资总额
	
	private long platformEaring;//累计收益
	
	private List<ProductInfoVo> prosList;//产品

	public long getPlatformSum() {
		return platformSum;
	}

	public void setPlatformSum(long platformSum) {
		this.platformSum = platformSum;
	}

	public long getPlatformEaring() {
		return platformEaring;
	}

	public void setPlatformEaring(long platformEaring) {
		this.platformEaring = platformEaring;
	}

	public List<ProductInfoVo> getProsList() {
		return prosList;
	}

	public void setProsList(List<ProductInfoVo> prosList) {
		this.prosList = prosList;
	}
	
}
