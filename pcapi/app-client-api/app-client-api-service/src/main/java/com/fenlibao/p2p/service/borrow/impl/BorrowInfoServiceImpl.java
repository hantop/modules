package com.fenlibao.p2p.service.borrow.impl;

import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.common.DateUtil;
import com.fenlibao.p2p.dao.borrow.BorrowInfoDao;
import com.fenlibao.p2p.model.entity.borrow.*;
import com.fenlibao.p2p.model.entity.finacing.BorrowAccountInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentBidInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.model.vo.borrow.ForwardRepayInfoVO;
import com.fenlibao.p2p.service.borrow.BorrowInfoService;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 借款相关
 * Created by xiao on 2016/12/26.
 */
@Service
public class BorrowInfoServiceImpl implements BorrowInfoService {

    @Resource
    BorrowInfoDao rorrowInfoDao;

    /**
     * 获取借款列表 还款中和逾期
     *
     * @param userId
     * @param status
     * @return
     */
    public List<BorrowInfo> getBorrowInfoList(int userId, String status, PageBounds pageBounds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("status", status);
        return rorrowInfoDao.getBorrowInfoList(map, pageBounds);
    }

    /**
     * 获取借款列表 已还清
     *
     * @param userId
     * @return
     */
    public List<BorrowInfo> getYHQBorrowInfoList(int userId, PageBounds pageBounds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return rorrowInfoDao.getYHQBorrowInfoList(map, pageBounds);
    }


    /**
     * 获取未还统计
     *
     * @param userId
     * @return
     */
    public BorrowStaticsInfo getStayRepayStatics(int userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return rorrowInfoDao.getStayRepayStatics(map);
    }

    /**
     * 还款信息
     *
     * @param userId
     * @return
     */
    public RepayInfo getCurrentRepayStatics(int userId, String bidId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bidId", bidId);
        return rorrowInfoDao.getCurrentRepayStatics(map);
    }

    /**
     * 提前还款信息
     * @param userId
     * @param bidId
     * @return
     */
    @Override
    public ForwardRepayInfoVO getPreRepayStatics(int userId, String bidId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bidId", bidId);
        List<ForwardRepayInfo> list = rorrowInfoDao.getPreRepayStatics(map);
        if(list.size() > 0){
            return preRepayStatics(list);
        }else{
            throw new Exception();
        }
    }

