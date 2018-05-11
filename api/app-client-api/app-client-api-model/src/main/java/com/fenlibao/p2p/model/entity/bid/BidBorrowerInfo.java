package com.fenlibao.p2p.model.entity.bid;

/** 
 * @Description: 标对应借款人详细信息
 * @author: junda.feng
 */
public class BidBorrowerInfo{

	private Integer id; //借款人id
	private Integer bid; //标id
	private Integer user_id; //借款人id
	private String borrower_info; //借款人描述信息
	private String credit; //征信信息
	private String bank_transaction; //银行流水
	private String risk; //风控审核项目

	private String operator;  //经办人
	private String realName;  // 真实借款人姓名
	private String realCreditCardNum;  //真实借款人身份证号（加密存储）
	private String realPhone;  //真实借款人手机
	private String industry;   //所属行业
	private String address;    //地址
	private Integer subjectNature;   //主体性质 1自然人, 2法人, 3其他组织机构
	private String otherBorrowerInfo;   //其他借款信息


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getBorrower_info() {
		return borrower_info;
	}

	public void setBorrower_info(String borrower_info) {
		this.borrower_info = borrower_info;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getBank_transaction() {
		return bank_transaction;
	}

	public void setBank_transaction(String bank_transaction) {
		this.bank_transaction = bank_transaction;
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

	public Integer getSubjectNature() {
		return subjectNature;
	}

	public void setSubjectNature(Integer subjectNature) {
		this.subjectNature = subjectNature;
	}

	public String getOtherBorrowerInfo() {
		return otherBorrowerInfo;
	}

	public void setOtherBorrowerInfo(String otherBorrowerInfo) {
		this.otherBorrowerInfo = otherBorrowerInfo;
	}
}
