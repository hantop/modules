package com.fenlibao.platform.service.queqianme;

import com.fenlibao.platform.model.queqianme.*;

public interface QueqianmeService {
    /**
     * 判断serialNum是否重复
     *
     * @param serialNum
     * @return
     */
    int getSerialNumCount(String serialNum);

    /**
     * 判断协议文件唯一流水号是否重复
     *
     * @param serialNum
     * @return
     */
    int getFileSerialNum(String serialNum);

    /**
     * 插入标数据
     *
     * @param bidinfo
     */
    int addbidinfo(Bidinfo bidinfo);

    /**
     * 插入标附件
     *
     * @param bidextralinfo
     */
    void addextrainfo(Bidextralinfo bidextralinfo);

    /**
     * 写日志
     */
    void writeLog(int accountId, String operateType, String operateDescription, String remoteIP);

    /**
     * 判断serialNum是否上传过三方协议
     *
     * @param serialNum
     * @return
     */
    int getSerialNumCountForTripleAgreement(String serialNum);

    /**
     * 插入三方协议数据
     *
     * @param tripleAgreementinfo
     */
    void addTripleAgreementinfo(TripleAgreementinfo tripleAgreementinfo);


    /**
     * 插入借款人基本信息
     * @param baseInfo
     */
    void addBaseInfo(BaseInfo baseInfo);

    /**
     * 插入借款人工作信息
     * @param workInfo
     */
    void addWorkInfo(WorkInfo workInfo);

    /**
     * 插入多头借贷信息
     * @param mutiplyBorrow
     */
    void addMutiplyBorrow(MutiplyBorrow mutiplyBorrow);

    /**
     * 插入反欺诈信息
     * @param antiFraud
     */
    void addAntiFraud(AntiFraud antiFraud);

    /**
     * 插入借款信息的所有数据
     */
    void addLoanInfo(Bidinfo bidinfo,WorkInfo workInfo,BaseInfo baseInfo,MutiplyBorrow mutiplyBorrow,AntiFraud antiFraud,RiskMutiply riskMutiply)throws Exception;


    /**
     * 插入审核信息
     * @param riskMutiply
     */
    void addRiskMutiply(RiskMutiply riskMutiply);
}
