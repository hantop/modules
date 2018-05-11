package com.fenlibao.p2p.model.xinwang.entity.bid;


/**
 * 借款人信息,银行流水,以及征信信息对应标
 *
 */
public class TBidExtUser {

	 /**
     * 自增ID
     */
    private int id;
    
    /**
     * 标ID
     */
    private int bid;
    
    /**
     * 借款人ID
     */
    private int userId;
    
    /**
     * 借款人信息(以下信息随发标走,新建表另存)   借款人收入及负债情况
     */
    private String borrowerInfo;
    /**
     * 征信信息
     */
    private String credit;
    /**
     * 银行流水
     */
    private String bankTransaction;
    
    /**
     * 风控审核项目
     */
    private String risk;
    
    /**
     * 经办人
     */
    private String operator;
    
    /**
     * 真实借款人姓名
     */
    private String realName;
    
    /**
     * 真实借款人身份证
     */
    private String realCreditCardNum;
    
    /**
     * 真实借款人手机号
     */
    private String realPhone;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 地址
     */
    private String address;

    /**
     * 主体性质
     */
    private int subjectNature;

    /**
     * 其他借款信息
     */
    private String otherBorrowerInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBorrowerInfo() {
        return borrowerInfo;
    }

    public void setBorrowerInfo(String borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealCreditCardNum() {
        return realCreditCardNum;
    }

    public void setRealCreditCardNum(String realCreditCardNum) {
        this.realCreditCardNum = realCreditCardNum;
    }

    public String getRealPhone() {
        return realPhone;
    }

    public void setRealPhone(String realPhone) {
        this.realPhone = realPhone;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSubjectNature() {
        return subjectNature;
    }

    public void setSubjectNature(int subjectNature) {
        this.subjectNature = subjectNature;
    }

    public String getOtherBorrowerInfo() {
        return otherBorrowerInfo;
    }

    public void setOtherBorrowerInfo(String otherBorrowerInfo) {
        this.otherBorrowerInfo = otherBorrowerInfo;
    }
//获取风控审核项目详细
	/*public Set<String> getRiskSet(String risk) {
		Set<String> riskSet = new TreeSet<>();
		if (risk != null) {
			String[] riskTemp = risk.split("\\|");
			for (int i = 0; i < riskTemp.length; i++) {
				riskSet.add(riskTemp[i]);
			}
			return riskSet;
		}
		return null;
	}
*/
}
