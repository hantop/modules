
package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.entity.activity.DtbForCashBack;
import com.fenlibao.p2p.model.entity.activity.McMemberForCashback;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地铁报首投活动自动发放奖励
 * @author: kris
 * @date: 20161009
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DtbAutoGrantAwardJob extends QuartzJobBean{

	private static final Logger logger = LogManager.getLogger(DtbAutoGrantAwardJob.class);

	@Resource
	ActivityService activityService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("地铁报首投活动自动发放奖励，定时任务处理开始--------------------------");
		List<DtbForCashBack> list=activityService.getDtbForCashBack(DtbForCashBack.ACTIVITY_CODE);
		for(DtbForCashBack dtb:list){
			int result=activityService.insertActivityCashbackRecord(dtb);
			if(result!=1){
				continue;
			}
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("userId",dtb.getUserId());
			map.put("amount",dtb.getAmount());
			map.put("code",dtb.ACTIVITY_CODE);
			try {
				activityService.grantActivityCashback(map);
			} catch (Exception e) {
				map.put("grantState",'F');
				activityService.uptateActivityCashbackRecord(map);
				logger.error("地铁报首投活动自动发放奖励处理异常："+e.toString(), e);
			}
		}
		logger.info(String.format("地铁报首投活动自动发放奖励，处理结束(%s)",list.size()));
	}
}