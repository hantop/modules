package com.fenlibao.service.pms.da.biz.repayment.impl;

import com.fenlibao.dao.pms.da.biz.loanList.LoanListMapper;
import com.fenlibao.dao.pms.da.biz.repayment.RepaymentMapper;
import com.fenlibao.model.pms.da.biz.form.RepaymentForm;
import com.fenlibao.model.pms.da.biz.viewobject.RepaymentVO;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectPrepaymentConfig;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.vo.RepayBudgetVO;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayService;
import com.fenlibao.service.pms.da.biz.loanList.impl.LoanListServiceImpl;
import com.fenlibao.service.pms.da.biz.repayment.RepaymentService;
import com.fenlibao.service.pms.da.exception.BidVerificationException;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 还款管理
 * <p>
 */
@Service
public class RepaymentServiceImpl implements RepaymentService {
    private static final Logger logger = LogManager.getLogger(RepaymentServiceImpl.class);

    @Autowired
    LoanListMapper loanListMapper;

    @Autowired
    private RepaymentMapper repaymentMapper;

    @Autowired
    private XWRepayService xwRepayService;

    @Override
    public List<RepaymentVO> getRepaymentList(RepaymentForm repaymentForm, RowBounds bounds) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        List<RepaymentVO> list = repaymentMapper.getRepaymentList(repaymentForm, bounds);
        List<Integer> userIds = new ArrayList<>();
        // 用户详细信息
        if(list.size() > 0){
            for (RepaymentVO repaymentVO : list) {
                if(!userIds.contains(repaymentVO.getBorrowUserId())){
                    userIds.add(repaymentVO.getBorrowUserId());
                }
                if(!userIds.contains(repaymentVO.getReceiptID())){
                    userIds.add(repaymentVO.getReceiptID());
                }
            }

        }
        List<Map<String, Object>> userDetailMap = new ArrayList<>();
        if(userIds.size() > 0){
            userDetailMap = loanListMapper.getUserDetail(userIds);
        }
        for (RepaymentVO repaymentVO : list) {
            cal.setTime(repaymentVO.getRepayDay());
            if(beforeDate(now, cal.getTime())){
                repaymentVO.setPrerepay(true);// 可提前还款
            }
            if(repaymentVO.getStatus().equals("HKZ")){
                if(repaymentVO.getOrderState() != null){
                    if(repaymentVO.getOrderState().equals("DTJ") || repaymentVO.getOrderState().equals("DQR")){
                        repaymentVO.setStatus("还款中");
                    }else if (repaymentVO.getOrderState().equals("SB")){
                        repaymentVO.setStatus("还款失败");
                    }
                }else{
                    repaymentVO.setStatus("待还款");
                }
            }
            if(repaymentVO.getTotalTerm() > 0 && repaymentVO.getCurrentTerm() > 0){
                repaymentVO.setTermDisplay(repaymentVO.getCurrentTerm() + "/" + repaymentVO.getTotalTerm());
            }
            repaymentVO.setBorrowUserName(LoanListServiceImpl.getUserName(userDetailMap, repaymentVO.getBorrowUserId()));
            repaymentVO.setReceiptName(LoanListServiceImpl.getUserName(userDetailMap, repaymentVO.getReceiptID()));
        }
        return list;
    }

    @Override
    public Map<String, Object> getRepayDetailInfo(int bidId, String repayMethod, boolean isPreRepay, boolean isSubrogation) throws Throwable {
        Map<String, Object> repayDetailInfoMap = new HashMap<String, Object>();
        String msg = null;
        if(bidId < 0){
            msg = "标记录不存在";
            repayDetailInfoMap.put("msg", msg);
            return repayDetailInfoMap;
        }
        if(isSubrogation){
            String guaranteePlatformUser = repaymentMapper.getGuaranteePlatformUser(bidId);
            if(guaranteePlatformUser == null || guaranteePlatformUser.trim().equals("")){
                throw new BidVerificationException("该标没有担保账户!");
            }
        }
        try{
            RepayBudgetVO repayBudgetVO = xwRepayService.getRepayBudget(bidId, getRepayType(isPreRepay), isSubrogation);
            if(repayBudgetVO != null){
                repayDetailInfoMap.put("repayBudgetVO", repayBudgetVO);
            }
        }catch(XWTradeException e){
            logger.error("[调用存管获取还款信息的异常:]" + e.getMessage(), e);
            throw e;
        }
        return repayDetailInfoMap;
    }

    @Override
    public String doRepay(int bidId, String repayMethod, boolean isPreRepay, boolean isSubrogation,XWProjectPrepaymentConfig xwProjectPrepaymentConfig) throws Throwable {
        String code;
        try{
            xwRepayService.repayApply(bidId, getRepayType(isPreRepay), isSubrogation,xwProjectPrepaymentConfig);
            code = "1111";
        }catch(XWTradeException e){
            logger.error("[调用存管还款异常:]" + e.getMessage(), e);
            throw e;
        }
        return code;
    }

    @Override
    public String doErrorRepay(int bidId) throws Throwable {
        String code;
        try{
            xwRepayService.handleError(bidId);
            code = "1111";
        }catch(XWTradeException e){
            logger.error("[调用存管异常还款发生异常:]" + e.getMessage(), e);
            throw e;
        }
        return code;
    }

    @Override
    public RepaymentVO getErrorRepayment(RepaymentForm repaymentForm) {
        return repaymentMapper.getErrorRepayment(repaymentForm);
    }

    private SysRepayOperationType getRepayType(boolean isPreRepay){
        if(isPreRepay){
            return SysRepayOperationType.PREPAY;
        }else{
            return SysRepayOperationType.REPAY;
        }
    }

    private boolean beforeDate(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar calendar = Calendar.getInstance();
            long time1 = getDate(calendar, date1);
            long time2 = getDate(calendar, date2);
            return time1 < time2;
        } else {
            return false;
        }
    }

    private static final long getDate(Calendar calendar, Date date) {
        calendar.setTimeInMillis(date.getTime());
        calendar.set(10, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

}
