package com.fenlibao.service.pms.da.biz.loanList.impl;

import com.fenlibao.dao.pms.da.biz.loanList.LoanListMapper;
import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.form.LoanListForm;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.trade.XWCancelTenderService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanService;
import com.fenlibao.service.pms.da.biz.loanList.LoanListService;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/17.
 */
@Service
public class LoanListServiceImpl implements LoanListService {

    private static final Logger logger = LogManager.getLogger(LoanListServiceImpl.class);

    @Autowired
    LoanListMapper loanListMapper;

    @Autowired
    private XWMakeLoanService xwMakeLoanService;

    @Autowired
    private XWCancelTenderService xwCancelTenderService;

    @Override
    public String makeALoanApply(int loanId) throws Exception{
        String code = null;
        String bidStatus = loanListMapper.getBidStatus(loanId);
        if(bidStatus.equals("DFK")){
            try {
                Integer orderId = xwMakeLoanService.getOngoingConfirmTenderOrder(loanId);
                if(orderId!=null){
                    throw new XWTradeException(XWResponseCode.TRADE_OPERATION_REPEAT);
                }
                xwMakeLoanService.makeLoanApply(Integer.valueOf(loanId));
                bidStatus = loanListMapper.getBidStatus(loanId);
                if(!bidStatus.equals("HKZ")){
                    code = "1000";//放款中/放款失败
                }else if(bidStatus.equals("HKZ")){
                    code = "2000";//放款成功
                }
            }catch (XWTradeException e) {
                logger.error("[调用存管放款产生的异常:]" + e.getMessage(), e);
                throw e;
            }
        }
        return code;
    }

