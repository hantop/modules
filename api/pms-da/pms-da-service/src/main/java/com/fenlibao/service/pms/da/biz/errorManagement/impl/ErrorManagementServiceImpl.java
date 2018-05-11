package com.fenlibao.service.pms.da.biz.errorManagement.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.dao.pms.da.biz.errorManagement.ErrorManagementMapper;
import com.fenlibao.model.pms.da.biz.viewobject.ErrorEntrustPayVO;
import com.fenlibao.p2p.model.xinwang.entity.trust.AuthorizationEntrustRecord;
import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.entrust.XWEntrustService;
import com.fenlibao.service.pms.da.biz.errorManagement.ErrorManagementService;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 还款管理
 * <p>
 */
@Service
public class ErrorManagementServiceImpl implements ErrorManagementService {
    private static final Logger logger = LogManager.getLogger(ErrorManagementServiceImpl.class);

    @Autowired
    private ErrorManagementMapper errorManagementMapper;

    @Autowired
    private XWEntrustService xwEntrustService;

    @Override
    public List<ErrorEntrustPayVO> getErrorEntrustPayList(RowBounds bounds) {
        List<ErrorEntrustPayVO> errorEntrustPayVOList = errorManagementMapper.getErrorEntrustPayList(bounds);
        if (errorEntrustPayVOList.size() > 0){
            Map<String, Object> resultMap = new HashMap<>();
            String errorReson = null;
            for (ErrorEntrustPayVO errorEntrustPayVO : errorEntrustPayVOList) {
                errorReson = errorEntrustPayVO.getErrorReson();
                resultMap = JSON.parseObject(errorReson);
                if(resultMap != null && !resultMap.get("authorizeStatus").toString().equals("PASSED") && resultMap.get("errorMessage") != null){
                    errorEntrustPayVO.setErrorReson(resultMap.get("errorMessage").toString());
                }else{
                    errorEntrustPayVO.setErrorReson(null);
                }
            }
        }
        return errorEntrustPayVOList;
    }

    @Override
    public String handelError(List<String> loanIds) throws Exception{
        if(loanIds.size() > 0){
            List<Map<String, Object>> entrustRequestNoAndLoanIdMaps = new ArrayList<>();
            entrustRequestNoAndLoanIdMaps = errorManagementMapper.getRequestNoByLoanId(loanIds);
            for (String loanId: loanIds) {
                if(loanId != null){
                    String requestNo = getEntrustRequestNoByLoanId(entrustRequestNoAndLoanIdMaps, loanId) == null ?
                            null : getEntrustRequestNoByLoanId(entrustRequestNoAndLoanIdMaps, loanId);
                    if(requestNo != null){
                        try{
                            // 查询委托支付授权状态
                            AuthorizationEntrustRecord authorizationEntrustRecord = xwEntrustService.getAuthEntrustRecord(requestNo);
                            if(!authorizationEntrustRecord.getAuthorizeStatus().equals("PASSED")){
                                AuthorizeStatus authorizeStatus = xwEntrustService.authEntrustPay(Integer.valueOf(loanId));
                            }
                        }catch (XWTradeException e){
                            logger.error("[调用存管委托支付产生异常:]" + e.getMessage(), e);
                            throw e;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String getEntrustRequestNoByLoanId(List<Map<String, Object>> entrustStateAndLoanIdMaps, String loanId){
        if(entrustStateAndLoanIdMaps.size() > 0){
            for (Map<String, Object> entrustStateDetail : entrustStateAndLoanIdMaps) {
                if (loanId.equals(entrustStateDetail.get("loanId").toString())) {
                    if(entrustStateDetail.get("requestNo").toString() != null){
                        return entrustStateDetail.get("requestNo").toString();
                    }
                }
            }
        }
        return null;
    }
}
