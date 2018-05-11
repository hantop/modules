package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.model.Response;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by xiao on 2016/11/08.
 */
public class TripleAgreementinfo {

    private static Config config = ConfigFactory.create(Config.class);

    private String serialNum;//借款流水ID(不可重复)
    private String agreementSignId;//签名文档编号（合同编号）
    private String agreementDocId;//签名文档存储编号
    private int flbPageNum;//分利宝签名的页面数
    private BigDecimal flbSignX;//分利宝签名的x坐标比例比例，小数点保留3
    private BigDecimal flbSignY;//分利宝签名的y坐标比例比例，小数点保留3

    public TripleAgreementinfo() {

    }

    public TripleAgreementinfo(String serialNum, String agreementSignId, String agreementDocId, int flbPageNum, BigDecimal flbSignX, BigDecimal flbSignY) {
        this.serialNum = serialNum;
        this.agreementSignId = agreementSignId;
        this.agreementDocId = agreementDocId;
        this.flbPageNum = flbPageNum;
        this.flbSignX = flbSignX;
        this.flbSignY = flbSignY;
    }

    public TripleAgreementinfo(String serialNum, String agreementSignId, String agreementDocId, String flbPageNum, String flbSignX, String flbSignY) {
        this.serialNum = serialNum;
        this.agreementSignId = agreementSignId;
        this.agreementDocId = agreementDocId;
        this.flbPageNum = Integer.parseInt(flbPageNum);
        this.flbSignX = new BigDecimal(flbSignX).setScale(3, BigDecimal.ROUND_HALF_UP);
        this.flbSignY = new BigDecimal(flbSignY).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public Response verifyTripleAgreementinfo() throws Exception {
        if (StringUtils.isBlank(serialNum)) {
            return Response.SYSTEM_EMPTY_PARAMETERS;
        }

        String serialNumMaxLength = config.getSerialNumMaxLength();
        if (serialNum.length() > Integer.parseInt(serialNumMaxLength)) {
            return Response.SERIALNUM_PARAMETERS_ERROR;
        }

        if (StringUtils.isBlank(agreementSignId)
                || StringUtils.isBlank(agreementDocId)
                || flbPageNum <= 0
                || flbSignX.compareTo(BigDecimal.ZERO) == -1 || flbSignX.compareTo(BigDecimal.ONE) == 1
                || flbSignY.compareTo(BigDecimal.ZERO) == -1 || flbSignY.compareTo(BigDecimal.ONE) == 1
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

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        TripleAgreementinfo.config = config;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
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
}
