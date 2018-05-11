package com.fenlibao.p2p.controller.v_1.v_1_0_0.borrow;

import com.fenlibao.p2p.model.entity.borrow.BorrowInfo;
import com.fenlibao.p2p.model.entity.borrow.BorrowStaticsInfo;
import com.fenlibao.p2p.model.entity.finacing.BorrowAccountInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentBidInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentDetail;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.fiancing.RepaymentItemVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.borrow.BorrowInfoService;
import com.fenlibao.p2p.service.withdraw.IWithdrawService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Message;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by LouisWang on 2015/8/14.
 */
@RestController("v_1_0_0/borrowController")
@RequestMapping("borrow")
public class BorrowController {
    private static final Logger logger = LogManager.getLogger(BorrowController.class);

    @Resource
    BorrowInfoService borrowInfoService;
    @Resource
    IWithdrawService withdrawService;
    @Resource
    private UserInfoService userInfoService;

    /**
     * 借款列表 还款中和逾期(HKZ/YYQ)
     *
     * @param paramForm
     * @param pageRequestForm
     * @param status
     * @return
     */
    @RequestMapping(value = "borrowInfoList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getBorrowInfoList(
            @ModelAttribute BaseRequestFormExtend paramForm,
            @ModelAttribute PageRequestForm pageRequestForm,
            @RequestParam(required = false, value = "status") String status
    ) {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || !pageRequestForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));

        try {
            List<BorrowInfo> borrowInfoList = borrowInfoService.getBorrowInfoList(paramForm.getUserId(), status, pageBounds);
            /*if(null!=borrowInfoList && borrowInfoList.size()>0){
                for(int i=0;i<borrowInfoList.size();i++){
                    Map<String,Object> map = new HashMap<>();
                    map.put("userId",paramForm.getUserId().toString());
                    map.put("term",borrowInfoList.get(i).getTerm());
                    map.put("bid",borrowInfoList.get(i).getBid());
                    borrowInfoList.get(i).setRefundAmount(borrowInfoService.getRefundAmount(map));
                }
            }*/
            Pager pager = new Pager(borrowInfoList);
            pager.setItems(borrowInfoList);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception e) {
            logger.error("[BorrowController.getBorrowInfoList]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;

    }

    @RequestMapping(value = "repaymentList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getRepaymentList(
            @ModelAttribute BaseRequestFormExtend paramForm,
            @ModelAttribute PageRequestForm pageRequestForm,
            @RequestParam(required = false, value = "bidId") String bidId) {

        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || !pageRequestForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        pageRequestForm.setLimit("36");
        PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));
        try {
            List<RepaymentItemVO> repaymentItemVOs = new ArrayList<RepaymentItemVO>();
            List<RepaymentInfo> repaymentList = borrowInfoService.repaymentList(paramForm.getUserId(), bidId, pageBounds, null, null);
            Pager pager = new Pager(repaymentList);
            //List<RepaymentInfo> repaymentPrincipal = borrowInfoService.repaymentList(paramForm.getUserId(), bidId, pageBounds, "7001", null);
           // List<RepaymentInfo> repaymentAccrual = borrowInfoService.repaymentList(paramForm.getUserId(), bidId, pageBounds, "7002", null);
            for (RepaymentInfo repaymentInfo : repaymentList) {
                RepaymentItemVO repaymentItemVO = new RepaymentItemVO();
                /*for (RepaymentInfo principal : repaymentPrincipal) {
                    if (repaymentInfo.getPeriod() == principal.getPeriod()) {
                        repaymentItemVO.setPrincipal(principal.getRepaymentAmount());
                    }
                }*/

                Map<String,Object> map = new HashMap<>();
                map.put("userId",paramForm.getUserId());
                map.put("bid",bidId);
                map.put("term",repaymentInfo.getPeriod());

                //待还本金
                map.put("tradeType","7001");
                repaymentItemVO.setPrincipal(borrowInfoService.getRefundAmount(map));
                /*for (RepaymentInfo accrual : repaymentAccrual) {
                    if (repaymentInfo.getPeriod() == accrual.getPeriod()) {
                        repaymentItemVO.setAccrual(accrual.getRepaymentAmount());
                    }
                }*/
                //待还利息

                map.put("tradeType","7002");
                repaymentItemVO.setAccrual(borrowInfoService.getRefundAmount(map));


                repaymentItemVO.setActualRepaymentDate(repaymentInfo.getActualRepaymentDate());
                repaymentItemVO.setBidId(repaymentInfo.getBidId());
                repaymentItemVO.setExpectedRepaymentDate(repaymentInfo.getExpectedRepaymentDate());
                repaymentItemVO.setPeriod(repaymentInfo.getPeriod());
                repaymentItemVO.setRepaymentStatus(repaymentInfo.getRepaymentStatus());
                repaymentItemVO.setRepaymentAmount(repaymentInfo.getRepaymentAmount().toString());
                repaymentItemVO.setSystemDate(new Date());
                repaymentItemVO.setLoanDays(repaymentInfo.getLoanDays());
                repaymentItemVO.setMonth(repaymentInfo.getMonth());
                repaymentItemVO.setLoanDate(repaymentInfo.getLoanDate());
                repaymentItemVOs.add(repaymentItemVO);


            }

            pager.setItems(repaymentItemVOs);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception e) {
            logger.error("[BorrowController.repaymentList]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "repaymentInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse repaymentInfo(
            @ModelAttribute BaseRequestFormExtend paramForm,
            @ModelAttribute PageRequestForm pageRequestForm,
            @RequestParam(required = false, value = "bidId") String bidId) {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate()|| StringUtils.isEmpty(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        PageBounds pageBounds = new PageBounds(1, 36);
        try {
            RepaymentDetail repaymentDetail = new RepaymentDetail();
            List<RepaymentInfo> repaymentList = borrowInfoService.repaymentList(paramForm.getUserId(), bidId, pageBounds, null, null);
            List<RepaymentInfo> repaymentByTypeList = borrowInfoService.repaymentByTypeList(paramForm.getUserId(), bidId, pageBounds, null, null);
            BigDecimal pendingPrincipal = BigDecimal.ZERO;
            BigDecimal alreadyPrincipal = BigDecimal.ZERO;
            BigDecimal pendingAccrual = BigDecimal.ZERO;
            BigDecimal alreadyAccrual = BigDecimal.ZERO;
            BigDecimal alreadyBreak = BigDecimal.ZERO;
            BigDecimal alreadyOverdue = BigDecimal.ZERO;
            int pendingTerm = 0;//未还期数
            int alreadyTerm = 0;//已还期数
            if(null != repaymentList && repaymentList.size()>0) {
                for (RepaymentInfo repaymentInfo : repaymentList) {
                   if(repaymentInfo.getRepaymentStatus().equals("WH")){
                       pendingTerm = pendingTerm +1;
                        repaymentDetail.setPendingRepayment(pendingTerm);//未还期数
                    }else if(repaymentInfo.getRepaymentStatus().equals("YH") || repaymentInfo.getRepaymentStatus().equals("TQH")){
                       alreadyTerm = alreadyTerm + 1;
                       repaymentDetail.setAlreadyRepayment(alreadyTerm);
                   }
                }

            }

            if(null != repaymentByTypeList && repaymentByTypeList.size()>0) {
                for (RepaymentInfo repaymentInfo : repaymentByTypeList) {
                    if(repaymentInfo.getRepaymentStatus().equals("WH")){
                        if(repaymentInfo.getTradeType()==7001){//未还本金
                            pendingPrincipal = pendingPrincipal.add(repaymentInfo.getRepaymentAmount());
                        }else if(repaymentInfo.getTradeType()==7002){//未还利息
                            pendingAccrual = pendingAccrual.add(repaymentInfo.getRepaymentAmount());
                        }
                    }else if(repaymentInfo.getRepaymentStatus().equals("YH") || repaymentInfo.getRepaymentStatus().equals("TQH")){
                        if(repaymentInfo.getTradeType()==7001){//已还本金
                            alreadyPrincipal = alreadyPrincipal.add(repaymentInfo.getRepaymentAmount());
                        }else if(repaymentInfo.getTradeType()==7002){//已还利息
                            alreadyAccrual = alreadyAccrual.add(repaymentInfo.getRepaymentAmount());
                        }else if(repaymentInfo.getTradeType()==7004){//已还违约金
                            alreadyBreak = alreadyBreak.add(repaymentInfo.getRepaymentAmount());
                        }
                        else if(repaymentInfo.getTradeType()==7005){//已还逾期利息
                            alreadyOverdue = alreadyOverdue.add(repaymentInfo.getRepaymentAmount());
                        }
                    }
                }

            }
            repaymentDetail.setPendingPrincipal(pendingPrincipal.toString());
            repaymentDetail.setAlreadyPrincipal(alreadyPrincipal.toString());
            repaymentDetail.setPendingAccrual(pendingAccrual.toString());
            repaymentDetail.setAlreadyAccrual(alreadyAccrual.toString());
            repaymentDetail.setAlreadyBreak(alreadyBreak.toString());
            repaymentDetail.setAlreadyOverdue(alreadyOverdue.toString());
            response.setData(CommonTool.toMap(repaymentDetail));
        } catch (Exception e) {
            logger.error("[BorrowController.repaymentInfo]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "repaymentBidInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse repaymentBidInfo(
            @ModelAttribute BaseRequestFormExtend paramForm,
            @RequestParam(required = false, value = "bidId") String bidId) {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()||StringUtils.isEmpty(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        try {
            RepaymentBidInfo bidInfo = borrowInfoService.getRepaymentBidInfo(paramForm.getUserId(), bidId);
            if (bidInfo != null) {
                response.setData(CommonTool.toMap(bidInfo));
            }
        } catch (Exception e) {
            logger.error("[BorrowController.repaymentBidInfo]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 借款列表 已还清
     *
     * @param paramForm
     * @param pageRequestForm
     * @return
     */
    @RequestMapping(value = "yhqBorrowInfoList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getYHQBorrowInfoList(
            @ModelAttribute BaseRequestFormExtend paramForm,
            @ModelAttribute PageRequestForm pageRequestForm
    ) {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || !pageRequestForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));

        try {
            List<BorrowInfo> borrowInfoList = borrowInfoService.getYHQBorrowInfoList(paramForm.getUserId(), pageBounds);
            Pager pager = new Pager(borrowInfoList);
            pager.setItems(borrowInfoList);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception e) {
            logger.error("[BorrowController.getYHQBorrowInfoList]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 借款未还统计
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "stayRepayStatics", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getStayRepayStatics(@ModelAttribute BaseRequestFormExtend paramForm) {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        try {
            BorrowStaticsInfo borrowStaticsInfo = borrowInfoService.getStayRepayStatics(paramForm.getUserId());
            response.setData(CommonTool.toMap(borrowStaticsInfo));
        } catch (Exception e) {
            logger.error("[BorrowController.getStayRepayStatics]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

//    /**
//     * 还款信息
//     *
//     * @param paramForm
//     * @return
//     */
//    @RequestMapping(value = "currentRepayStatics", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
//    public HttpResponse getCurrentRepayStatics(@ModelAttribute BaseRequestFormExtend paramForm,
//                                               @RequestParam(required = true, value = "bidId") String bidId) {
//        HttpResponse response = new HttpResponse();
//        HXBalanceVO balanceVO = null;
//        if (!paramForm.validate() || bidId == null || "".equals(bidId)) {
//            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
//            return response;
//        }
//
//        try {
//            RepayInfo repayInfo = borrowInfoService.getCurrentRepayStatics(paramForm.getUserId(), bidId);
//            // 华兴余额
//            try {
//                balanceVO = hxCommonService.getBalance(paramForm.getUserId());
//            } catch (Exception e) {
//                logger.error("[BorrowController.getCurrentRepayStatics]" + e.getMessage(), e);
//            }
//            if (balanceVO != null) {
//                repayInfo.setAvailable(balanceVO.getAvailable());
//            } else {
//                repayInfo.setAvailable(BigDecimal.ZERO);
//            }
//            response.setData(CommonTool.toMap(repayInfo));
//        } catch (Exception e) {
//            logger.error("[BorrowController.getCurrentRepayStatics]", e);
//            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
//        }
//        return response;
//    }

//    /**
//     * 提前还款信息
//     *
//     * @param paramForm
//     * @return
//     */
//    @RequestMapping(value = "preRepayStatics", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
//    public HttpResponse getPreRepayStatics(@ModelAttribute BaseRequestFormExtend paramForm,
//                                           @RequestParam(required = true, value = "bidId") String bidId) {
//        HttpResponse response = new HttpResponse();
//        HXBalanceVO balanceVO = null;
//        if (!paramForm.validate() || bidId == null || "".equals(bidId)) {
//            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
//            return response;
//        }
//
//        try {
//            ForwardRepayInfoVO forwardRepayInfoVO = borrowInfoService.getPreRepayStatics(paramForm.getUserId(), bidId);
//            try {
//                balanceVO = hxCommonService.getBalance(paramForm.getUserId());
//            } catch (Exception e) {
//                logger.error("[BorrowController.getPreRepayStatics]" + e.getMessage(), e);
//            }
//            if (balanceVO != null) {
//                forwardRepayInfoVO.setAvailable(balanceVO.getAvailable());
//            } else {
//                forwardRepayInfoVO.setAvailable(BigDecimal.ZERO);
//            }
//
//            response.setData(CommonTool.toMap(forwardRepayInfoVO));
//        } catch (Exception e) {
//            logger.error("[BorrowController.getPreRepayStatics]", e);
//            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
//        }
//        return response;
//    }


    /**
     * 借款用户账户总览
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "borrowerInfo", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getBorrowerInfo(@ModelAttribute BaseRequestFormExtend paramForm,String userType) {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()||StringUtils.isEmpty(userType)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        try {
            UserAccountInfoVO userAccountInfoVO = userInfoService.getUserAccountInfo(String.valueOf(paramForm.getUserId()), userType);
            if (userAccountInfoVO == null) {
                response.setMessage("用户不存在!");
                response.setCodeMessage(Message.STATUS_1014, Message.get(Message.STATUS_1014));
            } else {
                BorrowAccountInfo borrowInfo = borrowInfoService.getBorrowerInfo(paramForm.getUserId());
                BigDecimal poundage = withdrawService.getPoundage(paramForm.getUserId());
                borrowInfo.setFactorage(poundage);

                // 获取用户账户信息字段Map
                Map<String, Object> data = userInfoService.getUserAccountInfoDataMap(userAccountInfoVO,null,false, VersionTypeEnum.CG);
                borrowInfo.setIsBorrower((Integer) data.get("isBorrower"));
                borrowInfo.setIsBorrowBinkCards(Integer.valueOf((String) data.get("isBorrowBinkCards")));
                borrowInfo.setBorrowBankList((ArrayList<String>) data.get("borrowBankList"));

                response.setData(CommonTool.toMap(borrowInfo));
            }
        } catch (Exception e) {
            logger.error("[BorrowController.getBorrowerInfo]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }
}
