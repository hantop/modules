package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.util.AES;
import com.fenlibao.platform.common.util.AESForTP;
import com.fenlibao.platform.model.Response;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Created by xiao on 2016/10/23.
 */
public class Bidinfo {

    private static Config config = ConfigFactory.create(Config.class);

    private int id;
    private int orderNum;//排序号
    private int stageNum;//排序号
    private String repaymentType;//借款流水ID(不可重复)
    private String serialNum;//借款流水ID(不可重复)
    private BigDecimal amount;//借款金额（元）
    private int cycle;//借款周期
    private String cycleType = "d";//借款周期类型（天/月）('m'/'d'),默认为'd'
    private String idNo;//用户身份证号
    private String idName;//用户姓名
    private String phoneNum;//用户电话号码
    private String bankcardNo;//银行卡号

    private String agreementSignId;//签名文档编号（合同编号）
    private String agreementDocId;//签名文档存储编号
    private int flbPageNum;//分利宝签名的页面数
    private BigDecimal flbSignX;//分利宝签名的x坐标比例比例，小数点保留3
    private BigDecimal flbSignY;//分利宝签名的y坐标比例比例，小数点保留3
    private int investorPageNum;//投资人签名的页面数
    private BigDecimal investorSignX;//投资人签名的起始x坐标比例比例，小数点保留3
    private BigDecimal investorSignY;//投资人签名的起始y坐标比例比例，小数点保留3

    private String industry;//所属行业
    private String address;//借款人地址
    private int subjectNature;//主体性质
    private String loanUsage;//借款用途
    private String repaymentOrigin;//还款来源




    public Bidinfo() {

    }

    public Bidinfo(int orderNum, String serialNum, BigDecimal amount, int cycle, String cycleType, String idNo, String idName, String phoneNum,String bankcardNo,String industry,String address, String subjectNature, String loanUsage, String repaymentOrigin
    ) {
        this.orderNum = orderNum;
        this.serialNum = serialNum;
        this.amount = amount;
        this.cycle = cycle;
        this.cycleType = cycleType;
        this.idNo = idNo;
        this.idName = idName;
        this.phoneNum = phoneNum;
        this.bankcardNo = bankcardNo;
        this.address = address;
        this.loanUsage = loanUsage;
        this.repaymentOrigin = repaymentOrigin;
        this.subjectNature = Integer.parseInt(subjectNature);
        this.industry = industry;
    }

    public Bidinfo(String orderNum, String stageNum, String serialNum, String amount, String cycle, String cycleType, String idNo, String idName, String phoneNum, String repaymentType,String bankcardNo,String industry,String address, String subjectNature, String loanUsage, String repaymentOrigin) {
        this.serialNum = serialNum;
        this.amount = new BigDecimal(amount);
        this.orderNum = Integer.parseInt(orderNum);
        this.stageNum = Integer.parseInt(stageNum);
        this.cycle = Integer.parseInt(cycle);
        this.cycleType = cycleType;
        this.idNo = idNo;
        this.idName = idName;
        this.phoneNum = phoneNum;
        this.repaymentType = repaymentType;
        this.bankcardNo = bankcardNo;
        this.address = address;
        this.loanUsage = loanUsage;
        this.repaymentOrigin = repaymentOrigin;
        this.subjectNature = Integer.parseInt(subjectNature);
        this.industry = industry;
    }

