package com.fenlibao.p2p.service.xinwang.trade;


import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface XWSyncTransactionService {
    /**
     * 发放营销款
     * @param userId 用户账户
     * @param userRole 用户角色
     * @param amounts 金额
     * @param businessType 交易类型
     * @return
     */
    public Map<String,Object> syncTransactionMarketing(Integer userId, String userRole, List<BigDecimal> amounts, BusinessType businessType, Integer orderId) throws Exception;

    public Map<String,Object> syncTransactionMarketingForPlanSettle(Integer userId, String userRole, List<BigDecimal> amounts, BusinessType businessType, Integer orderId) throws Exception;
}
