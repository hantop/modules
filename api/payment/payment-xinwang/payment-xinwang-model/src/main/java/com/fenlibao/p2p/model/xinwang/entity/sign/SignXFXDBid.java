package com.fenlibao.p2p.model.xinwang.entity.sign;

/**
 * @author zeronx on 2017/12/20 17:12.
 * @version 1.0
 */
public class SignXFXDBid {
    private Integer bid; // 标id
    private String signId; // 上上签文档id
    private Integer sign;//  0：文档没有签名，1：文档已签名',
    private String noSensitiveAgreement; // '脱敏合同文件',
    private String sensitiveAgreement;// '完整合同文件'
    private String signStatus; // 文档签名状态 YQM WQM QMZ

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public String getNoSensitiveAgreement() {
        return noSensitiveAgreement;
    }

    public void setNoSensitiveAgreement(String noSensitiveAgreement) {
        this.noSensitiveAgreement = noSensitiveAgreement;
    }

    public String getSensitiveAgreement() {
        return sensitiveAgreement;
    }

    public void setSensitiveAgreement(String sensitiveAgreement) {
        this.sensitiveAgreement = sensitiveAgreement;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }
}
