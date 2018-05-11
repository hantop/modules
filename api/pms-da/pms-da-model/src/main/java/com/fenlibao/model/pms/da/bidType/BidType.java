package com.fenlibao.model.pms.da.bidType;

import java.io.Serializable;

/**
 *标的类型(T6211)
 * @author Administrator
 *
 */
public class BidType implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 
     * 自增ID
     */
    public int id;

    /** 
     * 类型名称
     */
    public String typeName;

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public String status;
    
    /**
     * 类型代码
     */
    public String code;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
