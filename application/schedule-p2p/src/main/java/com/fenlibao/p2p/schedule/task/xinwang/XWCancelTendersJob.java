package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysCancelTenderDao;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_Cancel;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_MakeLoan;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;
import com.fenlibao.p2p.service.xinwang.project.XWChangeProjectStatusService;
import com.fenlibao.p2p.service.xinwang.trade.XWCancelTenderTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台流标
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWCancelTendersJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWCancelTendersJob.class);

    @Resource
    XWProjectDao projectDao;

    @Resource
    PTCommonDao commonDao;

    @Resource
    XWCancelTenderTransactionService cancelTenderTransactionService;

    @Resource
    SysCancelTenderDao cancelTenderDao;

    @Resource
    XWChangeProjectStatusService changeProjectStatusService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("流标中断重跑开始。。。  实例ID： " + context.getFireInstanceId());
        // 已操作放款仍处于待放款标的  500条
        List<Integer> bidIds = projectDao.getCancelBidsByStatus(PTProjectState.DFK);
        for (Integer projectId : bidIds) {
            try {
                XWProjectInfo projectInfo = projectDao.getProjectInfoById(projectId);
                // 当前时间
                Date currentDate = commonDao.getCurrentDate();
                // 判断是否全部投资均已取消,是则更新标状态为已流标
                Map<String,Object> tenderRecordParams = new HashMap<>(3);
                tenderRecordParams.put("projectNo", projectId);
                tenderRecordParams.put("cancel", TenderRecord_Cancel.F.name());
                tenderRecordParams.put("makeLoan", TenderRecord_MakeLoan.F.name());
                List<XWTenderRecord> notYetCancelList = projectDao.getTenderRecord(tenderRecordParams);
                if (notYetCancelList.isEmpty()) {
                    Integer orderId = cancelTenderDao.getOngoingCancelTenderOrder(projectId);
                    // 改变新网标状态
                    changeProjectStatusService.changeProjectStatus(Integer.valueOf(projectInfo.getProjectNo()), orderId, XWProjectStatus.MISCARRY);
                    // 改变平台标状态
                    cancelTenderTransactionService.finishCancelTender(projectId, orderId, currentDate);
                }
            } catch (Exception ex) {
                StringBuilder sb = new StringBuilder("检查标的流标是否结束异常：");
                sb.append(projectId);
                sb.append(ex.toString());
                logger.error(sb.toString(), ex);
            }
        }
    }

    public void test(JobExecutionContext context) throws JobExecutionException{
        this.executeInternal(context);
    }
}
