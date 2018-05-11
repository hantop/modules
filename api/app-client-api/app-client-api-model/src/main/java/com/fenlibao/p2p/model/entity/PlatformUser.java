package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 存管用户请求流水，用户编号
 */
public class PlatformUser implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String requestNo;//请求流水

	private String platformUserNo;//存管用户编号

	public String getPlatformUserNo() {
		return platformUserNo;
	}

	public void setPlatformUserNo(String platformUserNo) {
		this.platformUserNo = platformUserNo;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
