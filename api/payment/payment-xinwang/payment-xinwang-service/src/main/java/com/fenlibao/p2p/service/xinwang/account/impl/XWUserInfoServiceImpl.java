package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class XWUserInfoServiceImpl implements XWUserInfoService{
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    private XWAccountDao accountDao;

    @Override
    public XinwangUserInfo queryUserInfo(String platformUserNo){
        Map<String, Object> reqData = new HashMap<String, Object>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        String result = "";
        try {
            result = XinWangUtil.serviceRequest(XinwangInterfaceName.QUERY_USER_INFORMATION.getCode(),reqData);
        }catch (Exception e){
            LOG.error(e);
        }

        XinwangUserInfo userInfo = JSONObject.parseObject(result, XinwangUserInfo.class);
        return userInfo;
    }

    @Override
    public XWFundAccount getFundAccount(int userId, SysFundAccountType fundAccountType){
        return accountDao.getFundAccount(userId, fundAccountType);
    }

    @Override
    public void validateAmount(XWFundAccount userInfo, BigDecimal amount) {
        if (amount.compareTo(userInfo.getAmount()) > 0) {//先判断可用余额是否足够
            throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
        }
    }
}
