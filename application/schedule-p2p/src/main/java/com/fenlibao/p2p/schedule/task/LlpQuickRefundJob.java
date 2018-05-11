package com.fenlibao.p2p.schedule.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.dimeng.util.parser.BooleanParser;
import com.fenlibao.lianpay.v_1_0.enums.SignTypeEnum;
import com.fenlibao.p2p.model.vo.pay.RefundVO;
import com.fenlibao.p2p.service.pay.ILlpQuickPayService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Payment;

/**
 * 连连快捷支付退款
 * @author zcai
 * @date 2016年4月26日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LlpQuickRefundJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(LlpQuickRefundJob.class);

	@Resource
	private ILlpQuickPayService quickPayService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("连连快捷支付退款开始  实例ID：[{}] ", context.getFireInstanceId());
		List<RefundVO> orderList = quickPayService.getWaitRefundOrder(); //每次300
		logger.info("退款订单数量[{}]", orderList.size());
		boolean isTest = BooleanParser.parse(Payment.get(Payment.IS_QUICK_TEST));// 是否为测试
		Map<String, String> params = new HashMap<>(10);
		String sign_type = SignTypeEnum.MD5.getCode();
		try {
			String key = quickPayService.getSignKey(sign_type, true);
			String oid_partner = Payment.get(Payment.OID_PARTNER_QUICK);
			if (isTest) {
				oid_partner = Payment.get(Payment.OID_PARTNER_QUICK_TEST);
			}
			params.put("oid_partner", oid_partner);
			params.put("notify_url", Config.get(Payment.NOTIFY_URL_QUICK_REFUND));
			params.put("sign_type", sign_type);
			for (RefundVO vo : orderList) {
				try {
					quickPayService.submitRefund(vo,params, key, isTest);
				} catch (Exception e) {
					logger.error("RefundVO["+vo.toString()+"]退款失败 >>> ", e);
				}
			}
		} catch (Exception e1) {
			logger.error("quick pay refund exception >>> ", e1);
		}
	}
}
