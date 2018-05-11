package com.fenlibao.p2p.schedule.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.p2p.service.recharge.IRechargeService;
import com.fenlibao.p2p.service.withdraw.IAipgBusManageService;
import com.fenlibao.p2p.service.withdraw.IAipgManageService;
import com.fenlibao.p2p.service.withdraw.IAipgWithdrawService;
import com.fenlibao.p2p.util.pay.DigestUtil;

/**
 * 提现对账
 * @author yangzengcai
 * @date 2015年10月29日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WithdrawJob extends QuartzJobBean {
	
	private static final Logger logger = LogManager.getLogger(WithdrawJob.class);
	
    @Resource
    private IAipgBusManageService aipgBusManageService;
    @Resource
    private IAipgManageService aipgManageService;
    @Resource
    private IAipgWithdrawService aipgWithdrawService;
    @Resource
    private IRechargeService rechargeService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("提现对账开始。。。  实例ID： " + context.getFireInstanceId());
		try {
            // 查询FKZ所有单
            List<Integer> orderList = aipgBusManageService.getWithdrawingList();
            logger.info(String.format("有【%s】张提现订单需要对账", orderList.size()));
            if (orderList != null && orderList.size() > 0) {
                for (Integer orderId : orderList) {
                    logger.debug("提现订单号：" + orderId + "开始对账");
                    String retString = aipgManageService.queryTrade(String.valueOf(orderId));
                    // 查询结果处理
                    doCreateRet(retString, String.valueOf(orderId));
                    Thread.sleep(1000);
                }
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
	}

	
    /**
     * 对账返回结果处理
     *
     * @param retString
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
	private void doCreateRet(String retString, String orderId)
            throws Throwable {
    	try {
            logger.info(String.format("[orderId=%s]连连代付对账-返回的信息:%s", orderId, retString));
            ObjectMapper objm = new ObjectMapper();
            Map<String, String> retParams = objm.readValue(retString, Map.class);
            // 成功       
            if (retParams.size() > 8) {
                // 订单号
                if (DigestUtil.checkReturnSign(retParams)) {
                    logger.debug("验签通过");
                    if ("0000".equals(retParams.get("ret_code"))) {

                        String ret_code = retParams.get("result_pay");
                        if ("SUCCESS".equals(ret_code)) {
                            logger.info("交易成功（最终结果）");
                            // 交易流水号
                            Map<String, String> params = new HashMap<>();
                            params.put("oid_paybill", retParams.get("oid_paybill"));
                            params.put("money_order", retParams.get("money_order"));
                            // 结果处理
                            aipgWithdrawService.confirmForPay(Integer.valueOf(orderId), params);
                            //更新银行卡状态
                            rechargeService.updateBindStatus(Integer.valueOf(orderId));
                        } else if ("PROCESSING".equals(ret_code)) {
                            // 银行代付处理中
                            logger.info(String.format("[orderId=%s]银行代付处理中,等待查询对账", orderId));
                        } else {
                            logger.info("交易失败（最终结果）");
                            logger.info("原因：" + retParams.get("ret_msg"));
                            aipgBusManageService.withdrawFail(Integer.valueOf(orderId), "代付失败");
                        }
                    }
                } else {
                    logger.info(String.format("[orderId=%s]的提现订单验签失败", orderId));
                }
            } else {
            	if (retParams.size() > 0 && StringUtils.isNotBlank(retParams.get("ret_code"))) {
            		if ("1001".equals(retParams.get("ret_code"))) {
            			logger.info(String.format("[orderId=%s]的提现订单，定时器对账失败，原因：%s", orderId, retParams.get("ret_msg")));
            		}
            	} else {
            		logger.info("交易失败（最终结果）");
            		logger.info("原因：代付订单不存在");
            		aipgBusManageService.withdrawFail(Integer.valueOf(orderId), retParams.get("ret_msg"));
            	}
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }
}
