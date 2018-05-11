package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.entity.order.UnusualRepay;
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
 * 新网非正常还款任务
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWUnusualRepayob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWUnusualRepayob.class);

    @Resource
    SysRepayDao repayDao;
    @Resource
    XWRepayService repayService;
    @Resource
    PTCommonDao commonDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网非正常还款开始。。。  实例ID： " + context.getFireInstanceId());

        List<UnusualRepay> orderList = repayDao.getUnusualRepays();
        for (UnusualRepay unusualRepay : orderList) {
            try {
                //先改异常还款订单状态，再发起异常还款
                unusualRepay.setStatus(false);
                int result = repayDao.updateUnusualRepay(unusualRepay, true);
                if (result == 1) {
                    repayService.handleError(unusualRepay.getOrderId());
                }
            } catch (Exception e) {
                ErrorLogParam param = new ErrorLogParam();
                param.setMethod("XWUnusualRepayob");
                param.setErrorLog(e.getMessage());
                commonDao.insertErrorLog(param);
                logger.error("新网非正常还款出错", e);
            }
        }
    }
}
