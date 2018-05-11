/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: ZqzrAutoCancelJob.java 
 * @Prject: schedule-p2p
 * @Package: com.fenlibao.p2p.schedule.task 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-4 下午6:09:57 
 * @version: V1.1   
 */
package com.fenlibao.p2p.schedule.task;

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
import java.util.List;

/**
 * 9月名创会员首投活动自动发放奖励
 * @author: kris
 * @date: 20160901
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutoGrantAwardJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(AutoGrantAwardJob.class);

	@Resource
	ActivityService activityService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("名创活动(送现金)活动开始");
		List<McMemberForCashback> mcMembers=activityService.getMcMembersForCashback();
		for(McMemberForCashback mcMember:mcMembers){
			//新增发放记录
			int result=activityService.insertMcMemberSeptemberRecord(mcMember);
			if(result!=1){
				continue;
			}
			try {
				//发放现金
				activityService.grantMcMembersCashback(mcMember);
			} catch (Exception e) {
				//更新发放状态,失败
				activityService.uptateMcMemberSeptemberRecord(mcMember,"F",e.getMessage().substring(0,e.getMessage().length()>200?200:e.getMessage().length()));
				logger.error("【名创活动(送现金)活动异常】："+e.toString(), e);
				continue;
			}
			// 发送短
			String smsPattern = Sender.get("sms.activity.mcMember.cashback.content");
			activityService.sendSms(smsPattern,mcMember.getPhone());
		}
		logger.info(String.format("名创活动(送现金)活动结束(%s)",mcMembers.size()));
	}
}