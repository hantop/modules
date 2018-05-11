package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.coupon.SysCouponManageDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysCancelTenderDao;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.coupon.CouponState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_Cancel;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.project.XWChangeProjectStatusService;
import com.fenlibao.p2p.service.xinwang.trade.XWCancelTenderTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/7/6.
 */
@Service
public class XWCancelTenderTransactionServiceImpl implements XWCancelTenderTransactionService{
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    SysOrderManageDao orderManageDao;

    @Resource
    SysCancelTenderDao cancelTenderDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysOrderService orderService;

    @Resource
    PTCommonDao commonDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    SysCouponManageDao couponManageDao;

    @Resource
    XWChangeProjectStatusService changeProjectStatusService;

    @Override
    @Transactional
    public void finishCancelTender(Integer projectId,Integer orderId,Date currentDate)throws Exception{
        //更新平台标状态
        Map<String,Object> updateProjectParams=new HashMap<>();
        updateProjectParams.put("id", projectId);
        updateProjectParams.put("state", PTProjectState.YLB);
        projectDao.updateProjectInfo(updateProjectParams);
        //更新流标时间
        Map<String,Object> updateProjectExtraInfoParams=new HashMap<>();
        updateProjectExtraInfoParams.put("id", projectId);
        updateProjectExtraInfoParams.put("cancelTenderTime", currentDate);
        projectDao.updateProjectExtraInfo(updateProjectExtraInfoParams);
        //结束流标订单
        orderService.success(orderId);
    }

    @Override
    @Transactional
    public void platformCancelTender(Integer orderId, Integer projectId, XWProjectInfo projectInfo, List<XWTenderRecord> cancelSuccessTenderRecordList)throws Exception{
        Map<Integer,XWFundAccount> investorSDZHMap=new HashMap<>();
        Map<Integer,XWFundAccount> investorWLZHMap=new HashMap<>();
        List<XWCapitalFlow> t6102sToInsert = new ArrayList<>();
        BigDecimal totalTenderAmount= BigDecimal.ZERO;
        List<XWFundAccount> t6101sToUpdate=new ArrayList<>();
        for(XWTenderRecord tenderRecord:cancelSuccessTenderRecordList){
            String remark=String.format("撤销散标出借:%s", tenderRecord.getId());
            //投资人锁定账户
            XWFundAccount investorSDZH = investorSDZHMap.get(tenderRecord.getInvestorId());
            if (investorSDZH == null) {
                investorSDZH = accountDao.getFundAccount(tenderRecord.getInvestorId(), SysFundAccountType.XW_INVESTOR_SDZH);
                investorSDZHMap.put(tenderRecord.getInvestorId(), investorSDZH);
            }
            //投资人往来账户
            XWFundAccount investorWLZH = investorWLZHMap.get(tenderRecord.getInvestorId());
            if (investorWLZH == null) {
                investorWLZH = accountDao.getFundAccount(tenderRecord.getInvestorId(), SysFundAccountType.XW_INVESTOR_WLZH);
                investorWLZHMap.put(tenderRecord.getInvestorId(), investorWLZH);
            }
            //投资人锁定账户支出
            investorSDZH.setAmount(investorSDZH.getAmount().subtract(tenderRecord.getAmount()));
            if (investorSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(), tenderRecord.getInvestorId()));
            }
            //投资人锁定账户流水
            XWCapitalFlow t6102SDZH = new XWCapitalFlow();
            t6102SDZH.setFundAccountId(investorSDZH.getId());
            t6102SDZH.setTadeType(SysTradeFeeCode.TZ_CX);
            t6102SDZH.setOtherFundAccountId(investorWLZH.getId());
            t6102SDZH.setExpenditure(tenderRecord.getAmount());
            t6102SDZH.setBalance(investorSDZH.getAmount());
            t6102SDZH.setRemark(remark);
            t6102SDZH.setProjectId(projectId);
            t6102SDZH.setOrderId(orderId);
            t6102sToInsert.add(t6102SDZH);
            //投资人往来账户收入
            investorWLZH.setAmount(investorWLZH.getAmount().add(tenderRecord.getAmount()));
            //投资人往来账户流水
            XWCapitalFlow t6102WLZH = new XWCapitalFlow();
            t6102WLZH.setFundAccountId(investorWLZH.getId());
            t6102WLZH.setTadeType(SysTradeFeeCode.TZ_CX);
            t6102WLZH.setOtherFundAccountId(investorSDZH.getId());
            t6102WLZH.setIncome(tenderRecord.getAmount());
            t6102WLZH.setBalance(investorWLZH.getAmount());
            t6102WLZH.setRemark(remark);
            t6102WLZH.setProjectId(projectId);
            t6102WLZH.setOrderId(orderId);
            t6102sToInsert.add(t6102WLZH);
            //更新投标记录为取消
            Map<String,Object> updateTenderRecordParams=new HashMap<>();
            updateTenderRecordParams.put("id",tenderRecord.getId());
            updateTenderRecordParams.put("cancel", TenderRecord_Cancel.S);
            projectDao.updateTenderRecordById(updateTenderRecordParams);
            //累加投资金额
            totalTenderAmount=totalTenderAmount.add(tenderRecord.getAmount());
            //退还用户使用的加息券
            XWUserCoupon userCoupon=couponManageDao.getUserCouponByTenderId(tenderRecord.getId());
            if(userCoupon!=null){
                if(2==userCoupon.getCouponStatus()){
                    couponManageDao.updateUserCoupon(userCoupon.getId(), CouponState.UNUSED,null);
                }
            }
        }
        //更新余额
        for(XWFundAccount fundAccount:investorSDZHMap.values()){
            t6101sToUpdate.add(fundAccount);
        }
        for(XWFundAccount fundAccount:investorWLZHMap.values()){
            t6101sToUpdate.add(fundAccount);
        }
        for(XWFundAccount fundAccount:t6101sToUpdate){
            Map<String,Object> updateFundAccountParams=new HashMap<>();
            updateFundAccountParams.put("id",fundAccount.getId());
            updateFundAccountParams.put("amount",fundAccount.getAmount());
            accountDao.updateFundAccount(updateFundAccountParams);
        }
        //插入流水
        if(!t6102sToInsert.isEmpty()){
            commonDao.batchInsertT6102(t6102sToInsert);
        }

        if (PTProjectState.DFK == projectInfo.getState()) {
            // 更新可投金额
            Map<String,Object> updateProjectParams=new HashMap<>();
            updateProjectParams.put("id", projectId);
            updateProjectParams.put("surplusAmount", projectInfo.getSurplusAmount().add(totalTenderAmount));
            projectDao.updateProjectInfo(updateProjectParams);
        }

    }
}