    public ForwardRepayInfoVO preRepayStatics(List<ForwardRepayInfo> repayInfoList) throws Exception {
        ForwardRepayInfo repayInfo1 = repayInfoList.get(0);

        Date now = repayInfo1.getCurrentDate();
        BigDecimal currentInterest = new BigDecimal(0); //利息
        BigDecimal sybj = new BigDecimal(0); //剩余本金
        BigDecimal loanPenalAmount = BigDecimal.ZERO;

        int daysOfCycle = 0; //借款周期
        int loanDays = 0;  //借款总天数
        int minTerm = repayInfo1.getMinTerm(); //当前期号
        Date starttime = repayInfo1.getStarttime();
        Date endtime = repayInfo1.getEndtime();

        if (repayInfo1.getLoanCycleForMonth() > 0) {
            daysOfCycle = DateUtil.daysOfTwo(starttime, endtime);
        } else {
            daysOfCycle = repayInfo1.getLoanCycleForDay();
        }
        loanDays = DateUtil.daysOfTwo(starttime, now);
        //针对次日启息和当天按提前还款
        if (loanDays < 0) {
            loanDays = 0;
        }
        int leftDays = daysOfCycle - loanDays;
        int lastNaturalMonthDays = DateUtil.daysOfLastNaturalMonth(starttime, daysOfCycle);

        int preTermsDaySum = DateUtil.sumDaysOfNaturalMonth(starttime, minTerm - 1);
        int currentTermLoanDays = loanDays - preTermsDaySum;

        for (ForwardRepayInfo repayInfo : repayInfoList) {
            int currentTerm = repayInfo.getTerm();
            int typeId = repayInfo.getTypeId();
            BigDecimal amount = repayInfo.getAmount();

            //计算当前期的本息
            if (currentTerm == minTerm) {
                if (FeeCode.TZ_LX == typeId) {
                    //利息
                    BigDecimal sybjOfDebt = repayInfo.getSybj();
                    if (daysOfCycle == 0) {
                        amount = BigDecimal.ZERO;
                    } else {
                        amount = sybjOfDebt.multiply(repayInfo.getLoanRate()).divide(new BigDecimal(365), 9, RoundingMode.HALF_UP).multiply(new BigDecimal(currentTermLoanDays)).setScale(2, RoundingMode.HALF_UP);
                    }
                    currentInterest = currentInterest.add(amount);
                    //计算违约金
                    BigDecimal penalOfOneDebt = sybjOfDebt.multiply(repayInfo.getLoanRate()).divide(new BigDecimal(365), 9, RoundingMode.HALF_UP);
                    if (leftDays > lastNaturalMonthDays) {
                        penalOfOneDebt = penalOfOneDebt.multiply(new BigDecimal(30)).setScale(2, RoundingMode.HALF_UP);
                    } else if (leftDays > 7) {
                        penalOfOneDebt = penalOfOneDebt.multiply(new BigDecimal(leftDays - 7)).setScale(2, RoundingMode.HALF_UP);
                    } else {
                        penalOfOneDebt = BigDecimal.ZERO;
                    }
                    loanPenalAmount = loanPenalAmount.add(penalOfOneDebt);
                }
            }
            //计算剩余本金
            else if (repayInfo.getTerm() > minTerm) {
                if (FeeCode.TZ_BJ == typeId) {
                    sybj = sybj.add(amount);
                }
            }
        }

        ForwardRepayInfoVO repayInfo = new ForwardRepayInfoVO();
        repayInfo.setLoanId(repayInfo1.getLoanId());//id
        repayInfo.setTerm(minTerm);//当前期号
        repayInfo.setSybj(sybj);//剩余本金
        repayInfo.setLoanPenalAmount(loanPenalAmount);//违约金
        repayInfo.setCurrentInterest(currentInterest);//当期应还利息
        repayInfo.setCurrentPrincipal(repayInfo1.getCurrentPrincipal());//当期应还本金
        repayInfo.setLoanTotalMoney(currentInterest.add(repayInfo1.getCurrentPrincipal()).add(sybj).add(loanPenalAmount));//提前还款总需
        return repayInfo;
    }

    @Override
    public List<RepaymentInfo> repaymentList(int userId, String bidId, PageBounds pageBounds, String tradeType, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bidId", bidId);
        map.put("tradeType", tradeType);
        map.put("status", status);
        return rorrowInfoDao.repaymentList(map, pageBounds);
    }

    @Override
    public RepaymentBidInfo getRepaymentBidInfo(int userId, String bidId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bidId", bidId);
        return rorrowInfoDao.getRepaymentBidInfo(map);
    }


    @Override
    public BigDecimal getRefundAmount(Map<String, Object> map) {
        return rorrowInfoDao.getRefundAmount(map);
    }

    @Override
    public BorrowAccountInfo getBorrowerInfo(int userId) {
        BigDecimal balance = rorrowInfoDao.getBorrowerBalance(userId); //新网借款账户余额
        BorrowAccountInfo borrowerInfo = new BorrowAccountInfo();
        if ( balance == null) {//可能系统没有保存用户的资金账户
            balance = BigDecimal.ZERO;
        }
        BigDecimal frozen = rorrowInfoDao.getBorrowerFrozen(userId);//新网借款账户冻结资金
        if ( frozen == null) {//可能系统没有保存用户的冻结账户
            frozen = BigDecimal.ZERO;
        }
        BigDecimal total = rorrowInfoDao.getBorrowTotal(userId);//新网借款账户借款总额
        if ( total == null) {
            total = BigDecimal.ZERO;
        }
        borrowerInfo.setBalance(balance);
        borrowerInfo.setBorrowerFrozen(frozen);
        borrowerInfo.setBorrowTotals(total);
        return borrowerInfo;
    }

    @Override
    public List<RepaymentInfo> repaymentByTypeList(int userId, String bidId, PageBounds pageBounds, String tradeType, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bidId", bidId);
        map.put("tradeType", tradeType);
        map.put("status", status);
        return rorrowInfoDao.repaymentByTypeList(map, pageBounds);
    }

    @Override
    public BorrowerDetail getBorrowerDetail(int bidId) {
        Map<String, Object> map = new HashMap<>();
        map.put("bidId", bidId);
        return rorrowInfoDao.getBorrowerDetail(map);
    }
}
