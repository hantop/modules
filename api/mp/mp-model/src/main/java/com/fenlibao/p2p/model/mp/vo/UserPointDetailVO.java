package com.fenlibao.p2p.model.mp.vo;

public class UserPointDetailVO {
	
	public String pName; //积分类型名称
	
	public String pLogo; //积分logo
	
	public int pChangeType; //积分变动类型(1:收入;2:支出)

	public int pNum; //积分数量
		
	public long createTime; // 创建时间
	
	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpLogo() {
		return pLogo;
	}

	public void setpLogo(String pLogo) {
		this.pLogo = pLogo;
	}

	public int getpNum() {
		return pNum;
	}

	public void setpNum(int pNum) {
		this.pNum = pNum;
	}

	public int getpChangeType() {
		return pChangeType;
	}

	public void setpChangeType(int pChangeType) {
		this.pChangeType = pChangeType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
