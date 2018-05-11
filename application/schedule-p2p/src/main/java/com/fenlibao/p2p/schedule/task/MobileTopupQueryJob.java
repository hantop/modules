package com.fenlibao.p2p.schedule.task;

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

import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpOrderInchargeEntity;
import com.fenlibao.p2p.service.mp.topup.ITopUpService;

/**
 * 手机充值查询定时器统计任务
 * @author zcai
 * @date 2016年5月20日
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MobileTopupQueryJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(MobileTopupQueryJob.class);

	@Resource
	private ITopUpService topUpService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("易赏手机充值查询定时器  实例ID：{} ", context.getFireInstanceId());
		try {
			List<MobileTopUpOrderInchargeEntity> orderList = topUpService.yishangQueryStatusList(null, 200);
			logger.info("易赏手机充值查询开始，size[{}]", orderList.size());
			for (MobileTopUpOrderInchargeEntity vo : orderList) {
				try {
					Map<String, Object> result = topUpService.yishangQueryStatus(vo);
					topUpService.yishangQueryStatusResult(result);
				} catch (Exception e) {
					logger.error("customOrderCode=["+vo.getCustomOrderCode()+"]查询失败", e);
				}
			}
			logger.info("易赏手机充值查询完成");
		} catch (Exception e) {
			logger.error("易赏手机充值查询失败", e);
		}
	}
		
}
