package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.service.xinwang.project.XWChangeProjectStatusService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayTransactionService;
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
 * 新网还款平台还款后改标状态重跑
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWRepayFinishRerunJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWRepayFinishRerunJob.class);

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWProjectDao projectDao;

    @Resource
    SysRepayDao repayDao;

    @Resource
    XWRepayService repayService;

    @Resource
    XWRepayTransactionService repayTransactionService;

    @Resource
    XWChangeProjectStatusService changeProjectStatusService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网还款平台还款后校验标状态重跑开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            //平台还款后将没有改成YH的还款计划改成已还
            List<Integer> abnormalRepayPlan=repayDao.getNotFinishRepayPlan();
            for(Integer repayPlanId:abnormalRepayPlan){
                try{
                    Map<String,Object> param=new HashMap<>();
                    param.put("id",repayPlanId);
                    param.put("repayState","YH");
                    param.put("now","now");
                    repayDao.updateRepaymentPlanById(param);
                }
                catch(Exception ex){
                    logger.error("还款计划"+repayPlanId+"平台还款后将没有改成YH的还款计划改成已还出错："+ex.toString(), ex);
                }
            }

            //还没完成还款的订单
            List<SysProjectRepayInfo> projectRepayInfoList = repayDao.getOrderNotFinishRepayRecords();
            for(SysProjectRepayInfo projectRepayInfo:projectRepayInfoList){
                XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectRepayInfo.getProjectId());
                try{
                    Integer projectId=Integer.valueOf(projectInfo.getProjectNo());
                    repayTransactionService.ifCurrentTermRepayFinish(projectId,projectRepayInfo.getTerm(),projectRepayInfo.getOrderId());

                }
                catch(Exception ex){
                    logger.error("还款订单"+projectRepayInfo.getOrderId()+"平台还款后结束订单重跑出错："+ex.toString(), ex);
                }
            }
            //查出剩余期数是0但状态是未结清的
            List<Integer> projectIdList=repayDao.getProjectNotFinishRepayRecords();
            for(Integer projectId:projectIdList){
                try{
                   XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
                   repayService.checkIfRepayFinish(projectInfo);
                }
                catch(Exception ex){
                    logger.error("标"+projectId+"修改标状态为已结清重跑出错："+ex.toString(), ex);
                }
            }
        } catch (Throwable e) {
            logger.error("还款后校验标状态重跑出错", e);
        }
    }
}
