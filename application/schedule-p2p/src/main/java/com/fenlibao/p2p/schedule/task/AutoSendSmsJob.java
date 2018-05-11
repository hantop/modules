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

import com.fenlibao.p2p.model.entity.activity.McMemberForBillSms;
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
public class AutoSendSmsJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(AutoSendSmsJob.class);

	@Resource
	ActivityService activityService;
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("名创活动(送奖品)发送短信开始");
		List<McMemberForBillSms> mcMembers=activityService.getMcMemberForBillSms();
		for(McMemberForBillSms mcMember:mcMembers){
			//新增记录
			int result=activityService.insertMcMemberSeptemberSms(mcMember);
			if(result!=1){
				continue;
			}
			// 发送短信：（条件二选一）
			//1.当首次投资≥5888元时，
			//尊敬的用户：恭喜您通过三月女神活动获得面值200元的携程礼品卡1份，奖励将在3个工作日内以短信形式发放，感谢您的支持！
			//2.当首次投资≥2888元时，
			//尊敬的用户：恭喜您通过三月女神活动获得单张面值50元的电影券2张，奖励将在3个工作日内以短信形式发放，感谢您的支持！
			String smsPattern = Sender.get("sms.activity.mcMember.prize3.content");
			if(2==mcMember.getParvalue()){
				smsPattern = Sender.get("sms.activity.mcMember.prize4.content");
			}
			activityService.sendSms(smsPattern,mcMember.getPhone());
//			activityService.sendSms(String.format(smsPattern,mcMember.getParvalue()),mcMember.getPhone());
		}
		logger.info(String.format("名创活动(送奖品)发送短信结束(%s)",mcMembers.size()));
	}
}