package com.fenlibao.p2p.schedule.task.sign;

import com.fenlibao.p2p.model.entity.bid.ConsumeBid;
import com.fenlibao.p2p.service.thirdparty.ShangshangqianService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * 上上签
 * @author: kris
 * @date: 20161028
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BestSignJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(BestSignJob.class);

	@Resource
	ShangshangqianService shangshangqianService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("[上上签]开始签名");
		List<ConsumeBid> consumeBids=shangshangqianService.getUnSignBidList();
		if(consumeBids!=null && consumeBids.size()>0){
			shangshangqianService.batchExe(consumeBids,shangshangqianService);
		}
		logger.info(String.format("[上上签]签名结束(%s)",consumeBids.size()));
	}

}