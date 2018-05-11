package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayService;
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
 * 新网自动还消费信贷标
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWAutoRepayJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWAutoRepayJob.class);

    @Resource
    SysRepayDao repayDao;

    @Resource
    XWRepayService repayService;

    @Resource
    PTCommonDao ptCommonDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网自动还消费信贷标开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            List<Integer> projectList = repayDao.getAutoRepayProjectList();
            for (Integer projectId : projectList) {
                try {
                    //自动还款默认代偿
                    repayService.repayApply(projectId, SysRepayOperationType.REPAY, true,null);
                }catch(Exception e){
                    ErrorLogParam errorLogParam = new ErrorLogParam();
                    errorLogParam.setMethod("XWRepayService.repayApply");
                    String param = e.toString();
                    errorLogParam.setErrorLog(param.toString().substring(0,param.length()<800?param.length():800).trim());
                    ptCommonDao.insertErrorLog(errorLogParam);
                    logger.error("标"+projectId+"自动还款出错："+e.toString(), e);
                }
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
    }
}
