package com.fenlibao.p2p.model.vo.creditassignment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 申请债权转让时显示的信息
 */
public class TransferApplyforInfoVO implements Serializable {

	int creditId;//债权ID

	BigDecimal creditCapitalAmount; //债权本金

	BigDecimal passedEarning;//债权已过天数收益

	int surplusDays; //债权剩余天数
	
	String assignmentRate;//债权转让费率
	
	String assignmentAgreement;//债权转让及受让协议
	
	int creditassignmentCount;//债权已申请转让次数
	
	int canAssignmentCount;//债权可转让次数

	public String getAssignmentAgreement() {
		return assignmentAgreement;
	}

	public void setAssignmentAgreement(String assignmentAgreement) {
		this.assignmentAgreement = assignmentAgreement;
	}

	public String getAssignmentRate() {
		return assignmentRate;
	}

	public void setAssignmentRate(String assignmentRate) {
		this.assignmentRate = assignmentRate;
	}

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

	public BigDecimal getCreditCapitalAmount() {
		return creditCapitalAmount;
	}

	public void setCreditCapitalAmount(BigDecimal creditCapitalAmount) {
		this.creditCapitalAmount = creditCapitalAmount;
	}

	public int getCreditId() {
		return creditId;
	}

	public void setCreditId(int creditId) {
		this.creditId = creditId;
	}


	public BigDecimal getPassedEarning() {
		return passedEarning;
	}

	public void setPassedEarning(BigDecimal passedEarning) {
		this.passedEarning = passedEarning;
	}

	public int getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(int surplusDays) {
		this.surplusDays = surplusDays;
	}
}
