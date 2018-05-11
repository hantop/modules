package com.fenlibao.p2p.schedule.task.xinwang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.pay.XWWithdrawDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.service.xinwang.common.XWRequestService;
import com.fenlibao.p2p.service.xinwang.pay.XWWithdrawTransactionService;
import com.fenlibao.p2p.service.xinwang.query.XWQueryTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 新网提现超时确认
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWWithdrawAcceptConfirmJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(XWWithdrawAcceptConfirmJob.class);

    @Resource
    XWWithdrawDao withdrawDao;

    @Resource
    XWQueryTransactionService queryTransactionService;

    @Resource
    XWWithdrawTransactionService withdrawTransactionService;

    @Resource
    XWRequestDao requestDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网提现超时确认开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            // 查询所有页面超时的请求
            List<XWRequest> pageExpiredRequestList = withdrawDao.getPageExpiredRequest();
            for (XWRequest request : pageExpiredRequestList) {
                try{
                // 查询新网请求状态
                String json=queryTransactionService.queryTransaction(null,null, XinwangInterfaceName.WITHDRAW.getCode(),request.getRequestNo());
                Map<String, Object> respMap = JSON.parseObject(json);
                String code=(String)respMap.get("code");
                String status=(String)respMap.get("status");
                //超时原因是受理回调丢失
                if(("0").equals(code)&& "SUCCESS".equals(status)){
                    //保存返回报文
                    XWResponseMessage responseParams=new XWResponseMessage();
                    responseParams.setRequestNo(request.getRequestNo());
                    responseParams.setResponseMsg(json);
                    requestDao.saveResponseMessage(responseParams);

                    JSONArray records=(JSONArray)respMap.get("records");
                    Map<String,String> item=(Map<String,String>)records.get(0);
                    String withdrawStatus=item.get("status");
                    if("ACCEPT_FAIL".equals(withdrawStatus)){
                        withdrawTransactionService.withdrawAcceptFail(request);
                    }
                    else{
                        //其他状态先置成待确认，交由另一个提现定时器处理
                        withdrawTransactionService.withdrawAccept(request);
                    }
                }
                //超时原因是用户页面操作超时
                else {
                    withdrawTransactionService.withdrawAcceptFail(request);
                }
                Thread.sleep(1000);
                }
                catch(Exception ex){
                    logger.error("提现订单"+request.getOrderId()+"超时确认出错："+ex.toString(), ex);
                }
            }

        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
    }

}
