package com.fenlibao.p2p.service.xinwang.project;

import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;

/**
 * Created by Administrator on 2017/7/6.
 */
public interface XWEstablishProjectTransactionService {
    void ptEstablishProject(String requestNo,Integer loanId,XWProjectInfo projectInfo) throws Exception;
}
