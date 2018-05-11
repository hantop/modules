package com.fenlibao.p2p.service.xinwang.bid.impl;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.order.SysTenderOrder;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.trade.PreTransactionResult;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.bid.ProductTypeForCG;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.model.xinwang.param.transaction.PreTransactionParam;
import com.fenlibao.p2p.service.xinwang.bid.SysBidManageService;
import com.fenlibao.p2p.service.xinwang.bid.XWBidService;
import com.fenlibao.p2p.service.xinwang.bid.XWBidTransactionService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.trade.XWTransactionService;
import com.fenlibao.p2p.util.xinwang.UserRoleUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @date 2017/5/11 16:04
 */
@Service
public class XWBidServiceImpl implements XWBidService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(XWBidService.class);
    @Resource
    private XWTransactionService transactionService;
    @Resource
    private XWProjectDao projectDao;
    @Resource
    private SysBidManageService bidManageService;
    @Resource
    private XWRequestDao requestDao;
    @Resource
    private SysOrderService orderService;
    @Resource
    private PTCommonDao commonDao;
    @Resource
    private XWBidTransactionService bidTransactionService;

    @Override
    public void doBid(Integer orderId, Integer jxqId, String[] redpacketIdsArr) throws Exception {
        SysTenderOrder tenderOrder = bidManageService.getTenderOrder(orderId);
        if (tenderOrder == null) {
            logger.warn(String.format("投标订单不存在,orderId=%s", orderId));
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        XWProjectInfo projectInfo = projectDao.getProjectInfoById(tenderOrder.getBidId());
        if (projectInfo == null) {
            logger.warn(String.format("新网标记录不存在，bidId=%s", tenderOrder.getBidId()));
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        //创建新网订单
        String platformUserNo = UserRole.INVESTOR.getCode() + tenderOrder.getUserId();
        String projectNo = tenderOrder.getBidId() + "";
        String requestNo = XinWangUtil.createRequestNo();
        Date requestTime = new Date();
        XWRequest req = new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.USER_AUTO_PRE_TRANSACTION.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DTJ);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(tenderOrder.getUserId());
        req.setOrderId(orderId);
        requestDao.createRequest(req);
        PreTransactionParam param = new PreTransactionParam();
        param.setBizType(XWBizType.TENDER);
        param.setAmount(tenderOrder.getTenderAmount());
        param.setPlatformUserNo(platformUserNo);
        param.setProjectNo(projectNo);
        param.setRequestNo(requestNo);
        PreTransactionResult transactionResult;
        try {
            transactionResult = transactionService.preTransaction(param);
        } catch (Exception ex) {
            bidTransactionService.doFail(orderId, "XWBidService.doBid", requestNo, XWResponseCode.COMMON_REQUEST_FAIL.getCode(), XWResponseCode.COMMON_REQUEST_FAIL.getMessage());
            throw new XWTradeException(XWResponseCode.BID_INVESTMENT_FAILURE);
        }
        if (transactionResult == null || !transactionResult.validate()) {
            bidTransactionService.doFail(orderId, "XWBidService.doBid", requestNo, transactionResult.getErrorCode(), transactionResult.getErrorMessage());
            throw new XWTradeException(XWResponseCode.BID_INVESTMENT_FAILURE);
        }
        Throwable throwable = null;
        try {
            bidTransactionService.doSubmit(orderId, requestNo);
            bidTransactionService.doSuccess(orderId, requestNo, jxqId, ProductTypeForCG.BID, redpacketIdsArr);
        } catch (Exception ex) {
            bidTransactionService.doFail(orderId,"doBidForPlan", requestNo,"","");
            logger.error(String.format("新网出借预处理失败，requestNo=%s,失败原因:%s,%s", requestNo, transactionResult.getErrorCode(), transactionResult.getErrorMessage()));
            logger.error("新网出借预处理失败异常信息:{}", ex);
            //本地失败的时候新网也要解冻金额
            try {
                transactionService.cancelTransaction(requestNo, tenderOrder.getTenderAmount());
            } catch (Throwable th) {
                throwable = th;
            }
        } finally {
            if (throwable != null) {
                //如果失败的话写入失败订单
                StringBuilder str = new StringBuilder();
                str.append("新网出借回滚失败，预处理流水：").append(requestNo)
                        .append(",orderId:").append(orderId)
                        .append(":").append(throwable.getMessage());
                ErrorLogParam errorLogParam = new ErrorLogParam();
                errorLogParam.setMethod("XWBidService.doBid");
                errorLogParam.setErrorLog(throwable.toString());
                commonDao.insertErrorLog(errorLogParam);
                orderService.insertOrderExceptionLog(orderId, str.toString());
            }
        }
    }

    @Override
    public void doBidForPlan(Integer orderId, XWTenderBO tender) throws Exception {
        int bidId = tender.getBidId();
        Integer userId = UserRoleUtil.parseUserNo(tender.getInvestorPlatformUserNo()).getUserId();
        XWProjectInfo projectInfo = projectDao.getProjectInfoById(bidId);
        if (projectInfo == null) {
            logger.warn(String.format("新网标记录不存在，bidId=%s", bidId));
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        //创建新网订单
        String platformUserNo = UserRole.INVESTOR.getCode() + userId;
        String projectNo = bidId + "";
        Date requestTime = new Date();
        XWRequest req = new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.USER_AUTO_PRE_TRANSACTION.getCode());
        req.setRequestNo(tender.getPreTreatRequestNo());
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DTJ);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(userId);
        req.setOrderId(orderId);
        requestDao.createRequest(req);
        PreTransactionParam param = new PreTransactionParam();
        param.setBizType(XWBizType.TENDER);
        param.setAmount(tender.getAmount());
        param.setPlatformUserNo(platformUserNo);
        param.setProjectNo(projectNo);
        param.setRequestNo(tender.getPreTreatRequestNo());
        PreTransactionResult transactionResult;
        try {
            transactionResult = transactionService.preTransaction(param);
        } catch (Exception ex) {
            bidTransactionService.doFail(orderId, "XWBidService.doBidForPlan", tender.getPreTreatRequestNo(), XWResponseCode.COMMON_REQUEST_FAIL.getCode(), XWResponseCode.COMMON_REQUEST_FAIL.getMessage());
            throw new XWTradeException(XWResponseCode.BID_INVESTMENT_FAILURE);
        }
        if (transactionResult == null || !transactionResult.validate()) {
            bidTransactionService.doFail(orderId, "XWBidService.doBidForPlan", tender.getPreTreatRequestNo(), transactionResult.getErrorCode(), transactionResult.getErrorMessage());
            throw new XWTradeException(XWResponseCode.BID_INVESTMENT_FAILURE);
        }

        bidTransactionService.doSuccessForPlan(orderId, tender, projectInfo);
    }

    @Override
    public List<XWTenderBO> getSendTender() {
        return projectDao.getSendTender();
    }
}
