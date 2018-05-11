package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件信息表(s1._1050)
 */
public class AccessoryInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;//ID
	
	private Date createTime;//创建时间
	
	private int type;//附件类型
	
	private String suffix;//后缀名

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
