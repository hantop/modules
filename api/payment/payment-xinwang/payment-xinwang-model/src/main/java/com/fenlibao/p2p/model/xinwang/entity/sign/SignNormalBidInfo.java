package com.fenlibao.p2p.model.xinwang.entity.sign;

import java.util.Date;

/**
 * @author zeronx on 2017/12/13 16:53.
 * @version 1.0
 */
public class SignNormalBidInfo {
    private Integer bid; // 标id
    private String noSensitiveAgreement; // 脱敏合同文件
    private String sensitiveAgreement; // 完整合同文件
    private Integer createAgreementStatus; // 合同文件生成状态(1:已生成; 0:未生成)
    private String sensitiveSignId; // 没有脱敏上传文档到上上签成功回传的签名文档标识
    private String sensitiveDocId; // 没有脱敏上传文档到上上签成功回传的签名文档存储编号
    private Integer sensitiveIsUpload; // 没有脱敏本地文档上传到上上签是否成功。0：否、1：是
    private String sensitiveSignStatus; // 没有脱敏的文档签名状态（WQM:未签名，QMZ:签名中，YQM:已签名，QMSB:签名失败）
    private String noSensitiveSignId; // 脱敏上传文档到上上签成功回传的签名文档标识
    private String noSensitiveDocId; // 脱敏上传文档到上上签成功回传的签名文档存储编号
    private Integer noSensitiveIsUpload; // 脱敏本地文档上传到上上签是否成功。0：否、1：是
    private String noSensitiveSignStatus; // 脱敏的文档签名状态（WQM:未签名，QMZ:签名中，YQM:已签名，QMSB:签名失败）
    private String sensitiveDocument; // 从上上签下载没有脱敏的文档保存到本地的文件名包括后缀
    private Integer sensitiveIsDownload; // 没有脱敏的文档从上上签下载到本地是否成功。0：否、1：是
    private String noSensitiveDocument; // 从上上签下载已脱敏的文档保存到本地的文件名包括后缀
    private Integer noSensitiveIsDownload; // 已脱敏的文档从上上签下载到本地是否成功。0：否、1：是
    private String investorsPdf; // 所有投资人名单文件pdf
    private String investorsPdfMd5; // 所有投资人名单文件md5值(防止篡改)
    private Integer sensitiveInvestorSuc; // 没有脱敏团签是否成功；0：否、1：是
    private Integer noSensitiveInvestorSuc; // 已脱敏团签是否成功；0：否、1：是
    private Integer operateStatus; // 0:文档没有上传（或可能已上传某一份：脱敏或不脱敏）、1：文档都已上传到上上签但没有签名（或可能已签名某一份）、2：文档都已签名成功但没有下载（或可能已下载某一份）、3：所有操作都已完成
    private Integer noSensitiveIsClose; // 脱敏文档是否已成功关闭 0：否 ，1：是
    private Integer sensitiveIsClose; // 没有脱敏文档是否已成功关闭 0：否 ，1：是
    private Date createTime; // 记录创建时间
    private Date updateTime; // 记录更新时间

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
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

    public Integer getCreateAgreementStatus() {
        return createAgreementStatus;
    }

    public void setCreateAgreementStatus(Integer createAgreementStatus) {
        this.createAgreementStatus = createAgreementStatus;
    }

    public String getSensitiveSignId() {
        return sensitiveSignId;
    }

    public void setSensitiveSignId(String sensitiveSignId) {
        this.sensitiveSignId = sensitiveSignId;
    }

    public String getSensitiveDocId() {
        return sensitiveDocId;
    }

    public void setSensitiveDocId(String sensitiveDocId) {
        this.sensitiveDocId = sensitiveDocId;
    }

    public Integer getSensitiveIsUpload() {
        return sensitiveIsUpload;
    }

    public void setSensitiveIsUpload(Integer sensitiveIsUpload) {
        this.sensitiveIsUpload = sensitiveIsUpload;
    }

    public String getSensitiveSignStatus() {
        return sensitiveSignStatus;
    }

    public void setSensitiveSignStatus(String sensitiveSignStatus) {
        this.sensitiveSignStatus = sensitiveSignStatus;
    }

    public String getNoSensitiveSignId() {
        return noSensitiveSignId;
    }

    public void setNoSensitiveSignId(String noSensitiveSignId) {
        this.noSensitiveSignId = noSensitiveSignId;
    }

    public String getNoSensitiveDocId() {
        return noSensitiveDocId;
    }

    public void setNoSensitiveDocId(String noSensitiveDocId) {
        this.noSensitiveDocId = noSensitiveDocId;
    }

    public Integer getNoSensitiveIsUpload() {
        return noSensitiveIsUpload;
    }

    public void setNoSensitiveIsUpload(Integer noSensitiveIsUpload) {
        this.noSensitiveIsUpload = noSensitiveIsUpload;
    }

    public String getNoSensitiveSignStatus() {
        return noSensitiveSignStatus;
    }

    public void setNoSensitiveSignStatus(String noSensitiveSignStatus) {
        this.noSensitiveSignStatus = noSensitiveSignStatus;
    }

    public String getSensitiveDocument() {
        return sensitiveDocument;
    }

    public void setSensitiveDocument(String sensitiveDocument) {
        this.sensitiveDocument = sensitiveDocument;
    }

    public Integer getSensitiveIsDownload() {
        return sensitiveIsDownload;
    }

    public void setSensitiveIsDownload(Integer sensitiveIsDownload) {
        this.sensitiveIsDownload = sensitiveIsDownload;
    }

    public String getNoSensitiveDocument() {
        return noSensitiveDocument;
    }

    public void setNoSensitiveDocument(String noSensitiveDocument) {
        this.noSensitiveDocument = noSensitiveDocument;
    }

    public Integer getNoSensitiveIsDownload() {
        return noSensitiveIsDownload;
    }

    public void setNoSensitiveIsDownload(Integer noSensitiveIsDownload) {
        this.noSensitiveIsDownload = noSensitiveIsDownload;
    }

    public String getInvestorsPdf() {
        return investorsPdf;
    }

    public void setInvestorsPdf(String investorsPdf) {
        this.investorsPdf = investorsPdf;
    }

    public String getInvestorsPdfMd5() {
        return investorsPdfMd5;
    }

    public void setInvestorsPdfMd5(String investorsPdfMd5) {
        this.investorsPdfMd5 = investorsPdfMd5;
    }

    public Integer getSensitiveInvestorSuc() {
        return sensitiveInvestorSuc;
    }

    public void setSensitiveInvestorSuc(Integer sensitiveInvestorSuc) {
        this.sensitiveInvestorSuc = sensitiveInvestorSuc;
    }

    public Integer getNoSensitiveInvestorSuc() {
        return noSensitiveInvestorSuc;
    }

    public void setNoSensitiveInvestorSuc(Integer noSensitiveInvestorSuc) {
        this.noSensitiveInvestorSuc = noSensitiveInvestorSuc;
    }

    public Integer getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(Integer operateStatus) {
        this.operateStatus = operateStatus;
    }

    public Integer getNoSensitiveIsClose() {
        return noSensitiveIsClose;
    }

    public void setNoSensitiveIsClose(Integer noSensitiveIsClose) {
        this.noSensitiveIsClose = noSensitiveIsClose;
    }

    public Integer getSensitiveIsClose() {
        return sensitiveIsClose;
    }

    public void setSensitiveIsClose(Integer sensitiveIsClose) {
        this.sensitiveIsClose = sensitiveIsClose;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
