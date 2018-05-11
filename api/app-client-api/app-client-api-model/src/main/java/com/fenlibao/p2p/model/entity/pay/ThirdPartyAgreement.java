package com.fenlibao.p2p.model.entity.pay;

/**
 * flb.t_pay_extend
 * @author Administrator
 *
 */
public class ThirdPartyAgreement {
    private Integer userId;
    private String lianlianAgreement;
    private String baofooBindId;
    
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLianlianAgreement() {
		return lianlianAgreement;
	}
	public void setLianlianAgreement(String lianlianAgreement) {
		this.lianlianAgreement = lianlianAgreement;
	}
	public String getBaofooBindId() {
		return baofooBindId;
	}
	public void setBaofooBindId(String baofooBindId) {
		this.baofooBindId = baofooBindId;
	}
    
}
