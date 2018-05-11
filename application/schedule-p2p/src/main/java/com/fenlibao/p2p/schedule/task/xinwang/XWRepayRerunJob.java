package com.fenlibao.p2p.schedule.task.xinwang;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWRepaymentPlan;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新网还款平台还款中断后重跑
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWRepayRerunJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWRepayRerunJob.class);

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
        logger.info("新网还款平台还款中断后重跑开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            Map<Integer,List<String>> map=new HashMap<>();
            //距离新网还款成功超过1小时平台还没还款的请求
            List<XWRequest> requestList = repayDao.getRepayRerunList();
            for (XWRequest request : requestList) {
                List<String> reqList=map.get(request.getOrderId());
                if(reqList==null){
                    reqList=new ArrayList<>();
                    map.put(request.getOrderId(),reqList);
                }
                reqList.add(request.getRequestNo());
            }
            for(Integer orderId:map.keySet()){
                SysProjectRepayInfo projectRepayInfo= null;
                XWProjectInfo projectInfo= null;
                try{
                    projectRepayInfo = repayDao.getProjectRepayInfoByOrderId(orderId);
                    projectInfo = projectDao.getProjectInfoById(projectRepayInfo.getProjectId());
                    Integer projectId=projectRepayInfo.getProjectId();
                    repayTransactionService.platfromRepay(projectId,projectInfo,projectRepayInfo,map.get(orderId));
                    //标是否还完
                    //repayService.checkIfRepayFinish(projectInfo);
                }
                catch(Exception ex){
                    logger.error("还款订单"+orderId+"平台还款中断后重跑出错："+ex.toString(), ex);
                }
                //发信息
                try {
                    repayTransactionService.sendLetterAndMsg(Integer.valueOf(projectInfo.getProjectNo()), projectInfo, projectRepayInfo);
                }
                catch(Exception e){
                    logger.error("标"+projectInfo.getProjectName()+"还款后发送信息出错："+e.getMessage(),e);
                }
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
    }
}
