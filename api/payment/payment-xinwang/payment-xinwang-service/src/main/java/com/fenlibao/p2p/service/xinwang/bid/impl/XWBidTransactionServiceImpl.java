package com.fenlibao.p2p.service.xinwang.bid.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.SysBidManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.bid.SysBidInfo;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.order.SysTenderOrder;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderEntity;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysTenderRecord;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.bid.ProductTypeForCG;
import com.fenlibao.p2p.model.xinwang.enums.common.XWCapitalFlowLevel;
import com.fenlibao.p2p.model.xinwang.enums.coupon.CouponState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.bid.SysBidManageService;
import com.fenlibao.p2p.service.xinwang.bid.XWBidTransactionService;
import com.fenlibao.p2p.service.xinwang.common.XWRequestService;
import com.fenlibao.p2p.service.xinwang.coupon.SysCouponManageService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.util.xinwang.UserRoleUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class XWBidTransactionServiceImpl implements XWBidTransactionService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(XWBidTransactionService.class);
    @Resource
    private XWProjectDao projectDao;
    @Resource
    private SysBidManageService bidManageService;
    @Resource
    private XWRequestDao requestDao;
    @Resource
    private XWRequestService requestService;
    @Resource
    private SysOrderManageDao orderManageDao;
    @Resource
    private SysOrderService orderService;
    @Resource
    private SysCouponManageService couponManageService;
    @Resource
    private XWUserInfoService xwUserInfoService;
    @Resource
    private PTCommonDao commonDao;
    @Resource
    private SysBidManageDao bidManageDao;
    @Resource
    private XWAccountDao accountDao;

    @Transactional
    public void doFail(int orderId, String method, String requestNo, String errorCode, String errorMsg) {
        String log = String.format("新网出借预处理失败，requestNo=%s,失败原因:%s,%s", requestNo, errorCode, errorMsg);
        ErrorLogParam errorLogParam = new ErrorLogParam();
        errorLogParam.setMethod(method);
        errorLogParam.setErrorLog(log);
        commonDao.insertErrorLog(errorLogParam);
        requestService.fail(requestNo);
        orderService.fail(orderId);
    }

    @Transactional
    public void doSubmit(int orderId, String requestNo) {
        orderService.submit(orderId);
        requestService.submit(requestNo);
    }

    @Transactional
    public void doSuccess(int orderId, String requestNo, Integer jxqId, ProductTypeForCG productType, String... redpacketIdsArr) throws Exception {
        XWRequest request = requestDao.getByRequestNo(requestNo);
        SystemOrder order = orderManageDao.get(request.getOrderId(), true);//lock up
        if (order != null && order.getOrderStatus().equals(XWOrderStatus.DQR)) {
            SysTenderOrder tenderOrder = bidManageService.getTenderOrder(orderId);
            if (tenderOrder == null) {
                logger.error(String.format("投标回调处理，找不到投标订单(t6504)，orderId=%s,requestNo=%s", orderId, requestNo));
                throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
            }
            if (ProductTypeForCG.BID == productType) {
                XWFundAccount accountInfo = xwUserInfoService.getFundAccount(tenderOrder.getUserId(), SysFundAccountType.XW_INVESTOR_WLZH);
                xwUserInfoService.validateAmount(accountInfo, tenderOrder.getTenderAmount());
            }
            SysBidInfo bidInfo = bidManageService.process(tenderOrder.getBidId(), tenderOrder.getTenderAmount());
            if (bidInfo == null) {
                throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
            }
            addFlows(bidInfo, tenderOrder);
            // 插入投标记录
            SysTenderRecord tenderRecord = new SysTenderRecord();
            tenderRecord.setBidId(bidInfo.F01);
            tenderRecord.setUserId(tenderOrder.getUserId());
            tenderRecord.setTenderAmount(tenderOrder.getTenderAmount());
            tenderRecord.setCancelFlag("F");
            tenderRecord.setAutoFlag("F");
            // 判断计息金额与标总金额是否一致
            if (bidInfo.F05.compareTo(bidInfo.F26) == 0) {
                tenderRecord.setCreditAmount(tenderOrder.getTenderAmount());
            } else {
                tenderRecord.setCreditAmount(bidInfo.F26.multiply(tenderOrder.getTenderAmount()).divide(bidInfo.F05, SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            }
            bidManageDao.addTenderRecord(tenderRecord);
            bidManageDao.updateTenderOrder(tenderOrder.getOrderId(), tenderRecord.getId());
            tenderOrder.setRecordId(tenderRecord.getId());
            //可投金额满足封标条件的时候需要进行判断该标的出借订单是否还有待确认的
            if (BigDecimal.ZERO.compareTo(bidInfo.F07) == 0) {
                int count = bidManageDao.countTenderOrderOfDQR(bidInfo.F01, tenderOrder.getOrderId());
                if (0 == count) {//封标，只有当前标没有确认
                    Map<String, Object> params = new HashMap<>(2);
                    params.put("F01", bidInfo.F01);
                    params.put("F11", new Date());
                    bidManageDao.updateBidExInfo(params);
                    params.remove("F11");
                    params.put("F20", PTProjectState.DFK.name());
                    bidManageDao.updateBid(params);
                } else {
                    logger.info(String.format("暂不能封标%s，还有%s笔投标订单没确认", bidInfo.F01, count));
                }
            }
            XWProjectInfo projectInfo = projectDao.getProjectInfoById(tenderOrder.getBidId());
            XWTenderEntity tenderEntity = new XWTenderEntity();
            tenderEntity.setBidId(bidInfo.F01);
            tenderEntity.setTenderId(tenderOrder.getRecordId());
            tenderEntity.setInvestorPlatformUserNo(UserRole.INVESTOR.getCode() + tenderOrder.getUserId());
            tenderEntity.setBorrowerPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
            tenderEntity.setPreTreatRequestNo(requestNo);
            tenderEntity.setSend(true);
            projectDao.insertTenderEntity(tenderEntity);
            if (redpacketIdsArr != null){
                if (!StringUtils.isEmpty(redpacketIdsArr[0])) {
                    couponManageService.updateRedpacket(CouponState.USED, tenderRecord.getId(), redpacketIdsArr);//将红包改为已使用
                }
            }
            if (jxqId != null && jxqId > 0) {
                couponManageService.updateUserCoupon(jxqId, CouponState.USED, tenderRecord.getId());
            }
            requestService.success(requestNo);
            orderService.success(orderId);
        }
    }

    /**
     * 添加流水
     *
     * @param bidInfo
     * @param tenderOrder
     */
    private void addFlows(SysBidInfo bidInfo, SysTenderOrder tenderOrder) {
        XWFundAccount rzzh;
        int feeCode;
        rzzh = xwUserInfoService.getFundAccount(tenderOrder.getUserId(), SysFundAccountType.XW_INVESTOR_SDZH);
        feeCode = Integer.valueOf(bidInfo.F04 + "" + SysTradeFeeCode.TZ);
        if (rzzh == null) {
            throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
        }
        XWFundAccount czzh = xwUserInfoService.getFundAccount(tenderOrder.getUserId(), SysFundAccountType.XW_INVESTOR_WLZH);//出账账户-出借者往来账户
        if (czzh == null) {
            throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
        }
        List<XWCapitalFlow> flowList = new ArrayList<>();
        // 更新往来帐户余额
        Map<String, Object> wlzhParams = new HashMap<>();
        wlzhParams.put("id", czzh.getId());
        wlzhParams.put("amount", czzh.getAmount().subtract(tenderOrder.getTenderAmount()));
        accountDao.updateFundAccount(wlzhParams);

        //出账流水
        XWCapitalFlow wlzhFlow = new XWCapitalFlow();
        wlzhFlow.setFundAccountId(czzh.getId());
        wlzhFlow.setTadeType(Integer.valueOf(bidInfo.F04 + "" + SysTradeFeeCode.TZ));
        wlzhFlow.setOtherFundAccountId(rzzh.getId());
        wlzhFlow.setExpenditure(tenderOrder.getTenderAmount());
        wlzhFlow.setBalance(czzh.getAmount().subtract(tenderOrder.getTenderAmount()));
        wlzhFlow.setRemark(String.format("散标出借:%s，标题：%s", bidInfo.F25, bidInfo.F03));
        wlzhFlow.setLevel(XWCapitalFlowLevel.YH);
        wlzhFlow.setOrderId(tenderOrder.getOrderId());
        flowList.add(wlzhFlow);

        //更新锁定帐户余额
        Map<String, Object> sdzhParams = new HashMap<>();
        sdzhParams.put("id", rzzh.getId());
        sdzhParams.put("amount", rzzh.getAmount().add(tenderOrder.getTenderAmount()));
        accountDao.updateFundAccount(sdzhParams);

        //入账流水
        XWCapitalFlow sdzhFlow = new XWCapitalFlow();
        sdzhFlow.setFundAccountId(rzzh.getId());
        sdzhFlow.setTadeType(feeCode);
        sdzhFlow.setOtherFundAccountId(czzh.getId());
        sdzhFlow.setIncome(tenderOrder.getTenderAmount());
        sdzhFlow.setBalance(rzzh.getAmount().add(tenderOrder.getTenderAmount()));
        sdzhFlow.setRemark(String.format("散标出借:%s，标题：%s", bidInfo.F25, bidInfo.F03));
        sdzhFlow.setLevel(XWCapitalFlowLevel.YH);
        sdzhFlow.setOrderId(tenderOrder.getOrderId());
        flowList.add(sdzhFlow);
        commonDao.batchInsertT6102(flowList);
    }

    @Transactional
    @Override
    public void doSuccessForPlan(Integer orderId, XWTenderBO tender, XWProjectInfo projectInfo) {
        Integer userId = UserRoleUtil.parseUserNo(tender.getInvestorPlatformUserNo()).getUserId();
        //1 订单回填出借记录
        bidManageDao.updateTenderOrder(orderId, tender.getTenderId());
        //2 添加资金交易流水
        {
            XWFundAccount wlzh = xwUserInfoService.getFundAccount(userId, SysFundAccountType.XW_INVESTOR_WLZH);
            if (wlzh == null) {
                throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
            }
            XWFundAccount sdzh = xwUserInfoService.getFundAccount(userId, SysFundAccountType.XW_INVESTOR_SDZH);
            if (sdzh == null) {
                throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
            }
            List<XWCapitalFlow> flowList = new ArrayList<>();

            String remark = String.format("计划资金解冻，用于出借/复投计划，标出借:%s，标题：%s", projectInfo.getProjectCode(), projectInfo.getProjectName());
            //sdzh出帐
            XWCapitalFlow sdzhOutFlow = new XWCapitalFlow();
            sdzhOutFlow.setFundAccountId(sdzh.getId());
            sdzhOutFlow.setTadeType(Integer.valueOf(projectInfo.getBidType() +""+SysTradeFeeCode.TZ));
            sdzhOutFlow.setOtherFundAccountId(wlzh.getId());
            sdzhOutFlow.setExpenditure(tender.getAmount());
            sdzhOutFlow.setBalance(sdzh.getAmount().subtract(tender.getAmount()));
            sdzhOutFlow.setLevel(XWCapitalFlowLevel.XT);
            sdzhOutFlow.setRemark(remark);
            sdzhOutFlow.setOrderId(orderId);
            flowList.add(sdzhOutFlow);

            //wlzh入账
            XWCapitalFlow wlzhInFlow = new XWCapitalFlow();
            wlzhInFlow.setFundAccountId(wlzh.getId());
            wlzhInFlow.setTadeType(Integer.valueOf(projectInfo.getBidType() +""+SysTradeFeeCode.TZ));
            wlzhInFlow.setOtherFundAccountId(sdzh.getId());
            wlzhInFlow.setIncome(tender.getAmount());
            wlzhInFlow.setBalance(wlzh.getAmount().add(tender.getAmount()));
            wlzhInFlow.setLevel(XWCapitalFlowLevel.XT);
            wlzhInFlow.setRemark(remark);
            wlzhInFlow.setOrderId(orderId);
            flowList.add(wlzhInFlow);

            //sdzh入帐
            XWCapitalFlow sdzhInFlow = new XWCapitalFlow();
            sdzhInFlow.setFundAccountId(sdzh.getId());
            sdzhInFlow.setTadeType(Integer.valueOf(projectInfo.getBidType() +""+SysTradeFeeCode.TZ));
            sdzhInFlow.setOtherFundAccountId(wlzh.getId());
            sdzhInFlow.setIncome(tender.getAmount());
            sdzhInFlow.setBalance(sdzh.getAmount());
            sdzhInFlow.setLevel(XWCapitalFlowLevel.XT);
            sdzhInFlow.setRemark(remark);
            sdzhInFlow.setOrderId(orderId);
            flowList.add(sdzhInFlow);

            //wlzh出账
            XWCapitalFlow wlzhOutFlow = new XWCapitalFlow();
            wlzhOutFlow.setFundAccountId(wlzh.getId());
            wlzhOutFlow.setTadeType(Integer.valueOf(projectInfo.getBidType() +""+SysTradeFeeCode.TZ));
            wlzhOutFlow.setOtherFundAccountId(sdzh.getId());
            wlzhOutFlow.setExpenditure(tender.getAmount());
            wlzhOutFlow.setBalance(wlzh.getAmount());
            wlzhOutFlow.setLevel(XWCapitalFlowLevel.XT);
            wlzhOutFlow.setRemark(remark);
            wlzhOutFlow.setOrderId(orderId);
            flowList.add(wlzhOutFlow);
            commonDao.batchInsertT6102(flowList);
        }
        //3 修改订单状态
        requestService.success(tender.getPreTreatRequestNo());
        orderService.success(orderId);
        //4 修改已发送
        projectDao.updateXWTenderSend(tender.getId(),true);
    }
}
