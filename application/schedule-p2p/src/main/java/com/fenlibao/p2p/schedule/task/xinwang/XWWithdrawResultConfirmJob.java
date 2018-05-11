package com.fenlibao.p2p.schedule.task.xinwang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.pay.XWWithdrawDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeQueryType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.common.LockService;
import com.fenlibao.p2p.service.common.RedisService;
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
 * 新网提现结果确认
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWWithdrawResultConfirmJob extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(XWWithdrawResultConfirmJob.class);

    @Resource
    XWWithdrawDao withdrawDao;

    @Resource
    XWQueryTransactionService queryTransactionService;

    @Resource
    XWWithdrawTransactionService withdrawTransactionService;

    @Resource
    XWRequestDao requestDao;

    @Resource
    RedisService redisService;

    @Resource
    LockService lockService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("新网提现结果确认开始。。。  实例ID： " + context.getFireInstanceId());
        try {
            // 考虑到要对账确认，查询所有待确认超过36小时的请求
            List<XWRequest> requestList = withdrawDao.getResultConfirmRequest();
            for (XWRequest request : requestList) {
                if (redisService.existsKey(request.getRequestNo())) {
                    continue;
                }
                try{
                    // 查询新网请求状态
                    String json=queryTransactionService.queryTransaction(null,null, XWTradeQueryType.WITHDRAW.name(),request.getRequestNo());
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
                        if("SUCCESS".equals(withdrawStatus)){
                            try {
                                lockService.createLock(request.getRequestNo(), "CG");
                            } catch (Exception ex) {
                                logger.warn("提现同步锁插入异常，该订单已经成功处理，无需再次处理，requestNo:" + request.getRequestNo());
                                throw new XWTradeException(XWResponseCode.TRADE_OPERATION_REPEAT);
                            }
                            withdrawTransactionService.withdrawSuccess(request);
                        }
                        else if("FAIL".equals(withdrawStatus)){
                            withdrawTransactionService.withdrawFail(request);
                        }
                        else{
                            logger.error("提现请求"+request.getRequestNo()+"出款异常,异常状态" + withdrawStatus);
                        }
                    }
                    else {
                        logger.error("查询提现请求"+request.getRequestNo()+"失败");
                    }
                    Thread.sleep(1000);
                }
                catch(Exception ex){
                    logger.error("提现订单"+request.getOrderId()+"结果确认出错："+ex.toString(), ex);
                }finally {
                    redisService.removeKey(request.getRequestNo());
                }
            }

        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
    }

}
