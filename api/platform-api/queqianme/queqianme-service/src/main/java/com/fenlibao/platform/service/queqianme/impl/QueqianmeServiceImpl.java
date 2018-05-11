package com.fenlibao.platform.service.queqianme.impl;


import com.fenlibao.platform.dao.queqianme.QueqianmeMapper;
import com.fenlibao.platform.model.queqianme.*;
import com.fenlibao.platform.service.queqianme.QueqianmeService;

import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class QueqianmeServiceImpl implements QueqianmeService {

    @Inject
    private QueqianmeMapper queqianmeMapper;

    protected static final Logger logger = LoggerFactory.getLogger(QueqianmeServiceImpl.class);

    /**
     * 判断serialNum是否重复
     *
     * @param serialNum
     * @return
     * @throws Exception
     */
    @Override
    public int getSerialNumCount(String serialNum) {
        return queqianmeMapper.getSerialNumCount(serialNum);
    }

    /**
     * 判断协议文件唯一流水号是否重复
     *
     * @param serialNum
     * @return
     * @throws Exception
     */
    @Override
    public int getFileSerialNum(String serialNum) {
        return queqianmeMapper.getFileSerialNum(serialNum);
    }

    /**
     * 插入标数据
     *
     * @param bidinfo
     */
    @Override
    public int addbidinfo(Bidinfo bidinfo) {
       return queqianmeMapper.addbidinfo(bidinfo);
    }

    /**
     * 插入标附件
     *
     * @param bidextralinfo
     */
    @Override
    public void addextrainfo(Bidextralinfo bidextralinfo) {
        queqianmeMapper.addextrainfo(bidextralinfo);
    }

    /**
     * 写日志
     */
    @Override
    public void writeLog(int accountId, String operateType, String operateDescription, String remoteIP) {
        LogInfo logInfo = new LogInfo(accountId, operateType, operateDescription, remoteIP);
        queqianmeMapper.writeLog(logInfo);
    }

    /**
     * 判断serialNum是否上传过三方协议
     *
     * @param serialNum
     * @return
     */
    @Override
    public int getSerialNumCountForTripleAgreement(String serialNum){
        return queqianmeMapper.getSerialNumCountForTripleAgreement(serialNum);
    }

    /**
     * 插入三方协议数据
     *
     * @param tripleAgreementinfo
     */
    @Override
    public void addTripleAgreementinfo(TripleAgreementinfo tripleAgreementinfo){
        queqianmeMapper.addTripleAgreementinfo(tripleAgreementinfo);
    }

    @Override
    public void addAntiFraud(AntiFraud antiFraud) {
        queqianmeMapper.addAntiFraud(antiFraud);
    }

    @Override
    public void addBaseInfo(BaseInfo baseInfo) {
        queqianmeMapper.addBaseInfo(baseInfo);
    }

    @Override
    public void addWorkInfo(WorkInfo workInfo) {
        queqianmeMapper.addWorkInfo(workInfo);
    }

    @Override
    public void addMutiplyBorrow(MutiplyBorrow mutiplyBorrow) {
        queqianmeMapper.addMutiplyBorrow(mutiplyBorrow);
    }

    @Transactional
    @Override
    public void addLoanInfo(Bidinfo bidinfo,WorkInfo workInfo,BaseInfo baseInfo,MutiplyBorrow mutiplyBorrow,AntiFraud antiFraud,RiskMutiply riskMutiply) throws  Exception{
               queqianmeMapper.addbidinfo(bidinfo);
               workInfo.setId(bidinfo.getId());
               baseInfo.setId(bidinfo.getId());
               mutiplyBorrow.setId(bidinfo.getId());
               antiFraud.setId(bidinfo.getId());
               riskMutiply.setId(bidinfo.getId());
               queqianmeMapper.addBaseInfo(baseInfo);
               queqianmeMapper.addAntiFraud(antiFraud);
               queqianmeMapper.addMutiplyBorrow(mutiplyBorrow);
               queqianmeMapper.addWorkInfo(workInfo);
               queqianmeMapper.addRiskMutiply(riskMutiply);
    }

    @Override
    public void addRiskMutiply(RiskMutiply riskMutiply) {
        queqianmeMapper.addRiskMutiply(riskMutiply);
    }
}
