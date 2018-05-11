package com.fenlibao.p2p.model.xinwang.entity.trade;

/**
 * 交易类型
 * Created by chenzhixuan on 2015/10/21.
 */
public class BusinessType {
    private int code;// 交易类型编码
    private String name;// 类型名称
    private String status;// 状态,QY:启用;TY:停用
    
    public BusinessType() {
		super();
	}

	public BusinessType(int code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
