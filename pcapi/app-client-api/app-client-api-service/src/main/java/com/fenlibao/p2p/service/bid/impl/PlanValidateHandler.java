package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/15 13:43.
 * @version 1.0
 * 将有关计划的一些业务判断放到这里判断，这样有助于整洁代码
 */
@Component
public class PlanValidateHandler {

    private static final Logger LOGGER = LogManager.getLogger(PlanValidateHandler.class);

    public void validateInvestPlan(Integer userId, Integer planId, BigDecimal investAmount) throws Exception {
        if (userId == null || userId <= 0 || planId == null || planId <= 0 || investAmount == null || investAmount.compareTo(BigDecimal.ZERO) < 0) {
            LOGGER.error("购买计划时参数为空：userId:{}, planId:{}, investAmount:{}", userId, planId, investAmount);
            throw new BusinessException(ResponseCode.EMPTY_PARAM);
        }
    }
}
