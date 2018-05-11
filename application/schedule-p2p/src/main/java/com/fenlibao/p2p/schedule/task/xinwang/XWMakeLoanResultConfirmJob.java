package com.fenlibao.p2p.schedule.task.xinwang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeQueryType;
import com.fenlibao.p2p.schedule.common.ThreadPoolHelper;
import com.fenlibao.p2p.service.xinwang.query.XWQueryTransactionService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanTransationService;
import com.fenlibao.p2p.util.DateUtil;
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
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 新网放款没有收到回调时结果确认
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWMakeLoanResultConfirmJob extends QuartzJobBean {
    private static final Logger logger = LogManager.getLogger(XWMakeLoanResultConfirmJob.class);

    @Resource
    SysMakeLoanDao makeLoanDao;

    @Resource
    XWQueryTransactionService queryTransactionService;

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWMakeLoanTransationService makeLoanTransationService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网放款结果确认开始。。。  实例ID： " + context.getFireInstanceId());

        ThreadPoolExecutor executor = ThreadPoolHelper.INSTANCE.getInstance();
        //距离批次受理（改为待确认）超过30分钟的请求
        XWRequest requestParam = new XWRequest();
        requestParam.setInterfaceName(XinwangInterfaceName.ASYNC_TRANSACTION.getCode());
        requestParam.setRequestTime(DateUtil.minuteAdd(DateUtil.nowDate(), -30));
        List<XWRequest> requestList = makeLoanDao.getResultConfirmRequest(requestParam);
        for (XWRequest request : requestList) {
            try {
                Runnable runner = new XWMakeLoanResultConfirmJob.Runner(request);
                executor.submit(runner);
                Thread.sleep(300);
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }
        }
    }

    public class Runner implements Runnable {
        protected XWRequest request;

        public Runner(XWRequest request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                // 查询新网请求状态
                String json = queryTransactionService.queryTransaction(null, null, XWTradeQueryType.TRANSACTION.name(), request.getRequestNo());
                /** 待提交状态的订单改为待确认 */
                Map<String, Object> params = new HashMap<>();
                params.put("requestNo", request.getRequestNo());
                params.put("state", XWRequestState.DQR);
                params.put("preState", XWRequestState.DTJ);
                requestDao.updateRequestStatus(params);
                Map<String, Object> respMap = JSON.parseObject(json);
                String code = (String) respMap.get("code");
                String status = (String) respMap.get("status");
                if (("0").equals(code) && "SUCCESS".equals(status)) {
                    //保存返回报文
                    XWResponseMessage responseParams = new XWResponseMessage();
                    responseParams.setRequestNo(request.getRequestNo());
                    responseParams.setResponseMsg(json);
                    requestDao.saveResponseMessage(responseParams);

                    JSONArray records = (JSONArray) respMap.get("records");
                    Map<String, String> item = (Map<String, String>) records.get(0);
                    String makeLoanStatus = item.get("status");
                    if ("SUCCESS".equals(makeLoanStatus)) {
                        //平台放款
                        makeLoanTransationService.platformMakeLoan(request.getRequestNo(), request.getOrderId());
                    } else if ("FAIL".equals(makeLoanStatus)) {
                        // 应该是订单和业务同时失败
                        params = new HashMap<>();
                        params.put("requestNo", request.getRequestNo());
                        params.put("state", XWRequestState.SB);
                        params.put("preState", XWRequestState.DQR);
                        requestDao.updateRequestStatus(params);
                    }
                }if("100007".equals(code)){//交易不存在
                    XWRequest requestParams = new XWRequest();
                    requestParams.setRequestNo(request.getRequestNo());
                    requestParams.setState(XWRequestState.SB);
                    requestDao.updateRequest(requestParams);
                } else {
                    logger.error("查询放款请求" + request.getRequestNo() + "失败");
                }
            } catch (Exception e) {
                logger.error("查询放款请求" + request.getRequestNo() + "失败", e);
            }
        }
    }
}
