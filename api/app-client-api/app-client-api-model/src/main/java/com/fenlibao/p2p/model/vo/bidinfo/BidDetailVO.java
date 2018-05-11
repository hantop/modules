package com.fenlibao.p2p.model.vo.bidinfo;

import com.fenlibao.p2p.model.global.InterfaceConst;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 标的详情
 */
public class BidDetailVO extends BidInfoVO {
	private static final long serialVersionUID = 1L;
	private String precisionTitle;//标的全称（含数字编码）
	private String repaymentMode;//还款方式
	private int purchasedStatus; //购买状态
	private String fwxyUrl; //服务协议
	private long buyTimestamp;//购买时间
	private long interestTimestamp;//计息时间
	private long endTimestamp;//到期时间
	private String remark;//借款描述
	private String lawFileUrl;//法律文件HTML页面URL
	private String[] lawFiles;//法律文件链接
    private double purchaseTotal;//总申购额度
	private double purchasedTotal;//已申购额度
	private String borrowerUrl;//借款人信息html页面url
	private List<BidExtendGroupVO> groupInfoList; //标扩展信息
	private String noviceBIdlimit; //新手标额度
	private String progressRate = "0%";//投资进度
	private String guaranteeFileUrl;//担保借款合同
	private String interest;//加息利率
	private int isCG;//是否是存管类型的标 1：是 0：不是
	private int itemType;//区分数组的item类型(0:标  1:计划)
	private String base; //借款人描述信息
	private String credit; //征信信息
	private String bankTransaction; //银行流水
	private String risk=""; //风控审核项目
	private boolean historyInfoFlag;//不是2.0之前的数据

	private List<Map<String, Object>> projectInfo; //标描述
	private List<Map<String, Object>> borrowerInfo; //借款人信息
	private String agreementUrl;  //担保合同链接

	private String recruitmentPeriod;  //募集期
	private String riskAnnounceUrl;  //网络借贷风险、禁止性行为提示及资金来源合法承诺链接

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public void setProgressRate(String progressRate) {
		this.progressRate = progressRate;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getBankTransaction() {
		return bankTransaction;
	}

	public void setBankTransaction(String bankTransaction) {
		this.bankTransaction = bankTransaction;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public boolean isHistoryInfoFlag() {
		return historyInfoFlag;
	}

	public void setHistoryInfoFlag(boolean historyInfoFlag) {
		this.historyInfoFlag = historyInfoFlag;
	}

	public String getPrecisionTitle() {
		return precisionTitle;
	}

	public void setPrecisionTitle(String precisionTitle) {
		this.precisionTitle = precisionTitle;
	}

	public String getGuaranteeFileUrl() {
		return guaranteeFileUrl;
	}

	public void setGuaranteeFileUrl(String guaranteeFileUrl) {
		this.guaranteeFileUrl = guaranteeFileUrl;
	}

	public int getPurchasedStatus() {
		return purchasedStatus;
	}

	public void setPurchasedStatus(int purchasedStatus) {
		this.purchasedStatus = purchasedStatus;
	}

	public String getFwxyUrl() {
		return fwxyUrl;
	}

	public void setFwxyUrl(String fwxyUrl) {
		this.fwxyUrl = fwxyUrl;
	}

	public long getBuyTimestamp() {
		return buyTimestamp;
	}

	public void setBuyTimestamp(long buyTimestamp) {
		this.buyTimestamp = buyTimestamp;
	}

	public long getInterestTimestamp() {
		return interestTimestamp;
	}

	public void setInterestTimestamp(long interestTimestamp) {
		this.interestTimestamp = interestTimestamp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLawFileUrl() {
		return lawFileUrl;
	}

	public void setLawFileUrl(String lawFileUrl) {
		this.lawFileUrl = lawFileUrl;
	}

	public List<BidExtendGroupVO> getGroupInfoList() {
		return groupInfoList;
	}

	public void setGroupInfoList(List<BidExtendGroupVO> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}

	public double getPurchaseTotal() {
		return purchaseTotal;
	}

	public void setPurchaseTotal(double purchaseTotal) {
		this.purchaseTotal = purchaseTotal;
	}

	public double getPurchasedTotal() {
		return purchasedTotal;
	}

	public void setPurchasedTotal(double purchasedTotal) {
		this.purchasedTotal = purchasedTotal;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String[] getLawFiles() {
		return lawFiles;
	}

	public void setLawFiles(String[] lawFiles) {
		this.lawFiles = lawFiles;
	}

	public String getBorrowerUrl() {
		return borrowerUrl;
	}

	public void setBorrowerUrl(String borrowerUrl) {
		this.borrowerUrl = borrowerUrl;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public String getNoviceBIdlimit() {
		return noviceBIdlimit;
	}

	public void setNoviceBIdlimit(String noviceBIdlimit) {
		this.noviceBIdlimit = noviceBIdlimit;
	}

	public String getProgressRate() {
		return progressRate;
	}

	@Override
	public String getInterest() {
		return interest;
	}

	@Override
	public void setInterest(String interest) {
		this.interest = interest;
	}

	@Override
	public int getIsCG() {
		return isCG;
	}

	@Override
	public void setIsCG(int isCG) {
		this.isCG = isCG;
	}

	@Override
	public int getItemType() {
		return itemType;
	}

	@Override
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public List<Map<String, Object>> getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(List<Map<String, Object>> projectInfo) {
		this.projectInfo = projectInfo;
	}

	public List<Map<String, Object>> getBorrowerInfo() {
		return borrowerInfo;
	}

	public void setBorrowerInfo(List<Map<String, Object>> borrowerInfo) {
		this.borrowerInfo = borrowerInfo;
	}

	public String getAgreementUrl() {
		return agreementUrl;
	}

	public void setAgreementUrl(String agreementUrl) {
		this.agreementUrl = agreementUrl;
	}

	public String getRecruitmentPeriod() {
		return recruitmentPeriod;
	}

	public void setRecruitmentPeriod(String recruitmentPeriod) {
		this.recruitmentPeriod = recruitmentPeriod;
	}

	public String getRiskAnnounceUrl() {
		return riskAnnounceUrl;
	}

	public void setRiskAnnounceUrl(String riskAnnounceUrl) {
		this.riskAnnounceUrl = riskAnnounceUrl;
	}

	/**
	 * @param loanAmount 借款金额
	 * @param surplusAmount 剩余可投金额
	 */
	public void setProgressRate(BigDecimal loanAmount, BigDecimal surplusAmount) {
		BigDecimal progressRate = ((loanAmount.subtract(surplusAmount))
				.divide(loanAmount, 2, BigDecimal.ROUND_FLOOR)
				.multiply(InterfaceConst.BIGDECIMAL_HUNDRED))
				.setScale(0, BigDecimal.ROUND_HALF_UP);
		if (surplusAmount.compareTo(InterfaceConst.BIGDECIMAL_HUNDRED) < 0) {
			progressRate = InterfaceConst.BIGDECIMAL_HUNDRED;
		}
		this.progressRate = progressRate.toString() + "%";
	}
}
