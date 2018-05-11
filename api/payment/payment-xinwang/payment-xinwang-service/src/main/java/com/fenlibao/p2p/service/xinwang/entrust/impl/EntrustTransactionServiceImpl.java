package com.fenlibao.p2p.service.xinwang.entrust.impl;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.entrust.EntrustTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @date 2017/8/10 17:53
 */
@Service
public class EntrustTransactionServiceImpl implements EntrustTransactionService {
    private final Logger LOG = LogManager.getLogger(this.getClass());
    @Resource
    private XWProjectDao projectDao;
    @Resource
    private XWRequestDao requestDao;

    @Transactional
    public void doSuccess(String requestNo, Integer loadId, AuthorizeStatus status) {
        projectDao.setEntrustInfo(loadId, null, requestNo, status);
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.CG);
        requestDao.updateRequest(param);
    }

    @Transactional
    public void doFail(String requestNo,Integer loadId, String errorMessage, AuthorizeStatus status) throws Exception{
        projectDao.setEntrustInfo(loadId, null, requestNo, status);
        //请求失败或处理失败
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.SB);
        requestDao.updateRequest(param);
        String errorLog=String.format(XWResponseCode.XW_ENTRUST_AUTHORIZATION_FAIL.getMessage(),errorMessage);
        LOG.warn("委托支付授权失败,requestNo[{}],errorMessage[{}]", requestNo, errorMessage);
        throw new XWTradeException(XWResponseCode.XW_ENTRUST_AUTHORIZATION_FAIL.getCode(),errorLog);
    }
}
