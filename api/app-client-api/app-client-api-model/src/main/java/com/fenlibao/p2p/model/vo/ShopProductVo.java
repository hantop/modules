package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 开店宝产品明细
 */
public class ShopProductVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int kdbPlantId;//开店宝计划id
	
	private String kdbPlanTitle;//开店宝计划title
	
	private int kdbType;//开店宝计划类型0:3个月,1:6个月,2:12个月
	
	private String kdbYield;//开店宝收益率
	
	private double kdbSum;//开店宝申购总额
	
	private String proUrl;//产品说明书url
	
	private String serviceAgreement;//开店宝服务协议url
	
	private String[] contractUrl;//开店宝合同url
	
	private int isNoviceBid;//是否是新手标 (1:是新手标;0:普通开店宝标)
	
	private int loanDays;//借款周期（单位：天）

	public int getKdbPlantId() {
		return kdbPlantId;
	}

	public void setKdbPlantId(int kdbPlantId) {
		this.kdbPlantId = kdbPlantId;
	}

	public String getKdbPlanTitle() {
		return kdbPlanTitle;
	}

	public void setKdbPlanTitle(String kdbPlanTitle) {
		this.kdbPlanTitle = kdbPlanTitle;
	}

	public int getKdbType() {
		return kdbType;
	}

	public void setKdbType(int kdbType) {
		this.kdbType = kdbType;
	}

	public String getKdbYield() {
		return kdbYield;
	}

	public void setKdbYield(String kdbYield) {
		this.kdbYield = kdbYield;
	}

	public double getKdbSum() {
		return kdbSum;
	}

	public void setKdbSum(double kdbSum) {
		this.kdbSum = kdbSum;
	}

	public String getProUrl() {
		return proUrl;
	}

	public void setProUrl(String proUrl) {
		this.proUrl = proUrl;
	}

	public String getServiceAgreement() {
		return serviceAgreement;
	}

	public void setServiceAgreement(String serviceAgreement) {
		this.serviceAgreement = serviceAgreement;
	}

	public String[] getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String[] contractUrl) {
		this.contractUrl = contractUrl;
	}

	/**
	 * @return the isNoviceBid
	 */
	public int getIsNoviceBid() {
		return isNoviceBid;
	}

	/**
	 * @param isNoviceBid the isNoviceBid to set
	 */
	public void setIsNoviceBid(int isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	/**
	 * @return the loanDays
	 */
	public int getLoanDays() {
		return loanDays;
	}

	/**
	 * @param loanDays the loanDays to set
	 */
	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}
	
}
