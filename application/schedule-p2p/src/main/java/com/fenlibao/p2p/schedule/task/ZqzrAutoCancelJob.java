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

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fenlibao.p2p.service.creditassignment.TenderTransferManageService;
import com.fenlibao.p2p.service.creditassignment.impl.TenderTransferManageServiceImpl;

/** 
 * @ClassName: ZqzrAutoCancelJob 
 * @Description:债权转让自动下架
 * @author: laubrence
 * @date: 2015-11-4 下午6:09:57  
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ZqzrAutoCancelJob extends QuartzJobBean{
	
	private static final Logger logger = LogManager.getLogger(ZqzrAutoCancelJob.class);

	@Resource 
	TenderTransferManageService tenderTransferManageService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("====【债权转让定时任务处理...】");
    	try {
    		tenderTransferManageService.zqzrAutoCancel();
		} catch (Throwable e) {
			logger.info("==========================【债权转让自动下架异常...】："+e.toString(), e);
			e.printStackTrace();
		}
	}
}