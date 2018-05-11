package com.fenlibao.p2p.schedule.task;

import com.fenlibao.p2p.model.entity.creditassignment.TransferOutInfo;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 每天自动购买2天前申请退出的随时退出标债权
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PurchaseQuitAssignmentJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(PurchaseQuitAssignmentJob.class);

    @Resource
    private SpecialUserService specialUserService;

    private int count ;

    @Resource
    private TransferInService transferInService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时器自动购买(2天前申请退出的随时退出标债权)开始  实例ID： "+context.getFireInstanceId());
        try {
            List<String> userIds = specialUserService.getUserIds(SpecialUserType.ZQZR);

            Date time = new Date();
            time = DateUtil.dateAdd(time,-2);

            List<TransferOutInfo> transferOutInfos = transferInService.getTransferOutAnytimeQuitList(10, time.getTime()/1000,1);

            count = 0 ;
            if (transferOutInfos != null && transferOutInfos.size() > 0) {
                for (int i = 0; i < transferOutInfos.size(); i++) {
                    try {
                        transferInService.purchase(Integer.valueOf(transferOutInfos.get(i).getApplyforId()), Integer.valueOf(userIds.get(0)));
                        count++;
                    }catch (BusinessException ex){
                        logger.info("定时器自动购买随时退出标债权失败:债权id = "+transferOutInfos.get(i).getApplyforId()+"====错误信息 : {}",ex.getMessage());
                    }
                }
            }
        }catch (Throwable e) {
            logger.error("定时器自动购买随时退出标债权失败", e);
        }
        logger.info("定时器自动购买(2天前申请退出的随时退出标债权)结束,本次处理债权 : {} 条", count);
    }

}
