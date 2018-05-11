package com.fenlibao.p2p.schedule.task.xinwang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWRepaymentPlan;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeQueryType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.service.xinwang.project.XWChangeProjectStatusService;
import com.fenlibao.p2p.service.xinwang.query.XWQueryTransactionService;
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
 * 已发送请求，新网还款没有收到回调时，主动查询更新DQR的请求状态
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWRepayResultConfirmJob  extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWRepayResultConfirmJob.class);

    @Resource
    SysRepayDao repayDao;

    @Resource
    XWQueryTransactionService queryTransactionService;

    @Resource
    XWRequestDao requestDao;
    @Resource
    XWRepayTransactionService repayTransactionService;



    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网还款结果确认开始。。。  实例ID： " + context.getFireInstanceId());
        Map<Integer,List<String>> map=new HashMap<>();
            //距离批次受理超过30分钟的请求
            List<XWRequest> requestList=repayDao.getRepayResultConfirmList();
            for (XWRequest request : requestList) {
                // 查询新网请求状态
            try {
                String json=queryTransactionService.queryTransaction(null,null, XWTradeQueryType.TRANSACTION.name(),request.getRequestNo());
                Map<String, Object> respMap = JSON.parseObject(json);
                String code=(String)respMap.get("code");
                String status=(String)respMap.get("status");
                if(("0").equals(code)&& "SUCCESS".equals(status)){
                    //保存返回报文
                    XWResponseMessage responseParams=new XWResponseMessage();
                    responseParams.setRequestNo(request.getRequestNo());
                    responseParams.setResponseMsg(json);
                    requestDao.saveResponseMessage(responseParams);

                    JSONArray records=(JSONArray)respMap.get("records");
                    Map<String,String> item=(Map<String,String>)records.get(0);
                    String repayStatus=item.get("status");
                    if("SUCCESS".equals(repayStatus)){
                        repayTransactionService.xinwangRepayFinish(request.getRequestNo(), XWTradeType.parse(item.get("confirmTradeType")));
                    }
                    else if("FAIL".equals(repayStatus)){
                        XWRequest requestParams=new XWRequest();
                        requestParams.setRequestNo(request.getRequestNo());
                        requestParams.setState(XWRequestState.SB);
                        requestDao.updateRequest(requestParams);
                    }
                }
                else {
                    logger.error("查询还款请求"+request.getRequestNo()+"失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("新网还款结果"+request.getRequestNo()+"异常");
            }
            }
    }
}
