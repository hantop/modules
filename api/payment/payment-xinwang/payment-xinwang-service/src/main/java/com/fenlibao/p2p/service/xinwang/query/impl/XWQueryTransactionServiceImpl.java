package com.fenlibao.p2p.service.xinwang.query.impl;

import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.service.xinwang.query.XWQueryTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/18.
 */
@Service
public class XWQueryTransactionServiceImpl implements XWQueryTransactionService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Override
    public String queryTransaction(Integer userId, String userRole, String transactionType, String requestNo2) {
        //组装请求
        String requestNo=XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        String platformUserNo = "";
        if (userId != null && StringUtils.isNotBlank(userRole)) {
            platformUserNo = userRole + userId;
        }
        reqData.put("requestNo", requestNo2);
        if (StringUtils.isNotBlank(platformUserNo)) {
            reqData.put("platformUserNo", platformUserNo);
        }
        reqData.put("transactionType", transactionType);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //发送请求
        String resultJson = "";
        try {
            resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.QUERY_TRANSACTION.getCode(), reqData);
        }catch (Exception e){
            LOG.error(e);
        }
        return resultJson;
    }
}
