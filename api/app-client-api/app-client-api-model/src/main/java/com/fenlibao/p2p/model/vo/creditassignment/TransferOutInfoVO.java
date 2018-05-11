/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInListVO.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 上午11:20:39 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.vo.creditassignment;

/**
 * @ClassName: TransferInListVO
 * @author: laubrence
 * @date: 2015-10-23 上午11:20:39
 */
public class TransferOutInfoVO {

	int applyforId; //转让申请id

	String transferTitle;//转让标题

	double transferOutValue;//转出价格

	double yearYield;//标的年化收益率

	int	surplusDays;//债权剩余天数

	double discountRate;//债权转让折扣率

	String[] assetTypes;//标的资产类型

	long timestamp;//转让申请时间

    String repaymentMode;

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public int getApplyforId() {
        return applyforId;
    }

    public void setApplyforId(int applyforId) {
        this.applyforId = applyforId;
    }

    public String[] getAssetTypes() {
        return assetTypes;
    }

    public void setAssetTypes(String[] assetTypes) {
        this.assetTypes = assetTypes;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(int surplusDays) {
        this.surplusDays = surplusDays;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTransferOutValue() {
        return transferOutValue;
    }

    public void setTransferOutValue(double transferOutValue) {
        this.transferOutValue = transferOutValue;
    }

    public String getTransferTitle() {
        return transferTitle;
    }

    public void setTransferTitle(String transferTitle) {
        this.transferTitle = transferTitle;
    }

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
    }
}
