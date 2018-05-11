package com.fenlibao.p2p.model.vo.creditassignment;

import java.io.Serializable;

/**
 * 申请债权转让时显示的信息
 */
public class ApplyforInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int zqId;//债权ID
	
	private String assignmentRate;//债权转让费率
	
	private String assignmentAgreement;//债权转让及受让协议
	
	private int creditassignmentCount;//债权已申请转让次数
	
	private int canAssignmentCount;//债权可转让次数

	public int getCanAssignmentCount() {
		return canAssignmentCount;
	}

	public void setCanAssignmentCount(int canAssignmentCount) {
		this.canAssignmentCount = canAssignmentCount;
	}

	public int getCreditassignmentCount() {
		return creditassignmentCount;
	}

	public void setCreditassignmentCount(int creditassignmentCount) {
		this.creditassignmentCount = creditassignmentCount;
	}

	public int getZqId() {
		return zqId;
	}

	public void setZqId(int zqId) {
		this.zqId = zqId;
	}

	public String getAssignmentRate() {
		return assignmentRate;
	}

	public void setAssignmentRate(String assignmentRate) {
		this.assignmentRate = assignmentRate;
	}

	public String getAssignmentAgreement() {
		return assignmentAgreement;
	}

	public void setAssignmentAgreement(String assignmentAgreement) {
		this.assignmentAgreement = assignmentAgreement;
	}
	
}
