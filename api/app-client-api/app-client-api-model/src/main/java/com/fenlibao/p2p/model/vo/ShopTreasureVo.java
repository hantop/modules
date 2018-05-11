package com.fenlibao.p2p.model.vo;

public class ShopTreasureVo {

	private int kdbPlantId;//开店宝计划id
	
	private String kdbPlanTitle;//开店宝计划标题
	
	private int kdbType;//开店宝计划类型
	
	private String kdbYield;//开店宝收益率
	
	private double kdbSum;//开店宝申购总额
	
	private double buySum;//开店宝已申购总额
	
	/**
	 *  1、正在申购的产品，显示：可申购
		2、计划金额已满，封标日期未到，显示：已满额
		3、计划金额未满，封标日期已到，显示：锁定期
		4、已放款成功，显示：收益中
		5、到期退出，显示：已退出
	 */
	private int kdbStatus;//开店宝状态{1:可申购;  2: 已满额;  3: 锁定期  4: 收益中  5: 已退出}
	
	private int kdbUserStatus;//用户是否申购该期的开店宝计划
	
	private String timestamp;//筹款到期时间
	
    private int isNoviceBid;//是否是新手标 (1:是新手标;0:普通开店宝标)
	
	private int loanDays;//借款周期（单位：天）
	
	private String[] assetTypes;//资产类型（信用认证、实地认证、抵押担保）
	
	public int getKdbPlantId() {
		return kdbPlantId;
	}

	public void setKdbPlantId(int kdbPlantId) {
		this.kdbPlantId = kdbPlantId;
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

	public double getBuySum() {
		return buySum;
	}

	public void setBuySum(double buySum) {
		this.buySum = buySum;
	}

	public int getKdbStatus() {
		return kdbStatus;
	}

	public void setKdbStatus(int kdbStatus) {
		this.kdbStatus = kdbStatus;
	}

	public String getKdbPlanTitle() {
		return kdbPlanTitle;
	}

	public void setKdbPlanTitle(String kdbPlanTitle) {
		this.kdbPlanTitle = kdbPlanTitle;
	}

	public int getKdbUserStatus() {
		return kdbUserStatus;
	}

	public void setKdbUserStatus(int kdbUserStatus) {
		this.kdbUserStatus = kdbUserStatus;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getIsNoviceBid() {
		return isNoviceBid;
	}

	public void setIsNoviceBid(int isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	public int getLoanDays() {
		return loanDays;
	}

	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public String[] getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(String[] assetTypes) {
		this.assetTypes = assetTypes;
	}
	
}
