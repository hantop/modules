package com.fenlibao.platform.model;

import com.fenlibao.platform.common.config.Config;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/7/29.
 */
public class BusinessAgreement {

    private static Config config = ConfigFactory.create(Config.class);

    private int id;
    private int businessId;
    private String agreementSignId;//签名文档编号（合同编号）
    private String agreementDocId;//签名文档存储编号
    private int flbPageNum;//分利宝签名的页面数
    private BigDecimal flbSignX;//分利宝签名的x坐标比例比例，小数点保留3
    private BigDecimal flbSignY;//分利宝签名的y坐标比例比例，小数点保留3
    private int loanPageNum;//借款人签名的页面数
    private BigDecimal loanSignX;//借款人签名的起始x坐标比例比例，小数点保留3
    private BigDecimal loanSignY;//借款人签名的起始y坐标比例比例，小数点保留3
    private int xinwangPageNum;//新网签名的页面数
    private BigDecimal xinwangSignX;//新网签名的起始x坐标比例比例，小数点保留3
    private BigDecimal xinwangSignY;//新网签名的起始y坐标比例比例，小数点保留3
    private String agreementNumber;

    private Integer userId;
    private int agreementType;

    public BusinessAgreement() {

    }

    public BusinessAgreement(String agreementDocId, String agreementSignId,String flbPageNum, String flbSignX, String flbSignY, String loanPageNum, String loanSignX, String loanSignY,String agreementNumber) {
        this.agreementDocId = agreementDocId;
        this.agreementSignId = agreementSignId;
        this.flbPageNum =Integer.parseInt(flbPageNum) ;
        this.flbSignX = new BigDecimal(flbSignX).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.flbSignY = new BigDecimal(flbSignY).setScale(3, BigDecimal.ROUND_HALF_UP);
      /*  this.loanPageNum = StringUtils.isEmpty(loanPageNum)?null:Integer.parseInt(loanPageNum) ;
        this.loanSignX = StringUtils.isEmpty(loanSignX)?null:new BigDecimal(loanSignX).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.loanSignY = StringUtils.isEmpty(loanSignY)?null:new BigDecimal(loanSignY).setScale(3, BigDecimal.ROUND_HALF_UP);*/
        this.agreementNumber = agreementNumber;
    }

    public Response verifyAgreement() throws Exception {


        if (StringUtils.isBlank(agreementSignId)
                || StringUtils.isBlank(agreementDocId)
                || flbPageNum <= 0
                || flbSignX.compareTo(BigDecimal.ZERO) == -1 || flbSignX.compareTo(BigDecimal.ONE) == 1
                || flbSignY.compareTo(BigDecimal.ZERO) == -1 || flbSignY.compareTo(BigDecimal.ONE) == 1
                ||StringUtils.isBlank(agreementNumber)
                ) {
            return Response.AGREEMENT_PARAMETERS_ERROR;
        }

        String agreementIdMaxLength = config.getAgreementIdMaxLength();
        if (agreementSignId.length() > Integer.parseInt(agreementIdMaxLength)
                || agreementDocId.length() > Integer.parseInt(agreementIdMaxLength)) {
            return Response.LOAN_AGREEMENT_ID_ERROR;
        }

        return Response.RESPONSE_SUCCESS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgreementDocId() {
        return agreementDocId;
    }

    public void setAgreementDocId(String agreementDocId) {
        this.agreementDocId = agreementDocId;
    }

    public String getAgreementSignId() {
        return agreementSignId;
    }

    public void setAgreementSignId(String agreementSignId) {
        this.agreementSignId = agreementSignId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
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

    public int getLoanPageNum() {
        return loanPageNum;
    }

    public void setLoanPageNum(int loanPageNum) {
        this.loanPageNum = loanPageNum;
    }

    public BigDecimal getLoanSignX() {
        return loanSignX;
    }

    public void setLoanSignX(BigDecimal loanSignX) {
        this.loanSignX = loanSignX;
    }

    public BigDecimal getLoanSignY() {
        return loanSignY;
    }

    public void setLoanSignY(BigDecimal loanSignY) {
        this.loanSignY = loanSignY;
    }

    public int getXinwangPageNum() {
        return xinwangPageNum;
    }

    public void setXinwangPageNum(int xinwangPageNum) {
        this.xinwangPageNum = xinwangPageNum;
    }

    public BigDecimal getXinwangSignX() {
        return xinwangSignX;
    }

    public void setXinwangSignX(BigDecimal xinwangSignX) {
        this.xinwangSignX = xinwangSignX;
    }

    public BigDecimal getXinwangSignY() {
        return xinwangSignY;
    }

    public void setXinwangSignY(BigDecimal xinwangSignY) {
        this.xinwangSignY = xinwangSignY;
    }

    public int getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(int agreementType) {
        this.agreementType = agreementType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }
}
