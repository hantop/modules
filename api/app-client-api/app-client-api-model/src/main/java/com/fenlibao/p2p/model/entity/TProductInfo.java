package com.fenlibao.p2p.model.entity;

import java.util.Date;

public class TProductInfo {

	private int id;
	
	private String name;
	
	private String distribution;
	
	private String earingsRang;
	
	private int status;
	
	private Date cteateTime;
	
	private int type;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public String getEaringsRang() {
		return earingsRang;
	}

	public void setEaringsRang(String earingsRang) {
		this.earingsRang = earingsRang;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCteateTime() {
		return cteateTime;
	}

	public void setCteateTime(Date cteateTime) {
		this.cteateTime = cteateTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
