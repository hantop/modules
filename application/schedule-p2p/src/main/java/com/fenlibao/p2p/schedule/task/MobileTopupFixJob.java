package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpErrorRecord;
import com.fenlibao.p2p.service.mp.topup.ITopUpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 手机充值 异常处理
 * @author kris
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MobileTopupFixJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(MobileTopupFixJob.class);

	@Resource
	private ITopUpService topUpService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("[易赏]备灾定时器  实例ID：{} ", context.getFireInstanceId());
		try {
			List<MobileTopUpErrorRecord> orderList = topUpService.yishangErrorRecords();
			for (MobileTopUpErrorRecord vo : orderList) {
				try {
					Map<String, Object> result = topUpService.yishangQueryStatus(vo);
					topUpService.yishangDealErrorRecord(result);
				} catch (Exception e) {
					logger.error("customOrderCode=["+vo.getCustomOrderCode()+"]查询失败", e);
				}
			}
			logger.info("[易赏]备灾完成，size[{}]", orderList.size());
		} catch (Exception e) {
			logger.error("[易赏]备灾失败", e);
		}
	}
		
}