    public Bidinfo(String orderNum, String stageNum, String serialNum, String amount, String cycle, String cycleType, String idNo, String idName, String phoneNum, String repaymentType, String agreementSignId, String agreementDocId, String flbPageNum, String flbSignX, String flbSignY, String investorPageNum, String investorSignX, String investorSignY,String bankcardNo,String industry,String address, String subjectNature, String loanUsage, String repaymentOrigin) {
        this.serialNum = serialNum;
        this.amount = new BigDecimal(amount);
        this.orderNum = Integer.parseInt(orderNum);
        this.stageNum = Integer.parseInt(stageNum);
        this.cycle = Integer.parseInt(cycle);
        this.cycleType = cycleType;
        this.idNo = idNo;
        this.idName = idName;
        this.phoneNum = phoneNum;
        this.bankcardNo = bankcardNo;
        this.repaymentType = repaymentType;
        this.agreementSignId = agreementSignId;
        this.agreementDocId = agreementDocId;
        this.flbPageNum = Integer.parseInt(flbPageNum);
        this.flbSignX = new BigDecimal(flbSignX).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.flbSignY = new BigDecimal(flbSignY).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.investorPageNum = Integer.parseInt(investorPageNum);
        this.investorSignX = new BigDecimal(investorSignX).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.investorSignY = new BigDecimal(investorSignY).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.address = address;
        this.loanUsage = loanUsage;
        this.repaymentOrigin = repaymentOrigin;
        this.subjectNature = Integer.parseInt(subjectNature);
        this.industry = industry;
    }

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1[3|4|5|7|8][0-9]\\d{8}$";
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    public Response verifyBidinfo(String key) throws Exception {
        if (StringUtils.isBlank(serialNum)
                || orderNum <= 0
                || stageNum <= 0
                || amount.compareTo(BigDecimal.ZERO) == -1
                || cycle <= 0
                || StringUtils.isBlank(repaymentType)
                || StringUtils.isBlank(idNo)
                || StringUtils.isBlank(idName)
                || StringUtils.isBlank(phoneNum)
                || StringUtils.isBlank(bankcardNo)
                ||StringUtils.isBlank(address)
                ||StringUtils.isBlank(repaymentOrigin)
                ||StringUtils.isBlank(loanUsage)
                ||StringUtils.isBlank(industry)
                ||subjectNature<=0) {
            return Response.SYSTEM_EMPTY_PARAMETERS;
        }

        if(subjectNature>3){
            return Response.SYSTEM_ERROR_PARAMETERS;
        }

        String serialNumMaxLength = config.getSerialNumMaxLength();
        if (serialNum.length() > Integer.parseInt(serialNumMaxLength)) {
            return Response.SERIALNUM_PARAMETERS_ERROR;
        }

        String minAmount = config.getLoanMinAmount();
        String maxAmount = config.getLoanMaxAmount();
        if ((amount.compareTo(new BigDecimal(minAmount)) == -1) || (amount.compareTo(new BigDecimal(maxAmount)) == 1)) {
            return Response.AMOUNT_PARAMETERS_ERROR;
        }

        if (StringUtils.isBlank(agreementSignId)
                || StringUtils.isBlank(agreementDocId)
                || flbPageNum <= 0
                || investorPageNum <= 0
                || flbSignX.compareTo(BigDecimal.ZERO) == -1 || flbSignX.compareTo(BigDecimal.ONE) == 1
                || flbSignY.compareTo(BigDecimal.ZERO) == -1 || flbSignY.compareTo(BigDecimal.ONE) == 1
                || investorSignX.compareTo(BigDecimal.ZERO) == -1 || investorSignX.compareTo(BigDecimal.ONE) == 1
                || investorSignY.compareTo(BigDecimal.ZERO) == -1 || investorSignY.compareTo(BigDecimal.ONE) == 1
                ) {
            return Response.AGREEMENT_PARAMETERS_ERROR;
        }

        String agreementIdMaxLength = config.getAgreementIdMaxLength();
        if (agreementSignId.length() > Integer.parseInt(agreementIdMaxLength)
                || agreementDocId.length() > Integer.parseInt(agreementIdMaxLength)) {
            return Response.LOAN_AGREEMENT_ID_ERROR;
        }

        //解密
        try {
            idName = AESForTP.decode(key, idName);
            idNo = AESForTP.decode(key, idNo);
            phoneNum = AESForTP.decode(key, phoneNum);
            bankcardNo = AESForTP.decode(key,bankcardNo);
        } catch (Exception e) {
            return Response.LOAN_DECODE_ERROR;
        }

        if (!Pattern.matches(REGEX_MOBILE, phoneNum) || !Pattern.matches(REGEX_ID_CARD, idNo)) {
            return Response.IDCARD_PARAMETERS_ERROR;
        }

        if (!"m".equalsIgnoreCase(cycleType) && !"d".equalsIgnoreCase(cycleType)) {
            return Response.CYCLETYPE_PARAMETERS_ERROR;
        }

        Response repaymentTypeResponse = checkRepaymentType(repaymentType);
        if (repaymentTypeResponse != Response.RESPONSE_SUCCESS) {
            return repaymentTypeResponse;
        }

        //身份证加密
        idNo = AES.getInstace().encrypt(idNo);
        bankcardNo = AES.getInstace().encrypt(bankcardNo);
        return Response.RESPONSE_SUCCESS;
    }

    /**
     * 还款方式校验
     *
     * @param repaymentType
     * @return
     */
    private Response checkRepaymentType(String repaymentType) {
        if ("YCFQ".equals(repaymentType)) {
            return Response.RESPONSE_SUCCESS;
        } else if ("MYFX".equals(repaymentType) || "DEBX".equals(repaymentType) || "DEBJ".equals(repaymentType)) {
            return Response.REPAYMENT_UNSUPPORT_ERROR;
        } else {
            return Response.REPAYMENT_ERROR;
        }
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getStageNum() {
        return stageNum;
    }

    public void setStageNum(int stageNum) {
        this.stageNum = stageNum;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getAgreementSignId() {
        return agreementSignId;
    }

    public void setAgreementSignId(String agreementSignId) {
        this.agreementSignId = agreementSignId;
    }

    public String getAgreementDocId() {
        return agreementDocId;
    }

    public void setAgreementDocId(String agreementDocId) {
        this.agreementDocId = agreementDocId;
    }

    public int getFlbPageNum() {
        return flbPageNum;
    }

    public void setFlbPageNum(int flbPageNum) {
        this.flbPageNum = flbPageNum;
    }

    public BigDecimal getFlbSignX() {
        return flbSignX;
    }

    public void setFlbSignX(BigDecimal flbSignX) {
        this.flbSignX = flbSignX;
    }

    public BigDecimal getFlbSignY() {
        return flbSignY;
    }

    public void setFlbSignY(BigDecimal flbSignY) {
        this.flbSignY = flbSignY;
    }

    public int getInvestorPageNum() {
        return investorPageNum;
    }

    public void setInvestorPageNum(int investorPageNum) {
        this.investorPageNum = investorPageNum;
    }

    public BigDecimal getInvestorSignX() {
        return investorSignX;
    }

    public void setInvestorSignX(BigDecimal investorSignX) {
        this.investorSignX = investorSignX;
    }

    public BigDecimal getInvestorSignY() {
        return investorSignY;
    }

    public void setInvestorSignY(BigDecimal investorSignY) {
        this.investorSignY = investorSignY;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLoanUsage() {
        return loanUsage;
    }

    public void setLoanUsage(String loanUsage) {
        this.loanUsage = loanUsage;
    }

    public String getRepaymentOrigin() {
        return repaymentOrigin;
    }

    public void setRepaymentOrigin(String repaymentOrigin) {
        this.repaymentOrigin = repaymentOrigin;
    }

    public int getSubjectNature() {
        return subjectNature;
    }

    public void setSubjectNature(int subjectNature) {
        this.subjectNature = subjectNature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
