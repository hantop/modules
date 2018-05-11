package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.coupon.SysCouponManageDao;
import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.dao.xinwang.plan.XWPlanDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.consts.SysVariableConsts;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectConfirmTenderInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.credit.SysCredit_Transfering;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.ProjectType;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_MakeLoan;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanInTransactionService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanTransationService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.UserRoleUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Administrator on 2017/7/6.
 */
@Service
public class XWMakeLoanTransationServiceImpl implements XWMakeLoanTransationService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    PTCommonDao commonDao;

    @Resource
    SysCreditDao creditDao;

    @Resource
    XWPlanDao planDao;

    @Resource
    SysCouponManageDao couponDao;

    @Resource
    SysMakeLoanDao makeLoanDao;

    @Resource
    SysOrderService orderService;

    @Resource
    XWMakeLoanInTransactionService inTransactionService;

    @Resource
    PTCommonService ptCommonService;

    private static BigDecimal resetAmount = new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP);

    @Override
    @Transactional
    public void confirmTenderAcceptFail(XWRequest request,Integer orderId, String resultJson) throws Exception {
        orderService.fail(orderId);
        //保存返回报文
        XWResponseMessage responseParams = new XWResponseMessage();
        responseParams.setBatchNo(request.getBatchNo());
        responseParams.setRequestNo(request.getRequestNo());
        responseParams.setResponseMsg(resultJson);
        requestDao.saveResponseMessage(responseParams);
        //受理失败
        XWRequest updatequestParam = new XWRequest();
        updatequestParam.setRequestNos(request.getRequestNos());
        updatequestParam.setBatchNo(request.getBatchNo());
        updatequestParam.setRequestNo(request.getRequestNo());
        updatequestParam.setState(XWRequestState.SB);
        requestDao.updateRequest(updatequestParam);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> buildBatchs(Integer projectId, List<XWTenderRecord> tenderRecordList, XWProjectRate projectRate, Integer orderId, Integer batchNum,XWProjectInfo projectInfo) throws Exception {
        List<Map<String, Object>> batchList = new ArrayList<>();
        Date requestTime = new Date();

        BigDecimal borrowAmount = projectInfo.getProjectAmount();//借款总金额
        BigDecimal serviceFeeAmount = borrowAmount.multiply(projectRate.getTransactionServiceRate()).setScale(2, RoundingMode.HALF_UP);//平台服务费总额
        BigDecimal pointAmount = borrowAmount.subtract(serviceFeeAmount);//放款金额临界点
        Boolean pointFlag = false;//达到或已经超越临界点的标志
        BigDecimal havaLoanAmount = BigDecimal.ZERO;//已放款金额，只要达到临界点，剩下的批次金额将全部放给平台


        for (int i = 0; i < batchNum; i++) {
            int startIndex = i * XinwangConsts.MAX_REQUESTS_PER_BATCH;
            int endIndex = i * XinwangConsts.MAX_REQUESTS_PER_BATCH + XinwangConsts.MAX_REQUESTS_PER_BATCH;
            endIndex = endIndex > tenderRecordList.size() ? tenderRecordList.size() : endIndex;
            List<XWTenderRecord> tendersInBatch = tenderRecordList.subList(startIndex, endIndex);
            //組裝請求
            String batchNo = XinWangUtil.createRequestNo();

            List<String> requestNos = new ArrayList<>();//该批次里面涉及的多个请求requestNo

            List<Map<String, Object>> bizDetails = new ArrayList<>();
            for (XWTenderRecord tenderRecord : tendersInBatch) {
                String requestNo = XinWangUtil.createRequestNo();
                requestNos.add(requestNo);
                //保存放款请求编号到出借记录表  这里要确保flb.t_xw_tender没有正在放款中的业务订单
                Map<String, Object> saveRequestNoParams = new HashMap<>();
                saveRequestNoParams.put("tenderId", tenderRecord.getId());
                saveRequestNoParams.put("makeLoanRequestNo", requestNo);
                //加锁，再次过滤，一条出借记录不能同时发送多个放款请求
                int result = makeLoanDao.saveMakeLoanRequestNo(saveRequestNoParams);
                if (result == 1) {
                    List<Map<String, Object>> details = new ArrayList<>();
                    havaLoanAmount=havaLoanAmount.add(tenderRecord.getAmount());
                    //固定放款 资金流出借人到借款人账户
                    //业务上只有钱到了借款人账户，出借人才能拥有债权，接下去才能收取借款人的服务费
                    Map<String, Object> toBorrowerMap = new HashMap<>();
                    toBorrowerMap.put("bizType", XWBizType.TENDER.getCode());
                    toBorrowerMap.put("freezeRequestNo", tenderRecord.getPreTreatRequestNo());
                    toBorrowerMap.put("sourcePlatformUserNo", tenderRecord.getInvestorPlatformUserNo());
                    toBorrowerMap.put("targetPlatformUserNo", tenderRecord.getBorrowerPlatformUserNo());
                    toBorrowerMap.put("amount", tenderRecord.getAmount());
                    details.add(toBorrowerMap);

                    if(havaLoanAmount.compareTo(pointAmount)>=0&&projectInfo.getCollectType()==1){
                        if(pointFlag){
                            //给平台
                            Map<String, Object> toServiceFeeMap = new HashMap<>();
                            toServiceFeeMap.put("bizType", XWBizType.COMMISSION.getCode());
                            //toServiceFeeMap.put("freezeRequestNo", tenderRecord.getPreTreatRequestNo());
                            //收取平台服务费必须从借款人账户出账
                            toServiceFeeMap.put("sourcePlatformUserNo", tenderRecord.getBorrowerPlatformUserNo());
                            toServiceFeeMap.put("amount", tenderRecord.getAmount());
                            details.add(toServiceFeeMap);
                            LOG.info("达到临界点出借人放款给平台的："+toServiceFeeMap);
                        }else{
                            if(havaLoanAmount.compareTo(pointAmount)==0){//理想情况，刚好达到临界点，这个出借用户的钱全部给借款人
                                //Map<String, Object> toBorrowerMap = new HashMap<>();
                                //toBorrowerMap.put("bizType", XWBizType.TENDER.getCode());
                                //toBorrowerMap.put("freezeRequestNo", tenderRecord.getPreTreatRequestNo());
                                //toBorrowerMap.put("sourcePlatformUserNo", tenderRecord.getInvestorPlatformUserNo());
                                //toBorrowerMap.put("targetPlatformUserNo", tenderRecord.getBorrowerPlatformUserNo());
                                //toBorrowerMap.put("amount", tenderRecord.getAmount());
                                //details.add(toBorrowerMap);
                                pointFlag = true;//已经达到临界点
                                LOG.info("刚好临界点从出借人放款给借款人的："+toBorrowerMap);
                            }else{
                                //这个出借用户的钱一部分给借款人，一部分给平台
                                BigDecimal  toServiceFee = havaLoanAmount.subtract(pointAmount);


                                //Map<String, Object> toBorrowerMap = new HashMap<>();
                                //toBorrowerMap.put("bizType", XWBizType.TENDER.getCode());
                                //toBorrowerMap.put("freezeRequestNo", tenderRecord.getPreTreatRequestNo());
                                //toBorrowerMap.put("sourcePlatformUserNo", tenderRecord.getInvestorPlatformUserNo());
                                //toBorrowerMap.put("targetPlatformUserNo", tenderRecord.getBorrowerPlatformUserNo());
                                //toBorrowerMap.put("amount", tenderRecord.getAmount());
                                //details.add(toBorrowerMap);
                                LOG.info("临界点，出借人部分钱放款给借款人的部分："+toBorrowerMap);

                                Map<String, Object> toServiceFeeMap = new HashMap<>();
                                toServiceFeeMap.put("bizType", XWBizType.COMMISSION.getCode());
                                //收取平台服务费必须从借款人账户出账
                                toServiceFeeMap.put("sourcePlatformUserNo", tenderRecord.getBorrowerPlatformUserNo());
                                toServiceFeeMap.put("amount", toServiceFee);
                                pointFlag = true;//已经达到临界点
                                details.add(toServiceFeeMap);

                                LOG.info("临界点，出借人部分钱放款给平台的部分："+toServiceFeeMap);
                            }

                        }
                    }
                    //else{
                    //      Map<String, Object> toBorrowerMap = new HashMap<>();
                    //      toBorrowerMap.put("bizType", XWBizType.TENDER.getCode());
                    //      toBorrowerMap.put("freezeRequestNo", tenderRecord.getPreTreatRequestNo());
                    //      toBorrowerMap.put("sourcePlatformUserNo", tenderRecord.getInvestorPlatformUserNo());
                    //      toBorrowerMap.put("targetPlatformUserNo", tenderRecord.getBorrowerPlatformUserNo());
                    //      toBorrowerMap.put("amount", tenderRecord.getAmount());
                    //      details.add(toBorrowerMap);
                    //    LOG.info("从出借人放款给借款人的："+toBorrowerMap);
                    //}

                    Map<String, Object> request = new HashMap<>();
                    request.put("requestNo", requestNo);
                    request.put("tradeType", XWTradeType.TENDER.getCode());
                    request.put("projectNo", "" + projectId);
                    request.put("details", details);
                    bizDetails.add(request);
                    //创建新网订单
                    XWRequest req = new XWRequest();
                    req.setInterfaceName(XinwangInterfaceName.ASYNC_TRANSACTION.getCode());
                    req.setBatchNo(batchNo);
                    req.setRequestNo(requestNo);
                    req.setRequestTime(requestTime);
                    req.setState(XWRequestState.DTJ);
                    req.setOrderId(orderId);
                    requestDao.createRequest(req);

                    //保存请求参数
                    XWResponseMessage requestParams = new XWResponseMessage();
                    requestParams.setRequestNo(requestNo);
                    requestParams.setBatchNo(batchNo);
                    requestParams.setRequestParams(JSON.toJSONString(request));
                    requestDao.saveRequestMessage(requestParams);
                }
            }
            //业务明细存在才加入
            if (!bizDetails.isEmpty()) {
                Map<String, Object> batch = new HashMap<>();
                batch.put("requestNos",requestNos);
                batch.put("batchNo", batchNo);
                batch.put("bizDetails", bizDetails);
                batch.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
                batchList.add(batch);
            }
        }
        return batchList;
    }

    @Override
    @Transactional
    public void platformMakeLoan(String requestNo, Integer orderId) throws Exception {
        /** 待提交状态的订单改为待确认 */
        Map<String, Object> params = new HashMap<>();
        params.put("requestNo", requestNo);
        params.put("state", XWRequestState.CG);
        params.put("preState", XWRequestState.DQR);
        int result = requestDao.updateRequestStatus(params);
        // 只有状态修改DQR订单状态成功才可以进行下面的操作
        if (result != 1) {
            LOG.info("新网回调放款失败requestNo[{}]，订单orderId[{}],",requestNo,orderId);
            throw new XWTradeException(XWResponseCode.ORDER_STATUS_WRONG);
        }
        SysProjectConfirmTenderInfo projectConfirmTenderInfo = makeLoanDao.getProjectConfirmTenderInfoByOrderId(orderId);
        XWProjectInfo projectInfo = projectDao.getProjectInfoById(projectConfirmTenderInfo.getProjectId());
        Integer projectId = Integer.parseInt(projectInfo.getProjectNo());
        if (projectInfo.getState() != PTProjectState.DFK) {
            throw new XWTradeException(XWResponseCode.TRADE_MAKE_A_LOAN_CONDITIONS_NOT_SATISFIED);
        }
        String borrowersRole = UserRoleUtil.parseUserNo(projectInfo.getBorrowerPlatformUserNo()).getUserRole();
        //借款人资金账户
        String borrowerFundAccountType = "XW_" + borrowersRole + "_WLZH";

        XWFundAccount borrowerWLZH = accountDao.getFundAccount(projectInfo.getBorrowerUserId(), SysFundAccountType.parse(borrowerFundAccountType));
        if (borrowerWLZH == null) {
            LOG.info("用户" + projectInfo.getBorrowerPlatformUserNo() + "往来账户不存在");
            throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
        }
        // 新网平台收入账户
        XWFundAccount platformIncomeWLZH = accountDao.getFundAccount(SysCommonConsts.PLATFORM_USER_ID, SysFundAccountType.XW_PLATFORM_INCOME_WLZH);
        if (platformIncomeWLZH == null) {
            throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
        }
        final String remark = "放款";
        Date currentDate = commonDao.getCurrentDate();
        //查出费率
        //XWProjectRate projectRate = projectDao.getProjectRateById(projectId);

        //本次批量交易总放款金额
        BigDecimal totalConfirmTenderAmount = BigDecimal.ZERO;

        Map<Integer, XWFundAccount> investorSDZHMap = new HashMap<>();
        List<XWCapitalFlow> t6102sToInsert = new ArrayList<>();
        List<XWFundAccount> t6101sToUpdate = new ArrayList<>();
        //处理每一笔出借
        //TODO 前置成交服务费的收取流水？
        {
            //出借记录
            XWTenderRecord tenderRecord = makeLoanDao.getTenderRecordByMakeLoanRequestNo(requestNo);
            if (tenderRecord == null) {
                throw new XWTradeException(XWResponseCode.BID_INVEST_RECORD_NOT_EXIST);
            }
            // 锁定出借人锁定账户
            XWFundAccount investorSDZH = investorSDZHMap.get(tenderRecord.getInvestorId());
            if (investorSDZH == null) {
                investorSDZH = accountDao.getFundAccount(tenderRecord.getInvestorId(), SysFundAccountType.XW_INVESTOR_SDZH);
                investorSDZHMap.put(tenderRecord.getInvestorId(), investorSDZH);
            }
            if (investorSDZH == null) {
                throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
            }
            //累加放款金额
            totalConfirmTenderAmount = totalConfirmTenderAmount.add(tenderRecord.getAmount());
            // 扣减锁定账户
            investorSDZH.setAmount(investorSDZH.getAmount().subtract(tenderRecord.getAmount()));
            if (investorSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(), String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(), investorSDZH.getUserId()));
            }
            //出借人流水
            XWCapitalFlow t6102tzr = new XWCapitalFlow();
            t6102tzr.setFundAccountId(investorSDZH.getId());
            t6102tzr.setTadeType(SysTradeFeeCode.TZ);
            t6102tzr.setOtherFundAccountId(borrowerWLZH.getId());
            t6102tzr.setExpenditure(tenderRecord.getAmount());
            t6102tzr.setBalance(investorSDZH.getAmount());
            t6102tzr.setRemark(remark);
            t6102tzr.setProjectId(tenderRecord.getProjectNo());
            t6102tzr.setOrderId(orderId);
            t6102sToInsert.add(t6102tzr);
            // 增加借款人往来账户资金
            borrowerWLZH.setAmount(borrowerWLZH.getAmount().add(tenderRecord.getAmount()));
            //借款人流水
            XWCapitalFlow t6102jkr = new XWCapitalFlow();
            t6102jkr.setFundAccountId(borrowerWLZH.getId());
            t6102jkr.setTadeType(SysTradeFeeCode.JK);
            t6102jkr.setOtherFundAccountId(investorSDZH.getId());
            t6102jkr.setIncome(tenderRecord.getAmount());
            t6102jkr.setBalance(borrowerWLZH.getAmount());
            t6102jkr.setRemark(remark);
            t6102jkr.setProjectId(tenderRecord.getProjectNo());
            t6102jkr.setOrderId(orderId);
            t6102sToInsert.add(t6102jkr);
            // 插入债权
            SysCredit t6251 = new SysCredit();
            t6251.setCode(SysCredit.creditCode(tenderRecord.getId()));
            t6251.setProjectId(tenderRecord.getProjectNo());
            t6251.setCreditorId(tenderRecord.getInvestorId());
            t6251.setPurchasePrice(tenderRecord.getAmount());
            t6251.setOriginCreditAmount(tenderRecord.getAmount());
            t6251.setHoldCreditAmount(tenderRecord.getAmount());
            t6251.setTransfering(SysCredit_Transfering.F);
            t6251.setCreateDate(currentDate);
            t6251.setBearInterestDate(currentDate);
            t6251.setTenderId(tenderRecord.getId());
            creditDao.createCredit(t6251);
            //将债权id回填到用户计划债权表
            Map<String, Object> params1 = new HashMap<>();
            params1.put("tenderId", tenderRecord.getId());
            params1.put("creditId", t6251.getId());
            planDao.fillCreditIdByTenderId(params1);
            // 更新投标记录为已放款
            Map<String, Object> params2 = new HashMap<>();
            params2.put("id", tenderRecord.getId());
            params2.put("makeLoan", TenderRecord_MakeLoan.S);
            projectDao.updateTenderRecordById(params2);

        }


        //是否委托收款
        if (projectInfo.getProjectType() == ProjectType.ENTRUST_PAY) {
            String entrusterRole = UserRoleUtil.parseUserNo(projectInfo.getEntrustPayeePlatformUserNo()).getUserRole();
            XWProjectExtraInfo projectExtraInfo = projectDao.getProjectExtraInfo(projectId);
            //委托收款人资金账户
            String entrustPayeeFundAccountType = "XW_" + entrusterRole + "_WLZH";
            XWFundAccount entrustPayeeWLZH = accountDao.getFundAccount(projectExtraInfo.getEntrustPayeeUserId(), SysFundAccountType.parse(entrustPayeeFundAccountType));
            if (entrustPayeeWLZH == null) {
                LOG.info("用户" + projectInfo.getEntrustPayeePlatformUserNo() + "往来账户不存在");
                throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
            }
            //借款人往来账户减少
            BigDecimal entrustPayAmount = totalConfirmTenderAmount;
            borrowerWLZH.setAmount(borrowerWLZH.getAmount().subtract(entrustPayAmount));
            XWCapitalFlow t6102Borrower = new XWCapitalFlow();
            t6102Borrower.setFundAccountId(borrowerWLZH.getId());
            t6102Borrower.setTadeType(SysTradeFeeCode.ENTRUST_PAY);
            t6102Borrower.setOtherFundAccountId(entrustPayeeWLZH.getId());
            t6102Borrower.setExpenditure(entrustPayAmount);
            t6102Borrower.setBalance(borrowerWLZH.getAmount());
            t6102Borrower.setRemark(String.format("标：%s 委托收款", projectInfo.getProjectName()));
            t6102Borrower.setProjectId(projectId);
            t6102Borrower.setOrderId(orderId);
            t6102sToInsert.add(t6102Borrower);
            //委托收款人往来账户增加
            entrustPayeeWLZH.setAmount(entrustPayeeWLZH.getAmount().add(entrustPayAmount));
            XWCapitalFlow t6102EntrustPayee = new XWCapitalFlow();
            t6102EntrustPayee.setFundAccountId(entrustPayeeWLZH.getId());
            t6102EntrustPayee.setTadeType(SysTradeFeeCode.ENTRUST_PAY);
            t6102EntrustPayee.setOtherFundAccountId(borrowerWLZH.getId());
            t6102EntrustPayee.setIncome(entrustPayAmount);
            t6102EntrustPayee.setBalance(entrustPayeeWLZH.getAmount());
            t6102EntrustPayee.setRemark(String.format("标：%s 委托收款", projectInfo.getProjectName()));
            t6102EntrustPayee.setProjectId(projectId);
            t6102EntrustPayee.setOrderId(orderId);
            t6102sToInsert.add(t6102EntrustPayee);
            t6101sToUpdate.add(entrustPayeeWLZH);
        }
        //批量更新和插入
        t6101sToUpdate.add(platformIncomeWLZH);
        t6101sToUpdate.add(borrowerWLZH);
        for (Map.Entry<Integer, XWFundAccount> entry : investorSDZHMap.entrySet()) {
            t6101sToUpdate.add(entry.getValue());
        }
        for (XWFundAccount funcAccount : t6101sToUpdate) {
            Map<String, Object> fundAccountParams = new HashMap<>();
            fundAccountParams.put("userId", funcAccount.getUserId());
            fundAccountParams.put("type", funcAccount.getFundAccountType());
            fundAccountParams.put("amount", funcAccount.getAmount());
            accountDao.updateFundAccount(fundAccountParams);
        }
        commonDao.batchInsertT6102(t6102sToInsert);
        //开始结束时间
        Date bearInterestDate = currentDate;
        Date endDate;
        Date nextRepayDate;
        if (projectInfo.getMonthProjectPeriod() == 0) { // 按天借款
            endDate = new Date(DateUtil.dateAdd(bearInterestDate.getTime(), projectInfo.getDayProjectPeriod()));
            nextRepayDate = endDate;
        } else {
            endDate = new Date(DateUtil.rollNaturalMonth(bearInterestDate.getTime(), projectInfo.getMonthProjectPeriod()));
            nextRepayDate = new Date(DateUtil.rollNaturalMonth(bearInterestDate.getTime(), 1));
        }
        //更新标扩展信息t6231
        Map<String, Object> t6231params = new HashMap<>();
        t6231params.put("id", projectId);
        t6231params.put("bidConfirmTime", currentDate);
        t6231params.put("bearInterestDate", bearInterestDate);
        t6231params.put("endDate", endDate);
        t6231params.put("nextRepayDate", nextRepayDate);
        projectDao.updateProjectExtraInfo(t6231params);
        //修改订单
        orderService.success(orderId);
    }

    @Transactional
    @Override
    public void generateRepaymentPlan(XWProjectInfo projectInfo, Date bearInterestDate, Date endDate, XWProjectExtraInfo extraInfo, XWProjectRate projectRate) throws Exception {

        Integer projectId = Integer.parseInt(projectInfo.getProjectNo());
        Map<String,Object> projectInfoParams=new HashMap<>();
        projectInfoParams.put("id",projectId);
        projectInfoParams.put("state", PTProjectState.HKZ);
        projectInfoParams.put("preState", PTProjectState.DFK);//前一个状态必须是DFK
        int result = projectDao.updateProjectStatus(projectInfoParams);
        if (result != 1) {
            throw new XWTradeException(XWResponseCode.TRADE_MAKE_A_LOAN_CONDITIONS_NOT_SATISFIED);
        }
        List<SysCredit> credits = creditDao.getCreditInfoByProjectId(projectId);

        inTransactionService.addXWCapitalFlowOfServiceFee(projectInfo,extraInfo);//前置收费，则添加收取平台服务费的流水记录

        if(!CollectionUtils.isEmpty(credits)){//生成平台服务费的还款计划
            inTransactionService.addXWRepaymentPlanWithServiceFee(projectInfo,credits.get(0),bearInterestDate,projectRate,extraInfo);

        }


        switch (projectInfo.getRepaymentWay()) {
            case DEBX: {
                for (SysCredit credit : credits) {
                    List<XWRepaymentPlan> repaymentPlan = repaymentPlanDEBX(projectInfo, credit, bearInterestDate,projectRate);
                    projectDao.batchInsertRepaymentPlan(repaymentPlan);
                }

                break;
            }
            case MYFX: {
                for (SysCredit credit : credits) {
                    List<XWRepaymentPlan> repaymentPlan = repaymentPlanMYFX(projectInfo, credit, bearInterestDate,projectRate);
                    projectDao.batchInsertRepaymentPlan(repaymentPlan);
                }
                break;
            }
            case YCFQ: {
                for (SysCredit credit : credits) {
                    List<XWRepaymentPlan> repaymentPlan = repaymentPlanYCFQ(projectInfo, credit, endDate,projectRate);
                    projectDao.batchInsertRepaymentPlan(repaymentPlan);
                }
                break;
            }
            default:
                LOG.error("标" + projectInfo.getProjectName() + "不支持的还款方式:" + projectInfo.getRepaymentWay().getName());
                throw new RuntimeException("不支持的还款方式");
        }

    }

    private List<XWRepaymentPlan> repaymentPlanDEBX(XWProjectInfo projectInfo, SysCredit credit, Date bearInterestDate, XWProjectRate projectRate) throws Exception {
        List<XWRepaymentPlan> t6252s = new ArrayList<>();
        //月利率
        BigDecimal monthRate = projectRate.getRepaymentRate().setScale(SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        //剩余本金
        BigDecimal remainTotal = credit.getHoldCreditAmount();
        //每月本息和
        BigDecimal monthPayTotal = debx(credit.getHoldCreditAmount(), monthRate, projectInfo.getMonthProjectPeriod());

        //加息券
        XWUserCoupon userCoupon = couponDao.getUserCouponByTenderId(credit.getTenderId());
        //每月加息
        BigDecimal monthlyRaiseLx = BigDecimal.ZERO;
        if (userCoupon != null) {
            //加息后月利率
            BigDecimal monthRate2 = projectRate.getRepaymentRate().add(userCoupon.getScope()).setScale(SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                    SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            //加息后每月本息和
            BigDecimal monthPayTotal2 = debx(credit.getHoldCreditAmount(), monthRate2, projectInfo.getMonthProjectPeriod());
            monthlyRaiseLx = monthPayTotal2.subtract(monthPayTotal);
        }

        //标加息
        XWProjectRate t6238 = projectDao.getProjectRateById(Integer.parseInt(projectInfo.getProjectNo()));
        BigDecimal bidMonthlyRaiseLx = BigDecimal.ZERO;
        if (t6238.getProjectRaiseInterestRate().compareTo(BigDecimal.ZERO) > 0) {
            //加息后月利率
            BigDecimal monthRate3 = projectRate.getRepaymentRate().add(t6238.getProjectRaiseInterestRate()).setScale(SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                    SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            //加息后每月本息和
            BigDecimal monthPayTotal3 = debx(credit.getHoldCreditAmount(), monthRate3, projectInfo.getMonthProjectPeriod());
            bidMonthlyRaiseLx = monthPayTotal3.subtract(monthPayTotal);
        }

        HashMap<Integer,List<XWRepaymentPlan>> map = new HashMap<>();//用于后面计算利息管理费 key期号，value，计划列表

        for (int term = 1; term <= projectInfo.getMonthProjectPeriod(); term++) {

            List<XWRepaymentPlan> list =new ArrayList<XWRepaymentPlan>();//仅放入利息及加息
            Date date = new Date(DateUtil.rollNaturalMonth(bearInterestDate.getTime(), term));
            //当月利息
            BigDecimal interest = remainTotal.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            //更新下次还款
            if (1 == term) {
                Map<String, Object> params = new HashMap<>();
                params.put("id", Integer.parseInt(projectInfo.getProjectNo()));
                params.put("nextRepayDate", date);
                projectDao.updateProjectExtraInfo(params);
            }

            {
                // 利息
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(projectInfo.getBorrowerUserId());
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.TZ_LX);
                t6252.setTerm(term);
                if (projectInfo.getMonthProjectPeriod() == term) {
                    t6252.setAmount(monthPayTotal.subtract(remainTotal).compareTo(BigDecimal.ZERO) > 0
                            ? monthPayTotal.subtract(remainTotal) : BigDecimal.ZERO);
                } else {
                    t6252.setAmount(interest);
                }
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                //如果利息计算结果为0,强制 0.01 and God bless American(flb)
                if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                    t6252.setAmount(resetAmount);
                }
//                t6252s.add(t6252);在这里不加入，后面重新计算利息管理费add进来
                list.add(t6252);
            }
            if (userCoupon != null) {
                //加息
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(SysCommonConsts.PLATFORM_USER_ID);
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.TZ_JX);
                t6252.setTerm(term);
                t6252.setAmount(monthlyRaiseLx);
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                //如果利息计算结果为0,强制 0.01 and God bless American(flb)
                if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                    t6252.setAmount(resetAmount);
                }
                t6252s.add(t6252);
                list.add(t6252);
            }
            if (t6238.getProjectRaiseInterestRate().compareTo(BigDecimal.ZERO) > 0) {
                //标加息
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(SysCommonConsts.PLATFORM_USER_ID);
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.BID_JX);
                t6252.setTerm(term);
                t6252.setAmount(bidMonthlyRaiseLx);
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                //如果利息计算结果为0,强制 0.01 and God bless American(flb)
                if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                    t6252.setAmount(resetAmount);
                }
                t6252s.add(t6252);
                list.add(t6252);
            }
            {
                // 本金
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(projectInfo.getBorrowerUserId());
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.TZ_BJ);
                t6252.setTerm(term);
                if (projectInfo.getMonthProjectPeriod() == term) {
                    t6252.setAmount(remainTotal);
                } else {
                    t6252.setAmount(monthPayTotal.subtract(interest));
                }
                remainTotal = remainTotal.subtract(t6252.getAmount());
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                t6252s.add(t6252);
            }
            map.put(term,list);
        }
        for (List<XWRepaymentPlan> repaymentPlanThisCredit : map.values()) {
            this.modifyXWRepaymentPlanWithInterestManagement(repaymentPlanThisCredit,t6252s,t6238);//重新计算利息管理费
        }
        return t6252s;
    }






    private BigDecimal debx(BigDecimal total, BigDecimal monthRate, int terms) {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
        return total.multiply(monthRate).multiply(tmp).divide(tmp.subtract(new BigDecimal(1)), 2,
                BigDecimal.ROUND_HALF_UP);
    }

    private ArrayList<XWRepaymentPlan> repaymentPlanMYFX(XWProjectInfo projectInfo, SysCredit credit, Date bearInterestDate, XWProjectRate projectRate) throws Exception {
        ArrayList<XWRepaymentPlan> t6252s = new ArrayList<>();
        BigDecimal monthes = new BigDecimal(12);
        // 总利息（不包括加息）
        BigDecimal totalExlastLx = (credit.getHoldCreditAmount().multiply(projectRate.getRepaymentRate()).divide(monthes, SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_DOWN)
                .multiply(new BigDecimal(projectInfo.getMonthProjectPeriod()))).setScale(2, BigDecimal.ROUND_HALF_UP);

        XWUserCoupon userCoupon = couponDao.getUserCouponByTenderId(credit.getTenderId());
        // 总加息券加息
        BigDecimal totalRaiseLx = BigDecimal.ZERO;
        if (userCoupon != null) {
            totalRaiseLx = (credit.getHoldCreditAmount().multiply(userCoupon.getScope()).divide(monthes, SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_DOWN)
                    .multiply(new BigDecimal(projectInfo.getMonthProjectPeriod()))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        // 总标加息
        XWProjectRate t6238 = projectDao.getProjectRateById(Integer.parseInt(projectInfo.getProjectNo()));
        BigDecimal bidTotalRaiseLx = BigDecimal.ZERO;
        if (t6238.getProjectRaiseInterestRate().compareTo(BigDecimal.ZERO) > 0) {
            bidTotalRaiseLx = (credit.getHoldCreditAmount().multiply(t6238.getProjectRaiseInterestRate()).divide(monthes, SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_DOWN)
                    .multiply(new BigDecimal(projectInfo.getMonthProjectPeriod()))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        HashMap<Integer,List<XWRepaymentPlan>> map = new HashMap<>();//用于后面计算利息管理费
        for (int term = 1; term <= projectInfo.getMonthProjectPeriod(); term++) {
            Date date = new Date(DateUtil.rollNaturalMonth(bearInterestDate.getTime(), term));
            if (1 == term) {
                Map<String, Object> params = new HashMap<>();
                params.put("id", Integer.parseInt(projectInfo.getProjectNo()));
                params.put("nextRepayDate", date);
                projectDao.updateProjectExtraInfo(params);
            }

            List<XWRepaymentPlan> list =new ArrayList<XWRepaymentPlan>();//仅放利息及加息


            {
                // 利息
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(projectInfo.getBorrowerUserId());
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.TZ_LX);
                t6252.setTerm(term);
                t6252.setAmount(credit.getHoldCreditAmount().multiply(projectRate.getRepaymentRate()).divide(monthes, 2, BigDecimal.ROUND_DOWN));
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                if (term < projectInfo.getMonthProjectPeriod()) {
                    totalExlastLx = totalExlastLx.subtract(t6252.getAmount());
                }
                if (term == projectInfo.getMonthProjectPeriod()) {
                    t6252.setAmount(totalExlastLx);
                }
                //如果利息计算结果为0,强制 0.01 and God bless American(flb)
                if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                    t6252.setAmount(resetAmount);
                }
//                t6252s.add(t6252);//后面再加入
                list.add(t6252);
            }
            if (userCoupon != null) {
                // 加息券加息
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(SysCommonConsts.PLATFORM_USER_ID);
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.TZ_JX);
                t6252.setTerm(term);
                t6252.setAmount(credit.getHoldCreditAmount().multiply(userCoupon.getScope()).divide(monthes, 2, BigDecimal.ROUND_DOWN));
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                if (term < projectInfo.getMonthProjectPeriod()) {
                    totalRaiseLx = totalRaiseLx.subtract(t6252.getAmount());
                }
                if (term == projectInfo.getMonthProjectPeriod()) {
                    t6252.setAmount(totalRaiseLx);
                }
                //如果利息计算结果为0,强制 0.01 and God bless American(flb)
                if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                    t6252.setAmount(resetAmount);
                }
                list.add(t6252);
                t6252s.add(t6252);
            }
            if (t6238.getProjectRaiseInterestRate().compareTo(BigDecimal.ZERO) > 0) {
                // 标加息
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(SysCommonConsts.PLATFORM_USER_ID);
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.BID_JX);
                t6252.setTerm(term);
                t6252.setAmount(credit.getHoldCreditAmount().multiply(t6238.getProjectRaiseInterestRate()).divide(monthes, 2, BigDecimal.ROUND_DOWN));
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                if (term < projectInfo.getMonthProjectPeriod()) {
                    bidTotalRaiseLx = bidTotalRaiseLx.subtract(t6252.getAmount());
                }
                if (term == projectInfo.getMonthProjectPeriod()) {
                    t6252.setAmount(bidTotalRaiseLx);
                }
                //如果利息计算结果为0,强制 0.01 and God bless American(flb)
                if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                    t6252.setAmount(resetAmount);
                }
                list.add(t6252);
                t6252s.add(t6252);
            }
            if (term == projectInfo.getMonthProjectPeriod()) {
                // 本金
                XWRepaymentPlan t6252 = new XWRepaymentPlan();
                t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
                t6252.setPayerId(projectInfo.getBorrowerUserId());
                t6252.setPayeeId(credit.getCreditorId());
                t6252.setFeeType(SysTradeFeeCode.TZ_BJ);
                t6252.setTerm(term);
                t6252.setAmount(credit.getHoldCreditAmount());
                t6252.setDueDate(date);
                t6252.setRepayState(RepaymentPlan_RepayState.WH);
                t6252.setActualRepayTime(null);
                t6252.setCreditId(credit.getId());
                t6252s.add(t6252);
            }

            map.put(term,list);
        }
        //填入总期数和剩余期数
        Map<String, Object> params = new HashMap<>();
        params.put("totalTerms", projectInfo.getMonthProjectPeriod());
        params.put("remainTerms", projectInfo.getMonthProjectPeriod());
        params.put("id", Integer.parseInt(projectInfo.getProjectNo()));
        projectDao.updateProjectExtraInfo(params);

        for (List<XWRepaymentPlan> repaymentPlanThisCredit : map.values()) {
            this.modifyXWRepaymentPlanWithInterestManagement(repaymentPlanThisCredit, t6252s, t6238);//重新计算利息管理费
        }
        return t6252s;
    }

    private ArrayList<XWRepaymentPlan> repaymentPlanYCFQ(XWProjectInfo projectInfo, SysCredit credit, Date endDate, XWProjectRate projectRate) throws Exception {
        int days = projectInfo.getDayProjectPeriod(); // 天标借款天数
        // 更新下个还款日
        Map<String, Object> t6231params = new HashMap<>();
        t6231params.put("id", Integer.parseInt(projectInfo.getProjectNo()));
        t6231params.put("nextRepayDate", endDate);
        projectDao.updateProjectExtraInfo(t6231params);

        XWUserCoupon userCoupon = couponDao.getUserCouponByTenderId(credit.getTenderId());
        XWProjectRate t6238 = projectDao.getProjectRateById(Integer.parseInt(projectInfo.getProjectNo()));

        HashMap<Integer,List<XWRepaymentPlan>> map = new HashMap<>();//用于后面计算利息管理费

        List<XWRepaymentPlan> list =new ArrayList<XWRepaymentPlan>();//仅放入利息，加息券加息，标加息
        ArrayList<XWRepaymentPlan> t6252s = new ArrayList<>();
        {
            // 利息
            XWRepaymentPlan t6252 = new XWRepaymentPlan();
            t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
            t6252.setPayerId(projectInfo.getBorrowerUserId());
            t6252.setPayeeId(credit.getCreditorId());
            t6252.setFeeType(SysTradeFeeCode.TZ_LX);
            t6252.setTerm(1);
            t6252.setAmount(credit.getHoldCreditAmount().setScale(SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            if (projectInfo.getMonthProjectPeriod() == 0) {
                t6252.setAmount(t6252.getAmount().multiply(projectRate.getRepaymentRate()).multiply(new BigDecimal(days)).divide(
                        new BigDecimal(365), SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            } else {
                t6252.setAmount(t6252.getAmount().multiply(projectRate.getRepaymentRate()).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod())).divide(new BigDecimal(12),
                        SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            }
            //如果利息计算结果为0,强制 0.01 and God bless American(flb)
            if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                t6252.setAmount(resetAmount);
            }
            t6252.setDueDate(endDate);
            t6252.setRepayState(RepaymentPlan_RepayState.WH);
            t6252.setActualRepayTime(null);
            t6252.setCreditId(credit.getId());
//            t6252s.add(t6252);//后面调整了利息
            list.add(t6252);
        }
        if (userCoupon != null) {
            // 加息券加息
            XWRepaymentPlan t6252 = new XWRepaymentPlan();
            t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
            t6252.setPayerId(SysCommonConsts.PLATFORM_USER_ID);
            t6252.setPayeeId(credit.getCreditorId());
            t6252.setFeeType(SysTradeFeeCode.TZ_JX);
            t6252.setTerm(1);
            t6252.setAmount(credit.getHoldCreditAmount().setScale(9, BigDecimal.ROUND_HALF_UP));
            if (projectInfo.getMonthProjectPeriod() == 0) {
                t6252.setAmount(t6252.getAmount().multiply(userCoupon.getScope()).multiply(new BigDecimal(days)).divide(
                        new BigDecimal(365), SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            } else {
                t6252.setAmount(t6252.getAmount().multiply(userCoupon.getScope()).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod()))
                        .divide(new BigDecimal(12), SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            }
            //如果利息计算结果为0,强制 0.01 and God bless American(flb)
            if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                t6252.setAmount(resetAmount);
            }
            t6252.setDueDate(endDate);
            t6252.setRepayState(RepaymentPlan_RepayState.WH);
            t6252.setActualRepayTime(null);
            t6252.setCreditId(credit.getId());
            t6252s.add(t6252);
            list.add(t6252);
        }
        if (t6238.getProjectRaiseInterestRate().compareTo(BigDecimal.ZERO) > 0) {
            // 标加息
            XWRepaymentPlan t6252 = new XWRepaymentPlan();
            t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
            t6252.setPayerId(SysCommonConsts.PLATFORM_USER_ID);
            t6252.setPayeeId(credit.getCreditorId());
            t6252.setFeeType(SysTradeFeeCode.BID_JX);
            t6252.setTerm(1);
            t6252.setAmount(credit.getHoldCreditAmount().setScale(SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            if (projectInfo.getMonthProjectPeriod() == 0) {
                t6252.setAmount(t6252.getAmount().multiply(t6238.getProjectRaiseInterestRate()).multiply(new BigDecimal(days)).divide(
                        new BigDecimal(365), SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            } else {
                t6252.setAmount(t6252.getAmount().multiply(t6238.getProjectRaiseInterestRate()).multiply(new BigDecimal(projectInfo.getMonthProjectPeriod()))
                        .divide(new BigDecimal(12), SysCommonConsts.DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            }
            //如果利息计算结果为0,强制 0.01 and God bless American(flb)
            if (resetAmount.compareTo(t6252.getAmount()) >= 0) {
                t6252.setAmount(resetAmount);
            }
            t6252.setDueDate(endDate);
            t6252.setRepayState(RepaymentPlan_RepayState.WH);
            t6252.setActualRepayTime(null);
            t6252.setCreditId(credit.getId());
            t6252s.add(t6252);
            list.add(t6252);
        }
        {
            // 本金
            XWRepaymentPlan t6252 = new XWRepaymentPlan();
            t6252.setProjectId(Integer.parseInt(projectInfo.getProjectNo()));
            t6252.setPayerId(projectInfo.getBorrowerUserId());
            t6252.setPayeeId(credit.getCreditorId());
            t6252.setFeeType(SysTradeFeeCode.TZ_BJ);
            t6252.setTerm(1);
            t6252.setAmount(credit.getHoldCreditAmount());
            t6252.setDueDate(endDate);
            t6252.setRepayState(RepaymentPlan_RepayState.WH);
            t6252.setActualRepayTime(null);
            t6252.setCreditId(credit.getId());
            t6252s.add(t6252);
        }

        map.put(1,list);

        for (List<XWRepaymentPlan> repaymentPlanThisCredit : map.values()) {
            this.modifyXWRepaymentPlanWithInterestManagement(repaymentPlanThisCredit, t6252s, t6238);//重新计算利息管理费
        }

        return t6252s;
    }

    /**
     * 2017/09/19 屏蔽放款短信/站内信
     */
    @Override
//    @Transactional
    public void sendLetterAndMsg(XWProjectInfo projectInfo) {

//        if("0001".equals(projectInfo.getProjectSource())){
//            try{
//                PlatformAccount platformAccount= accountDao.getPlatformAccountInfoByPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
//                XinwangAccount borrowerXinwangAccount=accountDao.getXinwangAccount(projectInfo.getBorrowerPlatformUserNo());
//                XWFundAccount borrowerFundAccount=accountDao.getFundAccount(projectInfo.getBorrowerUserId(), SysFundAccountType.parse("XW_"+borrowerXinwangAccount.getUserRole().name()+"_WLZH"));
//                //站内信
//                String letterTemplate=ptCommonDao.getSystemVariable(SysVariableConsts.CONFIRM_TENDER_SUCCESS_LETTER);
//                letterTemplate=letterTemplate.replace("${amount}", projectInfo.getProjectAmount().subtract(projectInfo.getSurplusAmount()).toString());
//                letterTemplate=letterTemplate.replace("${title}", projectInfo.getProjectName());
//                letterTemplate=letterTemplate.replace("${balance}", borrowerFundAccount.getAmount().toString());
//                ptCommonService.sendLetter(platformAccount.getUserId(), "放款成功", letterTemplate);
//                //短信
//                String msgTemplate=ptCommonDao.getSystemVariable(SysVariableConsts.CONFIRM_TENDER_SUCCESS_MSG);
//                msgTemplate=msgTemplate.replace("${amount}", projectInfo.getProjectAmount().subtract(projectInfo.getSurplusAmount()).toString());
//                msgTemplate=msgTemplate.replace("${title}", projectInfo.getProjectName());
//                msgTemplate=msgTemplate.replace("${balance}", borrowerFundAccount.getAmount().toString());
//                ptCommonService.sendMsg(platformAccount.getMobile(),msgTemplate, SysMsgSendType.ACTIVE.getValue());
//            }catch(Exception e){
//                LOG.error("标"+projectInfo.getProjectName()+"放款后发送信息出错："+e.getMessage(),e);
//            }
//        }
    }

    /**
     * 还款计划添加利息管理费
     * 利息管理费本质应该是出借用户给平台，实际操作逻辑是：在原本应给用户的利息中扣除利息服务费，扣掉的利息服务费由借款人给平台
     * 原本资金流向逻辑（利息服务费：出借人-->平台），实际资金流向逻辑（利息服务费：借款人-->平台)
     * @param t6252s 原本的还款计划
     * @param repaymentPlanThisCredit 当前债权
     *@param xWProjectRate  @return
     */
    private List<XWRepaymentPlan>  modifyXWRepaymentPlanWithInterestManagement(List<XWRepaymentPlan> repaymentPlanThisCredit, List<XWRepaymentPlan> t6252s, XWProjectRate xWProjectRate) {


        int creditId= 0;
        List<XWRepaymentPlan> result = new ArrayList<>();
        BigDecimal rate = xWProjectRate.getInterestManagementRate();//利息管理费率
        BigDecimal interestAmount = BigDecimal.ZERO;//利息总和
        XWRepaymentPlan tz_lx = null;//出借利息
        for (XWRepaymentPlan plan: repaymentPlanThisCredit) {
            creditId = plan.getCreditId();
            LOG.debug("债权【{}】，交易类型【{}】，金额【{}】",plan.getCreditId(),plan.getFeeType(),plan.getAmount());

            if(plan.getFeeType()==SysTradeFeeCode.TZ_LX||plan.getFeeType()==SysTradeFeeCode.TZ_JX||plan.getFeeType()==SysTradeFeeCode.BID_JX){
                interestAmount=interestAmount.add(plan.getAmount());
                if(plan.getFeeType()==SysTradeFeeCode.TZ_LX){
                    tz_lx = plan;
                }
            }
        }
        LOG.debug("债权【{}】，计算利息服务费的总金额【{}】",creditId,interestAmount);
        BigDecimal managementFee = interestAmount.multiply(rate).setScale(3, BigDecimal.ROUND_DOWN);;//先截取三位
        managementFee=managementFee.setScale(2,BigDecimal.ROUND_UP);//千分位进位管理费

        XWRepaymentPlan interestManagement = new XWRepaymentPlan();
        BeanUtils.copyProperties(tz_lx,interestManagement);//利息管理费  信息与出借利息的基本相同
        interestManagement.setId(null);
        interestManagement.setPayeeId(SysCommonConsts.PLATFORM_USER_ID);//收款人为平台
        interestManagement.setFeeType(SysTradeFeeCode.LX_GLF);//交易类型
        interestManagement.setAmount(managementFee);//利息管理费
        result.add(interestManagement);
        tz_lx.setAmount(tz_lx.getAmount().subtract(managementFee));
        result.add(tz_lx);


        t6252s.addAll(result);
        return result;
    }

    @Override
    @Transactional
    public void sendLetterAfterMakeLoan(XWProjectInfo projectInfo) {
        HashMap parameter = new HashMap();
        parameter.put("projectNo",projectInfo.getProjectNo());
        List<XWTenderRecord> records = this.projectDao.getTenderRecord(parameter);
        HashMap sentMap = new HashMap();//已经发送的用户
        String title="起息通知";
        String content=commonDao.getSystemVariable(SysVariableConsts.LETTER_AFTER_MAKELOAN);
        content=content.replace("${title}", projectInfo.getProjectName());
        for (XWTenderRecord record:records) {
            if(!sentMap.containsKey(record.getInvestorId())){
                try {
                    sentMap.put(record.getInvestorId(),record.getInvestorId());//存进已发送
                    ptCommonService.sendLetter(record.getInvestorId(),title,content);
                    if(LOG.isDebugEnabled()){
                        LOG.debug("放款成功后，给出借用户发送站内消息，用户id：【{}】，内容【{}】",record.getInvestorId(),content);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
