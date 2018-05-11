package com.fenlibao.platform.dao.queqianme;

import com.fenlibao.platform.model.queqianme.*;

public interface QueqianmeMapper {

    Integer getSerialNumCount(String serialNum);

    Integer getFileSerialNum(String serialNum);

    int addbidinfo(Bidinfo bidinfo);

    void addextrainfo(Bidextralinfo bidextralinfo);

    void writeLog(LogInfo logInfo);

    Integer getSerialNumCountForTripleAgreement(String serialNum);

    void addTripleAgreementinfo(TripleAgreementinfo tripleAgreementinfo);

    void addBaseInfo(BaseInfo baseInfo);

    void addWorkInfo(WorkInfo workInfo);

    void addMutiplyBorrow(MutiplyBorrow mutiplyBorrow);

    void addAntiFraud(AntiFraud antiFraud);

    void addRiskMutiply(RiskMutiply riskMutiply);

}
