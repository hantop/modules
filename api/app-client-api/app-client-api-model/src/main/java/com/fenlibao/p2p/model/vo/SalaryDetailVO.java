package com.fenlibao.p2p.model.vo;
import java.io.Serializable;

/**
 * 薪金宝信息结果
 */
public class SalaryDetailVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private int xjbId;//薪金宝计划id
		
	private String xjbTitle;//薪金宝的title
	
	private double xjbYield;//薪金宝年化收益率
	
	private int xjbTime;//薪金宝产品期限
	
	private int investDay;//薪金宝计划投资日
	
	private String proUrl;//产品说明书
	
	private String serviceAgreement;//薪金宝服务协议url
	
	private String[] contractUrl; //薪金宝合同url

	public int getXjbId() {
		return xjbId;
	}

	public void setXjbId(int xjbId) {
		this.xjbId = xjbId;
	}

	public String getXjbTitle() {
		return xjbTitle;
	}

	public void setXjbTitle(String xjbTitle) {
		this.xjbTitle = xjbTitle;
	}

	public int getXjbTime() {
		return xjbTime;
	}

	public void setXjbTime(int xjbTime) {
		this.xjbTime = xjbTime;
	}

	public int getInvestDay() {
		return investDay;
	}

	public void setInvestDay(int investDay) {
		this.investDay = investDay;
	}

	public String getProUrl() {
		return proUrl;
	}

	public void setProUrl(String proUrl) {
		this.proUrl = proUrl;
	}


	public String[] getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String[] contractUrl) {
		this.contractUrl = contractUrl;
	}

	public double getXjbYield() {
		return xjbYield;
	}

	public void setXjbYield(double xjbYield) {
		this.xjbYield = xjbYield;
	}

	public String getServiceAgreement() {
		return serviceAgreement;
	}

	public void setServiceAgreement(String serviceAgreement) {
		this.serviceAgreement = serviceAgreement;
	}

}