    @Override
    public List<LoanList> getLoanList(LoanListForm loanListForm, RowBounds bounds) {
        if (loanListForm.getTitle() != null && !loanListForm.getTitle().trim().equals("")){
            loanListForm.setTitle(loanListForm.getTitle());
        }else{
            loanListForm.setTitle(null);
        }
        if (loanListForm.getBorrowerAccount() != null && !loanListForm.getBorrowerAccount().trim().equals("")){
            loanListForm.setBorrowerAccount(loanListForm.getBorrowerAccount());
        }else{
            loanListForm.setBorrowerAccount(null);
        }
        if (loanListForm.getReceiptAccount() != null && !loanListForm.getReceiptAccount().trim().equals("")){
            loanListForm.setReceiptAccount(loanListForm.getReceiptAccount());
        }else{
            loanListForm.setReceiptAccount(null);
        }
        if(loanListForm.getStatus() != null && loanListForm.getStatus() != ""){
            if(!loanListForm.getStatus().equals("YLB")){
                if(loanListForm.getStatus().equals("1")){// getStatus为1时:放款中:标的处于待放款,订单处于待确认或者是待提交
                    loanListForm.setOrderStatusA("DTJ");
                    loanListForm.setOrderStatusB("DQR");
                }else if(loanListForm.getStatus().equals("0")){//放款失败:标的处于待放款,订单处于失败
                    loanListForm.setOrderStatusA("SB");
                }else if(loanListForm.getStatus().equals("DFK")){// 待放款
                    loanListForm.setOrderStatusA("none");
                }
                loanListForm.setStatus("DFK");
            }
        }else{//全部
            loanListForm.setStatus("DFK");
        }
        List<LoanList> loanList = loanListMapper.getLoanList(loanListForm, bounds);
        List<Integer> userIds = new ArrayList<>();
        List<Integer> loanIds = new ArrayList<>();
        // 获取用户详细信息
        if(loanList.size() > 0){
            for (LoanList loan : loanList) {
                //借款人
                if(!userIds.contains(loan.getBorrowerID())){
                    userIds.add(loan.getBorrowerID());
                }
                //收款人
                if(!userIds.contains(loan.getReceiptID())){
                    userIds.add(loan.getReceiptID());
                }
                //标的
                if(!loanIds.contains(loan.getBidId())){
                    loanIds.add(loan.getBidId());
                }
            }
        }
        List<Map<String, Object>> userDetailMap = new ArrayList<>();
        if(userIds.size() > 0){
            userDetailMap = loanListMapper.getUserDetail(userIds);
        }
        // 查询流标订单状态
        List<Map<String, Object>> projectOrderStateMap = new ArrayList<>();
        if(loanIds.size() > 0){
            projectOrderStateMap = loanListMapper.getFlowBidOrderState(loanIds);
        }
        String bidStatus;
        String orderStatus;
        String flowBidState;
        if(loanList.size() > 0){
            for (LoanList loan : loanList) {
                bidStatus = loan.getBidStatus();
                orderStatus = loan.getOrderStatus();
                loan.setBorrowerName(getUserName(userDetailMap, loan.getBorrowerID()));
                loan.setReceiptName(getUserName(userDetailMap, loan.getReceiptID()));
                //流标订单状态
                flowBidState = getProjectState(projectOrderStateMap, loan.getBidId());
                //查询相应订单状态(订单不存在或者是处于失败状态的时候可以发起放款操作)
                if (bidStatus.equals("DFK")) {//标的状态为待放款
                    if(orderStatus != null){
                        if (orderStatus.equals("DTJ") || orderStatus.equals("DQR")) {// 订单处于待提交或者是待确认状态
                            // 标的处于待放款,显示为放款中,不可操作
                            loan.setBidStatus("放款中");
                            loan.setLoan(0);
                        } else if (orderStatus.equals("SB")) {// 订单失败
                            // 标的处于待放款,显示为放款失败,可操作
                            loan.setBidStatus("放款失败");
                            loan.setLoan(1);
                        }
                    }else{// 订单不存在
                        //已操作了流标
                        if(flowBidState != null){
                            if(flowBidState.equals("DTJ") || flowBidState.equals("DQR")){
                                loan.setBidStatus("流标中");
                                loan.setLoan(0);
                            }
                        }else{
                            // 标的处于待放款,显示为待放款,可操作
                            loan.setBidStatus("待放款");
                            loan.setLoan(1);
                        }
                    }
                }
                if (loan.getBorroweRate() != null) {
                    loan.setBorroweRate(new BigDecimal(loan.getBorroweRate()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
                }
                if (loan.getInvestRate() != null) {
                    loan.setInvestRate(new BigDecimal(loan.getInvestRate()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
                }
            }
        }
        return loanList;
    }

    @Override
    public String flowBidApply(int loanId) throws Exception {
        String code = null;
        String bidStatus = loanListMapper.getBidStatus(loanId);
        if(bidStatus.equals("DFK")){
            try {
                xwCancelTenderService.cancelTenders(Integer.valueOf(loanId));
                bidStatus = loanListMapper.getBidStatus(loanId);
                if(!bidStatus.equals("YLB")){
                    code = "1000";//流标中/流标失败
                }else if(bidStatus.equals("YLB")){
                    code = "2000";//流标成功
                }
            }catch (XWTradeException e) {
                logger.error("[调用存管流标产生的异常:]" + e.getMessage(), e);
                throw e;
            }
        }
        return code;
    }



    public static String getUserName(List<Map<String, Object>> userDetailMap, Integer userId){
        if(userDetailMap.size() > 0){
            for (Map<String, Object> userDetail : userDetailMap) {
                if (userId.toString().equals(userDetail.get("userId").toString())) {
                    if(userDetail.get("userName").toString() != null){
                        return userDetail.get("userName").toString();
                    }
                }
            }
        }
        return null;
    }

    public static String getProjectState(List<Map<String, Object>> projectDetailMap, Integer loanId){
        if(projectDetailMap.size() > 0){
            for (Map<String, Object> projectDetail : projectDetailMap) {
                if (loanId.toString().equals(projectDetail.get("loanId").toString())) {
                    if(projectDetail.get("state").toString() != null){
                        return projectDetail.get("state").toString();
                    }
                }
            }
        }
        return null;
    }


    @Override
    public List<LoanList> afterLoanList(LoanListForm loanListForm, RowBounds bounds) throws Exception {

        List<LoanList> loanList = loanListMapper.getLoanList(loanListForm, bounds);
        List<Integer> userIds = new ArrayList<>();
        List<Integer> loanIds = new ArrayList<>();
        // 获取用户详细信息
        if(loanList.size() > 0){
            for (LoanList loan : loanList) {
                //借款人
                if(!userIds.contains(loan.getBorrowerID())){
                    userIds.add(loan.getBorrowerID());
                }
                //收款人
                if(!userIds.contains(loan.getReceiptID())){
                    userIds.add(loan.getReceiptID());
                }
                //标的
                if(!loanIds.contains(loan.getBidId())){
                    loanIds.add(loan.getBidId());
                }
            }
        }
        List<Map<String, Object>> userDetailMap = new ArrayList<>();
        if(userIds.size() > 0){
            userDetailMap = loanListMapper.getUserDetail(userIds);
        }
        // 查询流标订单状态
        List<Map<String, Object>> projectOrderStateMap = new ArrayList<>();
        if(loanIds.size() > 0){
            projectOrderStateMap = loanListMapper.getFlowBidOrderState(loanIds);
        }
        String bidStatus;
        String orderStatus;
        String flowBidState;
        if(loanList.size() > 0){
            for (LoanList loan : loanList) {
                bidStatus = loan.getBidStatus();
                orderStatus = loan.getOrderStatus();
                loan.setBorrowerName(getUserName(userDetailMap, loan.getBorrowerID()));
                loan.setReceiptName(getUserName(userDetailMap, loan.getReceiptID()));
                //流标订单状态
                flowBidState = getProjectState(projectOrderStateMap, loan.getBidId());
                //查询相应订单状态(订单不存在或者是处于失败状态的时候可以发起放款操作)
                if (bidStatus.equals("DFK")) {//标的状态为待放款
                    if(orderStatus != null){
                        if (orderStatus.equals("DTJ") || orderStatus.equals("DQR")) {// 订单处于待提交或者是待确认状态
                            // 标的处于待放款,显示为放款中,不可操作
                            loan.setBidStatus("放款中");
                            loan.setLoan(0);
                        } else if (orderStatus.equals("SB")) {// 订单失败
                            // 标的处于待放款,显示为放款失败,可操作
                            loan.setBidStatus("放款失败");
                            loan.setLoan(1);
                        }
                    }else{// 订单不存在
                        //已操作了流标
                        if(flowBidState != null){
                            if(flowBidState.equals("DTJ") || flowBidState.equals("DQR")){
                                loan.setBidStatus("流标中");
                                loan.setLoan(0);
                            }
                        }else{
                            // 标的处于待放款,显示为待放款,可操作
                            loan.setBidStatus("待放款");
                            loan.setLoan(1);
                        }
                    }
                }
                if (loan.getBorroweRate() != null) {
                    loan.setBorroweRate(new BigDecimal(loan.getBorroweRate()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
                }
                if (loan.getInvestRate() != null) {
                    loan.setInvestRate(new BigDecimal(loan.getInvestRate()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
                }
            }
        }
        return loanList;
    }
}
