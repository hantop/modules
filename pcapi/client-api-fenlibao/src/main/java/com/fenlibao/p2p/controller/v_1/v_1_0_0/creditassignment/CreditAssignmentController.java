/**
 * Copyright © 2015 fenlibao . All rights reserved.
 *
 * @Title: CreditassignmentController.java
 * @Prject: client-api-fenlibao
 * @Package: com.fenlibao.p2p.controller.v_1.V_1_0_0.creditassignment
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-22 上午9:34:57
 * @version: V1.0.0
 */
package com.fenlibao.p2p.controller.v_1.v_1_0_0.creditassignment;

import com.dimeng.util.parser.BigDecimalParser;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.bid.ProjectInfo;
import com.fenlibao.p2p.model.entity.borrow.BorrowerDetail;
import com.fenlibao.p2p.model.entity.creditassignment.AddTransfer;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.entity.creditassignment.TransferOutInfo;
import com.fenlibao.p2p.model.entity.creditassignment.UserCoupons;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.GuaranteeMeasureEunm;
import com.fenlibao.p2p.model.enums.bid.PaymentTypeEnum;
import com.fenlibao.p2p.model.enums.bid.RepaymentModeEnum;
import com.fenlibao.p2p.model.enums.loan.LoanUserEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.creditassignment.*;
import com.fenlibao.p2p.service.AccessoryInfoService;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.borrow.BorrowInfoService;
import com.fenlibao.p2p.service.creditassignment.TenderTransferManageService;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import com.fenlibao.p2p.service.creditassignment.impl.ZqzrManageImpl;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import com.fenlibao.p2p.util.pay.CalculateEarningsUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName: CreditassignmentController
 * @Description: 债权转让相关接口
 * @author: laubrence
 * @date: 2015-10-22 上午9:34:57  
 */

@RestController("v_1_0_0/CreditAssignmentController")
@RequestMapping("creditassignment")
public class CreditAssignmentController {

    private static final Logger logger = LogManager.getLogger(CreditAssignmentController.class);

    @Resource
    TransferInService transferInService;

    @Resource
    TenderTransferManageService tenderTransferManageService;

    @Resource
    private ZqzrManageImpl zqzrManageImpl;

    @Resource
    private AccessoryInfoService accessoryInfoService;

    @Resource
    private ITradeService iTradeService;

    @Resource
    BidInfoService bidInfoService;

    @Resource
    FinacingService finacingService;

    @Resource
    BorrowInfoService borrowInfoService;

    /**
     * 债权转让购买之前判断是否可以购买
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @param applyforId
     * @return
     */
    @RequestMapping(value = "transferIn/isUserCanPurchase", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse isUserCanPurchase(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm, @RequestParam(required = true, value = "token") String token, @RequestParam(required = true, value = "userId") String userId, @RequestParam(required = true, value = "applyforId") String applyforId) throws Throwable {
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(applyforId)) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            boolean isUserCanPurchase = tenderTransferManageService.isUserCanPurchase(Integer.valueOf(applyforId), Integer.valueOf(userId));
            if (isUserCanPurchase) {
                return response;
            }else{
                response.setCodeMessage(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage());
            }
        } catch (Exception ex) {
            throw ex;
        }
        return response;
    }

    /**
     * 债权转让购买
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @param applyforId
     * @return
     */
    @RequestMapping(value = "transferIn/purchase", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse transferInPurchase(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
                                    @RequestParam(required = true, value = "token") String token,
                                    @RequestParam(required = true, value = "userId") String userId,
                                    @RequestParam(required = true, value = "applyforId") String applyforId,
                                    @RequestParam(required = true, value = "dealPassword") String dealPassword,
                                    @RequestParam(required = true, value = "versionType") VersionTypeEnum versionType) throws Throwable {
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(applyforId) || StringUtils.isEmpty(dealPassword)||versionType==null) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }

            if(versionType!=null&&versionType.getCode().equals("PT")){
                response.setCodeMessage(ResponseCode.NOT_SUPPURT_BUY_TRANFER);
                return response;
            }

            if(!versionType.getCode().equals(VersionTypeEnum.CG.getCode())) {
                //验证用户购买交易密码是否正确
                if (!iTradeService.isValidUserPwd(Integer.valueOf(userId), dealPassword)) {
                    response.setCodeMessage(ResponseCode.TRADE_USER_NOT_RIGHT_PASSWORD.getCode(), ResponseCode.TRADE_USER_NOT_RIGHT_PASSWORD.getMessage());//交易密码错误
                    return response;
                }
            }
            //验证是否是在计划里面的债权
            boolean isPlanContain = transferInService.isValidPlanZq(Integer.valueOf(applyforId));
            if (isPlanContain) {
                response.setCodeMessage(ResponseCode.ZQZR_CANNOT_BUY_PLAN.getCode(), ResponseCode.ZQZR_CANNOT_BUY_PLAN.getMessage());
                return response;
            }
            transferInService.purchase(Integer.valueOf(applyforId), Integer.valueOf(userId));
        } catch (Exception ex) {
            throw ex;
        }
        return response;
    }

    /**
     * 转入债权列表信息
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @param timestamp
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "transferIn/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse transferInList(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
                                @RequestParam(required = true, value = "token") String token,
                                @RequestParam(required = true, value = "userId") String userId,
                                @RequestParam(required = false, value = "timestamp") String timestamp) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        List<TransferInInfoVO> transferInInfoVOs = new ArrayList<TransferInInfoVO>();

        List<TransferInInfo> transferInInfos = transferInService.getTransferInInfoList(Integer.valueOf(userId), timestamp);
        for (TransferInInfo transferInInfo : transferInInfos) {
            TransferInInfoVO transferInInfoVO = new TransferInInfoVO();
            transferInInfoVO.setCreditId(transferInInfo.getCreditId());
            transferInInfoVO.setCreditTitle(InterfaceConst.CREDIT_NAME_PREFIX + transferInInfo.getCreditCode());
            transferInInfoVO.setPurchaseTime(DateUtil.dateToTimestampToSec(transferInInfo.getPurchaseTime()));
            transferInInfoVO.setYearYield(transferInInfo.getYearYield());
            transferInInfoVO.setSurplusDays(transferInInfo.getSurplusDays()>0?transferInInfo.getSurplusDays():0);//如果过了到期时间，剩余债权天数为0
            transferInInfoVO.setInvestAmount(transferInInfo.getInvestAmount());
            transferInInfoVO.setExpectedProfit(transferInInfo.getExpectedProfit());
            //标的状态
            if (Status.TBZ.name().equals(transferInInfo.getBidStatus()) || Status.DFK.name().equals(transferInInfo.getBidStatus())) {
                transferInInfoVO.setCreditStatus(1);
            }
            if (Status.HKZ.name().equals(transferInInfo.getBidStatus())) {
                transferInInfoVO.setCreditStatus(2);
            }
            if ("S".equals(transferInInfo.getIsTransfer())) {
                transferInInfoVO.setCreditStatus(3);
            }
            if (Status.YJQ.name().equals(transferInInfo.getBidStatus())) {
                transferInInfoVO.setCreditStatus(6);
            }
            if (!"F".equals(transferInInfo.getIsYq())) {
                transferInInfoVO.setCreditStatus(5);
            }
            if (transferInInfo.getTransferOutId() > 0) {
                transferInInfoVO.setCreditStatus(4);
                transferInInfoVO.setExpectedProfit(transferInInfo.getArrivalProfit());
            }
            transferInInfoVOs.add(transferInInfoVO);
        }
        response.getData().put("transferInList", transferInInfoVOs);
        return response;
    }

    /**
     * 转入债权详情信息
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "transferIn/detail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse transferInDetail(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
                                  @RequestParam(required = true, value = "token") String token,
                                  @RequestParam(required = true, value = "userId") String userId,
                                  @RequestParam(required = false, value = "creditId") String creditId) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(creditId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        TransferInInfoDetailVO transferInInfoDetailVO = new TransferInInfoDetailVO();

        TransferInInfo transferInInfo = transferInService.getTransferInInfoDetail(Integer.valueOf(userId), Integer.valueOf(creditId));
        if (transferInInfo == null) {
            return response;
        }
        List<Integer> tenderIdList =new ArrayList<Integer>();
        Map<String,Object> tenderMap=new HashMap<>();
        tenderIdList.add(transferInInfo.getCreditId());
        tenderMap.put("list",tenderIdList);
        List<UserCoupons> userCouponsList = new ArrayList<UserCoupons>();
        if(!tenderIdList.isEmpty()) {
            userCouponsList = transferInService.getUserCoupons(tenderMap);
        }
        //加息利率（券加息+标加息）
        double bidInterestRise = transferInInfo.getBidInterestRise();
        transferInInfoDetailVO.setJxFlag(false);
        if(!userCouponsList.isEmpty()) {
            for (UserCoupons userCoupons : userCouponsList) {
                if (transferInInfo.getCreditId() == userCoupons.getTenderId()) {
                    bidInterestRise = bidInterestRise + userCoupons.getScope();
                }
            }
            transferInInfoDetailVO.setJxFlag(true);
        }
        transferInInfoDetailVO.setBidInterestRise(bidInterestRise);
        transferInInfoDetailVO.setCreditId(transferInInfo.getCreditId());
        transferInInfoDetailVO.setBidId(transferInInfo.getBidId());
        transferInInfoDetailVO.setCreditTitle(InterfaceConst.CREDIT_NAME_PREFIX + transferInInfo.getCreditCode());
        transferInInfoDetailVO.setPurchaseTime(DateUtil.dateToTimestampToSec(transferInInfo.getPurchaseTime()));
        transferInInfoDetailVO.setYearYield(transferInInfo.getYearYield());
        transferInInfoDetailVO.setSurplusDays(transferInInfo.getSurplusDays()>0?transferInInfo.getSurplusDays():0);//如果过了到期时间，剩余债权天数为0
        transferInInfoDetailVO.setInvestAmount(transferInInfo.getInvestAmount());
        transferInInfoDetailVO.setExpectedProfit(transferInInfo.getExpectedProfit());
        transferInInfoDetailVO.setInvestDate(DateUtil.dateToTimestampToSec(transferInInfo.getInvestDate()));
        transferInInfoDetailVO.setExpireDate(DateUtil.dateToTimestampToSec(transferInInfo.getExpireDate()));
        transferInInfoDetailVO.setAssignmentAgreementUrl(transferInService.getUserAssignmentAgreementUrl(Integer.valueOf(userId)));
        transferInInfoDetailVO.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(transferInInfo.getBidId()));
        transferInInfoDetailVO.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(transferInInfo.getBidId()));
        transferInInfoDetailVO.setRemark(transferInInfo.getRemark());
        transferInInfoDetailVO.setBorrowerUrl(Config.get("bid.borrower.url")+transferInInfo.getBidId());
        transferInInfoDetailVO.setGroupInfoList(bidInfoService.getGroupInfoList(transferInInfo.getBidId()));


        //标的状态
        if (Status.TBZ.name().equals(transferInInfo.getBidStatus()) || Status.DFK.name().equals(transferInInfo.getBidStatus())) {
            transferInInfoDetailVO.setCreditStatus(1);
        }
        if (Status.HKZ.name().equals(transferInInfo.getBidStatus())) {
            transferInInfoDetailVO.setCreditStatus(2);
        }
        if ("S".equals(transferInInfo.getIsTransfer())) {
            transferInInfoDetailVO.setCreditStatus(3);
        }
        if (Status.YJQ.name().equals(transferInInfo.getBidStatus())) {
            transferInInfoDetailVO.setCreditStatus(6);
        }
        if (!"F".equals(transferInInfo.getIsYq())) {
            transferInInfoDetailVO.setCreditStatus(5);
        }
        if (transferInInfo.getTransferOutId() > 0) {
            transferInInfoDetailVO.setCreditStatus(4);
            transferInInfoDetailVO.setExpectedProfit(transferInInfo.getArrivalProfit());
        }


        //2016-06-29 junda.feng
        transferInInfoDetailVO.setNextRepaymentDate(transferInInfo.getNextRepaymentDate()==null?null:transferInInfo.getNextRepaymentDate().getTime() / 1000);//下次还款日期    2016-06-29 junda.feng
        transferInInfoDetailVO.setInterestTime(transferInInfo.getInterestDate()==null?null:transferInInfo.getInterestDate().getTime()/1000);
//      transferInInfoDetailVO.setInterestPaymentType(transferInInfo.getInterestPaymentType());
        transferInInfoDetailVO.setInterestPaymentType("GDR");//按产品要求修改成GDR
        transferInInfoDetailVO.setRepaymentMode(transferInInfo.getRepaymentMode());

        //2016-07-11
        transferInInfoDetailVO.setNextRepaymentDate(transferInInfo.getNextRepaymentDate()==null?null:transferInInfo.getNextRepaymentDate().getTime() / 1000);//下次还款日期    2016-06-29 junda.feng
        transferInInfoDetailVO.setApplyTime(transferInInfo.getApplyTime()==null?null:transferInInfo.getApplyTime().getTime()/1000);
        transferInInfoDetailVO.setSuccessTime(transferInInfo.getSuccessTime()==null?null:transferInInfo.getSuccessTime().getTime()/1000);
        transferInInfoDetailVO.setActualRepaymentDate(transferInInfo.getActualRepaymentDate()==null?null:transferInInfo.getActualRepaymentDate().getTime()/1000);

        transferInInfoDetailVO.setAssetTypes(StringUtils.isNotEmpty(transferInInfo.getAssetTypes())?transferInInfo.getAssetTypes().split(","):new String[0]);
        transferInInfoDetailVO.setCreditAmount(transferInInfo.getCreditAmount().toPlainString());
        transferInInfoDetailVO.setExpectedAmount(transferInInfo.getCreditAmount().add(transferInInfo.getInterest()).toPlainString());
        transferInInfoDetailVO.setTotallAmount(transferInInfo.getCreditAmount().add(transferInInfo.getExpectedProfit()).toPlainString());
        response.setData(CommonTool.toMap(transferInInfoDetailVO));
        return response;
    }

    /**
     * 债权转让申请取消
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @return
     */
    @RequestMapping(value = "transferOut/cancel", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse transferOutCancel(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm, @RequestParam(required = true, value = "token") String token, @RequestParam(required = true, value = "userId") String userId, @RequestParam(required = true, value = "zqId") String zqId) throws Throwable {
        HttpResponse response = new HttpResponse();
        Map<String, Object> data = new HashMap<>(2);
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(zqId)) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            //添加前缀
            String code =InterfaceConst.CREDIT_NAME_PREFIX + tenderTransferManageService.cancel(Integer.valueOf(zqId), Integer.valueOf(userId));
            int count = this.zqzrManageImpl.getCreditassignmentCount(Integer.parseInt(zqId));
            data.put("code", code);
            data.put("count", InterfaceConst.ZQZR_CS - count);
            response.setData(data);
        } catch (Exception ex) {
            throw ex;
        }
        return response;
    }

    /**
     * 债权转让申请列表（转让中）
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "transferOut/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse applayforList(@ModelAttribute BaseRequestForm paramForm,
                               Integer zqDayMin, Integer zqDayMax, Integer transferValueMin, Integer transferValueMax,
                               Integer orderType, Integer sortType, Integer page, Integer limit, VersionTypeEnum versionType) throws Exception{
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()||versionType == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        PageBounds pageBounds=new PageBounds(page,limit);
        Map<String,Object> map=new HashMap<>();
        map.put("zqDayMin", zqDayMin);
        map.put("zqDayMax", zqDayMax);
        map.put("transferValueMin", transferValueMin);
        map.put("transferValueMax", transferValueMax);
        map.put("sortType", sortType);
        map.put("orderType", orderType);

        Map<String,Object> tenderMap=new HashMap<>();
        List<TransferOutInfo> transferOutInfos = transferInService.getTransferOutList(map,pageBounds,versionType==null?1:versionType.getIndex());
    	Pager pager = new Pager(transferOutInfos);
        TransferOutListVO transferOutListVO =new TransferOutListVO();
        List<TransferOutInfoVO> transferOutInfoVOs = new ArrayList<TransferOutInfoVO>();
        List<Integer> tenderIdList =new ArrayList<Integer>();
        for (TransferOutInfo transferOutInfo : transferOutInfos){
            tenderIdList.add(transferOutInfo.getRecordId());
        }
        tenderMap.put("list",tenderIdList);
        List<UserCoupons> userCouponsList = new ArrayList<UserCoupons>();
        if(!tenderIdList.isEmpty()) {
            userCouponsList = transferInService.getUserCoupons(tenderMap);
        }
        for (TransferOutInfo transferOutInfo : transferOutInfos){
            TransferOutInfoVO transferOutInfoVO = new TransferOutInfoVO();

            //加息利率（券加息+标加息）
            /*double bidInterestRise = transferOutInfo.getBidInterestRise();
            if(!userCouponsList.isEmpty()) {
                for (UserCoupons userCoupons : userCouponsList) {
                    if (transferOutInfo.getRecordId() == userCoupons.getTenderId()) {
                        bidInterestRise = bidInterestRise + userCoupons.getScope();
                    }
                }
            }
            transferOutInfoVO.setBidInterestRise(bidInterestRise);*/

//            double yearYield = transferOutInfo.getYearYield();
//            if(!userCouponsList.isEmpty()) {
//                for (UserCoupons userCoupons : userCouponsList) {
//                    if (transferOutInfo.getRecordId() == userCoupons.getTenderId()) {
//
//                        yearYield = yearYield + userCoupons.getScope();
//                    }
//                }
//            }
//            transferOutInfoVO.setYearYield(yearYield);
            //transferOutInfoVO.setYearYield(transferOutInfo.getYearYield());

            double yearYield = CalculateEarningsUtil.transferYieldForDay(transferOutInfo.getCollectInterest(),transferOutInfo.getTransferOutPrice(),transferOutInfo.getSurplusDays());
            transferOutInfoVO.setYearYield(yearYield);
            transferOutInfoVO.setApplyforId(transferOutInfo.getApplyforId());
            transferOutInfoVO.setTransferTitle(InterfaceConst.CREDIT_NAME_PREFIX+transferOutInfo.getCreditCode());
            transferOutInfoVO.setTransferOutValue(transferOutInfo.getTransferOutPrice());

            transferOutInfoVO.setSurplusDays(transferOutInfo.getSurplusDays());
//            transferOutInfoVO.setDiscountRate(transferOutInfo.getDiscountRate());
            transferOutInfoVO.setAssetTypes(StringUtils.isNotEmpty(transferOutInfo.getAssetTypes())?transferOutInfo.getAssetTypes().split(","):new String[0]);
            transferOutInfoVO.setTimestamp(DateUtil.dateToTimestampToSec(transferOutInfo.getTransferApplyforTime()));
            transferOutInfoVO.setCollectInterest(transferOutInfo.getCollectInterest());
            transferOutInfoVO.setSurplusDays(transferOutInfo.getSurplusDays()<0?0:transferOutInfo.getSurplusDays());
            transferOutInfoVO.setRepaymentMode(transferOutInfo.getRepaymentMode());
            transferOutInfoVOs.add(transferOutInfoVO);
        }
        pager.setItems(transferOutInfoVOs);
        response.setData(CommonTool.toMap(pager));
        return response;
    }

    /**
     * 债权转让申请详情（转让中）
     * @param paramForm
     * @param applyforId  债权转让申请ID
     * @return
     */
    @RequestMapping(value = "transferOut/detail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse applayforDetail(@ModelAttribute BaseRequestForm paramForm,String applyforId) throws Exception{
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(applyforId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        TransferOutInfo transferOutInfo = transferInService.getTransferOutDetail(Integer.valueOf(applyforId));
        if(transferOutInfo==null){
            response.setCodeMessage(ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getCode(), ResponseCode.ZQZR_APPLY_DATA_NOT_FOUND.getMessage());
            return response;
        }
        TransferOutDetailVO transferOutDetailVO = new TransferOutDetailVO();
        List<Integer> tenderIdList =new ArrayList<Integer>();
        Map<String,Object> tenderMap=new HashMap<>();
        tenderIdList.add(transferOutInfo.getRecordId());
        tenderMap.put("list",tenderIdList);
        List<UserCoupons> userCouponsList = new ArrayList<UserCoupons>();
        if(!tenderIdList.isEmpty()) {
            userCouponsList = transferInService.getUserCoupons(tenderMap);
        }

            //加息利率（券加息+标加息）
            /*double bidInterestRise = transferOutInfo.getBidInterestRise();
            if(!userCouponsList.isEmpty()) {
                for (UserCoupons userCoupons : userCouponsList) {
                    if (transferOutInfo.getRecordId() == userCoupons.getTenderId()) {
                        bidInterestRise = bidInterestRise + userCoupons.getScope();
                    }
                }
            }
        transferOutDetailVO.setBidInterestRise(bidInterestRise);*/
        transferOutDetailVO.setApplyforId(transferOutInfo.getApplyforId());
        transferOutDetailVO.setBidId(transferOutInfo.getBidId());
        //transferOutDetailVO.setYearYield(transferOutInfo.getYearYield());
        double yearYield = CalculateEarningsUtil.transferYieldForDay(transferOutInfo.getCollectInterest(),transferOutInfo.getTransferOutPrice(),transferOutInfo.getSurplusDays());
        transferOutDetailVO.setYearYield(yearYield);
        transferOutDetailVO.setCollectInterest(transferOutInfo.getCollectInterest());
        transferOutDetailVO.setAssignmentAgreementUrl(Config.get("assignment.agreement.url"));
        transferOutDetailVO.setOriginalCreditAmount(transferOutInfo.getOriginalCreditAmount());
        transferOutDetailVO.setRepaymentMode(transferOutInfo.getRepaymentMode());
        transferOutDetailVO.setTransferOutValue(transferOutInfo.getTransferOutPrice());
        transferOutDetailVO.setTransferTitle(InterfaceConst.CREDIT_NAME_PREFIX+transferOutInfo.getCreditCode());
        transferOutDetailVO.setSurplusDays(transferOutInfo.getSurplusDays());
        transferOutDetailVO.setAssetTypes(StringUtils.isNotEmpty(transferOutInfo.getAssetTypes())?transferOutInfo.getAssetTypes().split(","):new String[0]);
        transferOutDetailVO.setRemark(transferOutInfo.getRemark());
        transferOutDetailVO.setBorrowerUrl(Config.get("bid.borrower.url")+transferOutInfo.getBidId());
        transferOutDetailVO.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(transferOutInfo.getBidId()));
        transferOutDetailVO.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(transferOutInfo.getBidId()));
        transferOutDetailVO.setGroupInfoList(bidInfoService.getGroupInfoList(transferOutInfo.getBidId()));
        transferOutDetailVO.setInterestPaymentType(PaymentTypeEnum.getNameByCode("GDR"));//junda.feng


        ShopTreasureInfo  info = this.bidInfoService.getBidInfo(Integer.valueOf(transferOutInfo.getBidId()));

        transferOutDetailVO.setProjectInfo(ProjectInfoToJson(info,null));
        if(info.getItemType()==0) {
            transferOutDetailVO.setBorrowerInfo(BorrowerInfoToJson(borrowInfoService.getBorrowerDetail(Integer.valueOf(transferOutInfo.getBidId()))));
        }else {
            transferOutDetailVO.setBorrowerInfo(new LinkedHashMap<String, Object>());
        }
        transferOutDetailVO.setIsShowFile(showAfterLoanInfo(info.getLoanDays(),info.getMonth(),info.getInterestDate()));
        response.setData(CommonTool.toMap(transferOutDetailVO));
        return response;
    }

    /**
     * 债权转让申请信息(申请时需显示的信息)
     * @param paramForm
     * @param token
     * @param userId
     * @param creditId   债权ID
     * @return
     */
    @RequestMapping(value = "applyfor/info", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse applayforInfo(@ModelAttribute BaseRequestForm paramForm, @RequestParam(required = false, value = "token") String token,
                               @RequestParam(required = false, value = "userId") String userId,
                               @RequestParam(required = false, value = "creditId") String creditId) throws  Exception{
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || StringUtils.isEmpty(creditId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        List<Finacing> list = this.finacingService.getFinacing(userId, null, creditId);
        if (null == list || list.size() == 0) {
            response.setCodeMessage(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(), ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
            return response;
        }
        Finacing finacing = list.get(0);

        //校验规则
        response = validate(finacing,creditId,userId);
        if(!response.getCode().equals(ResponseCode.SUCCESS.getCode())){
            return response;
        }

        TransferApplyforInfoVO transferApplyforInfoVO = new TransferApplyforInfoVO();
        transferApplyforInfoVO.setCreditId(finacing.getId());
        double bidMoney =getBidValue(finacing,creditId,userId);

        transferApplyforInfoVO.setCreditCapitalAmount(new BigDecimal(bidMoney).setScale(2,BigDecimal.ROUND_HALF_UP));

        String assignmentRate=String.valueOf(InterfaceConst.ASSIGNMENT_RATE);
        if(finacing.getAnytimeQuit()==1){//随时退出标的费率不一样
            assignmentRate=String.valueOf(InterfaceConst.ASSIGNMENT_RATE_ANNYTIME_QUIT);
        }
        transferApplyforInfoVO.setAssignmentRate(assignmentRate);
        transferApplyforInfoVO.setAssignmentAgreement(Config.get("assignment.agreement.url"));
        int assignmentCount = this.zqzrManageImpl.getCreditassignmentCount(Integer.parseInt(creditId));
        transferApplyforInfoVO.setCreditassignmentCount(assignmentCount);
        transferApplyforInfoVO.setCanAssignmentCount(InterfaceConst.ZQZR_CS);

        BigDecimal passedEarning = getExpectedProfit(finacing,creditId,userId);
        transferApplyforInfoVO.setPassedEarning(passedEarning);

        int surplusDays = DateUtil.getDayBetweenDates(DateUtil.nowDate(),finacing.getEndTimestamp());
        transferApplyforInfoVO.setSurplusDays(surplusDays<0?0:surplusDays);

        response.setData(CommonTool.toMap(transferApplyforInfoVO));
        return response;
    }

    /**
     * 确认债权转让申请
     * @param paramForm
     * @param token
     * @param userId         用户ID
     * @param zqId           债权ID
     * @param transferValue  转出金额
     * @param passedEarning  债权已过天数的收益
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "transferOut/applyfor", method = RequestMethod.POST, headers = APIVersion.V_1_0_0)
    HttpResponse applyforSubmit(@ModelAttribute BaseRequestForm paramForm,
                                @RequestParam(required = false, value = "token") String token,
                                @RequestParam(required = false, value = "userId") String userId,
                                @RequestParam(required = false, value = "zqId") final String zqId,
                                @RequestParam(required = false, value = "transferValue") final String transferValue,
                                @RequestParam(required = false, value = "passedEarning") final String passedEarning,
                                @RequestParam(required = false, value = "dealPassword") String dealPassword,
                                @RequestParam(required = false, value = "versionType") VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if( versionType == null){
            versionType = VersionTypeEnum.PT;
        }
        if(versionType!=null&&versionType.getCode().equals("PT")){
            response.setCodeMessage(ResponseCode.NOT_SUPPURT_OUT_TRANFER);
            return response;
        }

        try {
            if (!paramForm.validate() || (versionType==VersionTypeEnum.PT&&StringUtils.isEmpty(dealPassword)) || StringUtils.isEmpty(zqId) || StringUtils.isEmpty(transferValue)) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }

            //验证用户交易密码
            if(versionType!=VersionTypeEnum.CG) {
                if (!iTradeService.isValidUserPwd(Integer.valueOf(userId), dealPassword)) {
                    response.setCodeMessage(ResponseCode.TRADE_USER_NOT_RIGHT_PASSWORD.getCode(), ResponseCode.TRADE_USER_NOT_RIGHT_PASSWORD.getMessage());//交易密码错误
                    return response;
                }
            }

            List<Finacing> list = this.finacingService.getFinacing(userId, null, zqId);
            if (null == list || list.size() == 0) {
                response.setCodeMessage(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(), ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
                return response;
            }
            Finacing finacing = list.get(0);
            //校验规则
            response = validate(finacing,zqId,userId);
            if(!response.getCode().equals(ResponseCode.SUCCESS.getCode())){
                return response;
            }

            BigDecimal expectedProfit=getExpectedProfit(finacing,zqId,userId);
            final BigDecimal finalExpectedProfit = expectedProfit;

            double bidMoney =getBidValue(finacing,zqId,userId);
            final double bidValue = bidMoney;

            //随时退出标需要考虑费率
            BigDecimal assignmentRate=BigDecimal.valueOf(InterfaceConst.ASSIGNMENT_RATE);
            if(finacing.getAnytimeQuit()==1){//随时退出标的费率不一样
                assignmentRate=BigDecimal.valueOf(InterfaceConst.ASSIGNMENT_RATE_ANNYTIME_QUIT);
            }

            final BigDecimal bidAssignmentRate=assignmentRate;

            zqzrManageImpl.transfer(new AddTransfer() {
                @Override
                public BigDecimal getTransferValue() {
                    return BigDecimalParser.parse(transferValue);
                }

                @Override
                public int getTransferId() {
                    return Integer.parseInt(zqId);
                }

                @Override
                public BigDecimal getRateMoney() {
                    return bidAssignmentRate;
                }

                @Override
                public BigDecimal getBidValue() {
                    return new BigDecimal(bidValue);
                }

                @Override
                public BigDecimal getPassedEarning() {
                    return finalExpectedProfit;
                }
            });
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            response.setCodeMessage(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage());
            logger.error("[CreditAssignmentController.applyforSubmit]" + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * 已购买的债权列表
     *
     * @return
     */
    @RequestMapping(value = "/buyedTransfer/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse buyedTransferList(HttpServletRequest request,
                                   BaseRequestForm paramForm,
                                   String token,
                                   String userId,
                                   Integer page,
                                   Integer limit,
                                   VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)||versionType == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        PageBounds pageBounds=new PageBounds(page, limit);
        List<TransferInInfo> transferInInfos = transferInService.getBuyedTransferList(userId, pageBounds,versionType==null?1:versionType.getIndex());
        List<TransferInBuyedInfoVO> transferInBuyedInfoVOs = new ArrayList<>();
        if(transferInInfos!=null){
            for (TransferInInfo transferInInfo : transferInInfos) {
                TransferInBuyedInfoVO vo = new TransferInBuyedInfoVO();
                vo.setCreditId(transferInInfo.getCreditId());
                vo.setCreditTitle(transferInInfo.getZrId()==0?transferInInfo.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX+transferInInfo.getCreditCode());
                vo.setPurchaseTime(transferInInfo.getHoldTime());
                vo.setCreditStatus(2);
                vo.setYearYield(transferInInfo.getYearYield());
                vo.setSurplusDays(transferInInfo.getSurplusDays());
                vo.setInvestAmount(transferInInfo.getInvestAmount().toPlainString());
                vo.setExpectedProfit(transferInInfo.getExpectedProfit());
                vo.setExpireDate(transferInInfo.getExpireDate());
                vo.setJxFlag(transferInInfo.isJxFlag());
                transferInBuyedInfoVOs.add(vo);
            }
        }
        Pager pager = new Pager(transferInInfos);
        pager.setItems(transferInBuyedInfoVOs);
        response.setData(CommonTool.toMap(pager));
        return response;
    }
    
    /**
     * 可转债权列表信息by:junda.feng 
     * 条件：1债权至少持有时间30天，2债权没有转出记录，3债权非转让中，4标还款中状态，5还款类型为一次还清，6债权已转让次数少于3次
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/allowTransfer/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse allowTransferList(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
       String token,String userId,Integer page,Integer limit,VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)||versionType == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        PageBounds pageBounds=new PageBounds(page, limit);
        Map<String,Object> map=new HashMap<>();
        map.put("userId", userId);
        map.put("zqHoldDayMin", InterfaceConst.ZQZR_CY_DAY);//至少持有时间
        map.put("zqHoldDayLast", InterfaceConst.ZQZR_CY_LAST_DAY);//债权持有的最后多天前可转让
        map.put("isTransfer", "F");//非转让中
        map.put("bidStatus", new String[]{Status.HKZ.name()});//还款中状态
        map.put("zqzrcs", InterfaceConst.ZQZR_CS);//债权可转让次数
        map.put("cgNum", versionType==null?1:versionType.getIndex());
        List<TransferInInfo> transferInInfos = transferInService.getAllowTransferInInfoList(map, pageBounds);
      
        List<AllowTransferInfoVO> transferInInfoVOs = new ArrayList<AllowTransferInfoVO>();
        if(transferInInfos!=null){
        	 for (TransferInInfo transferInInfo : transferInInfos) {
             	AllowTransferInfoVO vo = new AllowTransferInfoVO(transferInInfo);
                 if(transferInInfo.getTransferOutId()>0){
                     //购买的债权预期收益包含差价：预期收益 = 预期本息 — 转让金额  2017-2-20 by kris
                     Map exp=transferInService.getExpectedProfit(transferInInfo.getCreditId());
                     vo.setExpectedProfit(((BigDecimal) exp.get("expectedProfit")).toPlainString());
                 }
                transferInInfoVOs.add(vo);
             }
        }
    	Pager pager = new Pager(transferInInfos);
        pager.setItems(transferInInfoVOs);
		response.setData(CommonTool.toMap(pager));
        return response;
    }
    
    /**
     * 正在转让债权列表 by:junda.feng 
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/inTransfer/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse inTransferList(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
       String token,String userId,Integer page,Integer limit,VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)||versionType == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        PageBounds pageBounds=new PageBounds(page, limit);
        Map<String,Object> map=new HashMap<>();
        map.put("userId", userId);
        map.put("cgNum", versionType==null?1:versionType.getIndex());
        List<TransferInInfo> transferInInfos = transferInService.getInTransferInInfoList(map, pageBounds);
        List<InTransferInfoVO> transferInInfoVOs = new ArrayList<InTransferInfoVO>();
        if(transferInInfos!=null){
	        for (TransferInInfo transferInInfo : transferInInfos) {
	        	InTransferInfoVO vo = new InTransferInfoVO(transferInInfo);
	            transferInInfoVOs.add(vo);
	        }
        }
        Pager pager = new Pager(transferInInfos);
        pager.setItems(transferInInfoVOs);
		response.setData(CommonTool.toMap(pager));
        return response;
    }
    
    /**
     * 转让成功的债权列表 by:junda.feng 
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/successTransfer/list", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse successTransferList(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
       String token,String userId,Integer page,Integer limit,VersionTypeEnum versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)||versionType == null) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        PageBounds pageBounds=new PageBounds(page, limit);
        Map<String,Object> map=new HashMap<>();
        map.put("userId", userId);
        map.put("cgNum", versionType==null?1:versionType.getIndex());
        List<TransferInInfo> transferInInfos = transferInService.getSuccessTransferInInfoList(map, pageBounds);
        List<SuccessTransferInfoVO> transferInInfoVOs = new ArrayList<SuccessTransferInfoVO>();
        for (TransferInInfo transferInInfo : transferInInfos) {
        	SuccessTransferInfoVO vo = new SuccessTransferInfoVO(transferInInfo);
            transferInInfoVOs.add(vo);
        }
        Pager pager = new Pager(transferInInfos);
        pager.setItems(transferInInfoVOs);
		response.setData(CommonTool.toMap(pager));
        return response;
    }


    //校验
    public HttpResponse validate(Finacing finacing,String creditId,String userId){
        HttpResponse response = new HttpResponse();
        if (Status.S.name().equals(finacing.getIsTransfer())) {//正在转让中
            response.setCodeMessage(ResponseCode.ZQZR_IS_TRANSFER.getCode(), ResponseCode.ZQZR_IS_TRANSFER.getMessage());
            return response;
        }

        //新手标不能债权转让
        if (finacing.getAnytimeQuit()!=1 &&Status.S.name().equals(finacing.getIsNoviceBid())) {//是新手标，不能转让
            response.setCodeMessage(ResponseCode.ZQZR_IS_NOVICEBID.getCode(), ResponseCode.ZQZR_IS_NOVICEBID.getMessage());
            return response;
        }

        //正在转让中
        if (finacing.getIsTransfer().equals(Status.S.name())) {
            response.setCodeMessage(ResponseCode.ZQZR_IS_TRANSFER.getCode(), ResponseCode.ZQZR_IS_TRANSFER.getMessage());
            return response;
        }

        //债权转让次数
        int assignmentCount = this.zqzrManageImpl.getCreditassignmentCount(Integer.parseInt(creditId));
        if (finacing.getAnytimeQuit()!=1 && assignmentCount >= InterfaceConst.ZQZR_CS) {
            response.setCodeMessage(ResponseCode.ZQZR_CSGD);
            return response;
        }

        //债权持有时间超过3天
        if (finacing.getAnytimeQuit()!=1
                && DateUtil.getDayBetweenDates(finacing.getCreateTime(), DateUtil.nowDate()) < InterfaceConst.ZQZR_CY_DAY) {
            response.setCodeMessage(ResponseCode.ZQZR_SJWD);
            return response;
        }

        //距离标下一个还款时间小于x天不能转让
        int[] tradeTypes = new int[]{ FeeCode.TZ_LX};
        List<RepaymentInfo>  repaymentInfo = finacingService.getNextRepaymentItem(Integer.parseInt(userId)
                ,Integer.parseInt(creditId),tradeTypes);
        Date expectedRepaymentDate= repaymentInfo.size()>0?repaymentInfo.get(0).getExpectedRepaymentDate(): finacing.getEndTimestamp();
        if (finacing.getAnytimeQuit()!=1
                && DateUtil.getDayBetweenDates( DateUtil.nowDate(),expectedRepaymentDate) < InterfaceConst.ZQZR_CY_LAST_DAY) {
            response.setCodeMessage(ResponseCode.ZQZR_SJKD);
            return response;
        }
        return response;
    }

    //算利息
    public BigDecimal getExpectedProfit(Finacing finacing,String zqId,String userId){
        BigDecimal expectedProfit =BigDecimal.ZERO;
        //借款周期参数
        int loanPeriod=finacing.getMonth()==0?finacing.getLoanDays():finacing.getMonth();
        int period=finacing.getMonth()==0?365:12;

        //一次还清的利息计算
        if (RepaymentModeEnum.YCFQ.getCode().equals(finacing.getRepaymentMethod())) {
            //已过天数的收益（未到账）
            int passedDays = DateUtil.getDayBetweenDates(finacing.getBeginTimestamp(),DateUtil.nowDate());//已算收益天数
            int totalDays = DateUtil.getDayBetweenDates(finacing.getBeginTimestamp(),finacing.getEndTimestamp());//当前债权一共计息的天数

            if(totalDays>0){
                BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney())
                        .multiply(new BigDecimal(finacing.getRate()))
                        .multiply(new BigDecimal(loanPeriod))
                        .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
                expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
            }
        }

        //等额本息的收益计算
        if (RepaymentModeEnum.DEBX.getCode().equals(finacing.getRepaymentMethod())) {
            //查出下一期回款计划里面的利息
            int[] tradeTypes = new int[]{ FeeCode.TZ_LX,FeeCode.TZ_JXJL};
            Map nextRepaymentItemProfit = finacingService.getNextRepaymentItemProfit(0 ,Integer.parseInt(zqId),tradeTypes);

            BigDecimal totalEarning=BigDecimal.ZERO;
            if(nextRepaymentItemProfit!=null)totalEarning= (BigDecimal) nextRepaymentItemProfit.get("profitRepaymentAmount");

            tradeTypes = new int[]{ FeeCode.TZ_LX};
            RepaymentInfo lastRepaymentInfo = finacingService.getLastRepaymentItem(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);  //上一期的回款计划
            Date lastRepaymentDate = lastRepaymentInfo==null?finacing.getCreateTime():lastRepaymentInfo.getExpectedRepaymentDate();

            List<RepaymentInfo> nextRepayment = finacingService.getNextRepaymentItem(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);  //下一期的回款计划
            Date nextRepaymentDate = nextRepayment.get(0).getExpectedRepaymentDate();

            //当月已过天数的收益（未到账）
            int passedDays= DateUtil.daysToLastRepaymentDay(lastRepaymentDate,new Date());//当月收益天数,当天收益不计
            int totalDays = DateUtil.getDayBetweenDates(lastRepaymentDate,nextRepaymentDate);//当月债权一共计息的天数
            if(totalDays>0){
                expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
            }
        }

        //每月付息到期还本的收益计算
        if (RepaymentModeEnum.MYFX.getCode().equals(finacing.getRepaymentMethod())) {
            //当月已过天数的收益（未到账）
            int[] tradeTypes = new int[]{ FeeCode.TZ_LX};
            RepaymentInfo  repaymentInfo = finacingService.getLastRepaymentItem(0 ,Integer.parseInt(zqId),tradeTypes);  // 上一期的回款计划
            Date effectDate=repaymentInfo==null?finacing.getCreateTime():repaymentInfo.getExpectedRepaymentDate();

            int passedDays= DateUtil.daysToLastRepaymentDay(effectDate,new Date());//当月收益天数,当天收益不计
            int totalDays = DateUtil.getDayBetweenDates(finacing.getBeginTimestamp(),finacing.getEndTimestamp());//当前债权一共计息的天数
            if(totalDays>0){
                BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney())
                        .multiply(new BigDecimal(finacing.getRate()))
                        .multiply(new BigDecimal(loanPeriod))
                        .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
                expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
            }
        }
        return expectedProfit;
    }

    public double getBidValue(Finacing finacing,String zqId,String userId){
        //债权金额--等额本息方式要计算本金
        double bidMoney = finacing.getOriginalMoney();
        if (RepaymentModeEnum.DEBX.getCode().equals(finacing.getRepaymentMethod())) {//等额本息的本金计算
            //查出剩余期数未还本金
            int[] tradeTypes = new int[]{ FeeCode.TZ_BJ};
            List<RepaymentInfo>  repaymentInfos = finacingService.getUserRepaymentItem(Integer.parseInt(userId)
                    ,Integer.parseInt(zqId),tradeTypes);
            BigDecimal creditCapitalAmount=BigDecimal.ZERO;
            for(RepaymentInfo item:repaymentInfos){
                if("WH".equals(item.getRepaymentStatus())){
                    creditCapitalAmount = creditCapitalAmount.add(item.getRepaymentAmount());
                }
            }
            creditCapitalAmount=creditCapitalAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
            bidMoney=creditCapitalAmount.doubleValue();
        }
        return bidMoney;
    }

    /**
     * 转入债权详情信息
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "planTransferInDetail/detail", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse planTransferInDetail(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
                                  @RequestParam(required = true, value = "token") String token,
                                  @RequestParam(required = true, value = "userId") String userId,
                                  @RequestParam(required = false, value = "creditId") String creditId) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(creditId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        TransferInInfoDetailVO transferInInfoDetailVO = new TransferInInfoDetailVO();

        TransferInInfo transferInInfo = transferInService.getTransferInInfoDetail(Integer.valueOf(userId), Integer.valueOf(creditId));
        if (transferInInfo == null) {
            return response;
        }
        List<Integer> tenderIdList =new ArrayList<Integer>();
        Map<String,Object> tenderMap=new HashMap<>();
        tenderIdList.add(transferInInfo.getCreditId());
        tenderMap.put("list",tenderIdList);
        List<UserCoupons> userCouponsList = new ArrayList<UserCoupons>();
        if(!tenderIdList.isEmpty()) {
            userCouponsList = transferInService.getUserCoupons(tenderMap);
        }
        //加息利率（券加息+标加息）
        double bidInterestRise = transferInInfo.getBidInterestRise();
        transferInInfoDetailVO.setJxFlag(false);
        if(!userCouponsList.isEmpty()) {
            for (UserCoupons userCoupons : userCouponsList) {
                if (transferInInfo.getCreditId() == userCoupons.getTenderId()) {
                    bidInterestRise = bidInterestRise + userCoupons.getScope();
                }
            }
            transferInInfoDetailVO.setJxFlag(true);
        }
        transferInInfoDetailVO.setBidInterestRise(bidInterestRise);
        transferInInfoDetailVO.setCreditId(transferInInfo.getCreditId());
        transferInInfoDetailVO.setBidId(transferInInfo.getBidId());
        transferInInfoDetailVO.setCreditTitle(InterfaceConst.CREDIT_NAME_PREFIX + transferInInfo.getCreditCode());
        transferInInfoDetailVO.setPurchaseTime(DateUtil.dateToTimestampToSec(transferInInfo.getPurchaseTime()));
        transferInInfoDetailVO.setYearYield(transferInInfo.getYearYield());
        transferInInfoDetailVO.setSurplusDays(transferInInfo.getSurplusDays()>0?transferInInfo.getSurplusDays():0);//如果过了到期时间，剩余债权天数为0
        transferInInfoDetailVO.setInvestAmount(transferInInfo.getInvestAmount());
        transferInInfoDetailVO.setExpectedProfit(transferInInfo.getExpectedProfit());
        transferInInfoDetailVO.setInvestDate(DateUtil.dateToTimestampToSec(transferInInfo.getInvestDate()));
        transferInInfoDetailVO.setExpireDate(DateUtil.dateToTimestampToSec(transferInInfo.getExpireDate()));
        transferInInfoDetailVO.setAssignmentAgreementUrl(transferInService.getUserAssignmentAgreementUrl(Integer.valueOf(userId)));
        transferInInfoDetailVO.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(transferInInfo.getBidId()));
        transferInInfoDetailVO.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(transferInInfo.getBidId()));
        transferInInfoDetailVO.setRemark(transferInInfo.getRemark());
        transferInInfoDetailVO.setBorrowerUrl(Config.get("bid.borrower.url")+transferInInfo.getBidId());
        transferInInfoDetailVO.setGroupInfoList(bidInfoService.getGroupInfoList(transferInInfo.getBidId()));


        //标的状态
        if (Status.TBZ.name().equals(transferInInfo.getBidStatus()) || Status.DFK.name().equals(transferInInfo.getBidStatus())) {
            transferInInfoDetailVO.setCreditStatus(1);
        }
        if (Status.HKZ.name().equals(transferInInfo.getBidStatus())) {
            transferInInfoDetailVO.setCreditStatus(2);
        }
        if ("S".equals(transferInInfo.getIsTransfer())) {
            transferInInfoDetailVO.setCreditStatus(3);
        }
        if (Status.YJQ.name().equals(transferInInfo.getBidStatus())) {
            transferInInfoDetailVO.setCreditStatus(6);
        }
        if (!"F".equals(transferInInfo.getIsYq())) {
            transferInInfoDetailVO.setCreditStatus(5);
        }
        if (transferInInfo.getTransferOutId() > 0) {
            transferInInfoDetailVO.setCreditStatus(4);
            transferInInfoDetailVO.setExpectedProfit(transferInInfo.getArrivalProfit());
        }


        //2016-06-29 junda.feng
        transferInInfoDetailVO.setNextRepaymentDate(transferInInfo.getNextRepaymentDate()==null?null:transferInInfo.getNextRepaymentDate().getTime() / 1000);//下次还款日期    2016-06-29 junda.feng
        transferInInfoDetailVO.setInterestTime(transferInInfo.getInterestDate()==null?null:transferInInfo.getInterestDate().getTime()/1000);
//      transferInInfoDetailVO.setInterestPaymentType(transferInInfo.getInterestPaymentType());
        transferInInfoDetailVO.setInterestPaymentType("GDR");//按产品要求修改成GDR
        transferInInfoDetailVO.setRepaymentMode(transferInInfo.getRepaymentMode());

        //2016-07-11
        transferInInfoDetailVO.setNextRepaymentDate(transferInInfo.getNextRepaymentDate()==null?null:transferInInfo.getNextRepaymentDate().getTime() / 1000);//下次还款日期    2016-06-29 junda.feng
        transferInInfoDetailVO.setApplyTime(transferInInfo.getApplyTime()==null?null:transferInInfo.getApplyTime().getTime()/1000);
        transferInInfoDetailVO.setSuccessTime(transferInInfo.getSuccessTime()==null?null:transferInInfo.getSuccessTime().getTime()/1000);
        transferInInfoDetailVO.setActualRepaymentDate(transferInInfo.getActualRepaymentDate()==null?null:transferInInfo.getActualRepaymentDate().getTime()/1000);

        transferInInfoDetailVO.setAssetTypes(StringUtils.isNotEmpty(transferInInfo.getAssetTypes())?transferInInfo.getAssetTypes().split(","):new String[0]);
        transferInInfoDetailVO.setCreditAmount(transferInInfo.getCreditAmount().toPlainString());
        transferInInfoDetailVO.setExpectedAmount(transferInInfo.getCreditAmount().add(transferInInfo.getInterest()).toPlainString());
        transferInInfoDetailVO.setTotallAmount(transferInInfo.getCreditAmount().add(transferInInfo.getExpectedProfit()).toPlainString());
        response.setData(CommonTool.toMap(transferInInfoDetailVO));
        return response;
    }

    private LinkedHashMap ProjectInfoToJson(ShopTreasureInfo info, String fwUrl){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName(info.getName());
       /* projectInfo.setLoanAmount(info.getLoanAmount());
        projectInfo.setRate(info.getRate());
        projectInfo.setGuarantee("信用借款");
        projectInfo.setInterestDate("满额起息");
        projectInfo.setInterestType("按天计息");
        projectInfo.setFwUrl(fwUrl);
        projectInfo.setIsEarlyExit("不支持");
        projectInfo.setLoanPurpose("资金周转");*/

        projectInfo.setName(info.getName());
        projectInfo.setLoanAmount(info.getLoanAmount());
        projectInfo.setRate(info.getRate());
        if(!StringUtils.isEmpty(info.getGuaranteeMeasure())){
            projectInfo.setGuarantee(GuaranteeMeasureEunm.get(info.getGuaranteeMeasure()));
        }
        projectInfo.setInterestDate("放款起息");
        projectInfo.setInterestType("按天计息");
        projectInfo.setRepaymentMode(RepaymentModeEnum.get(info.getRepaymentMode()));
        projectInfo.setFwUrl(fwUrl);
        if(info.getAnytimeQuit()==1){
            projectInfo.setIsEarlyExit("支持");
        }else {
            projectInfo.setIsEarlyExit("不支持");
        }

        projectInfo.setLoanPurpose(info.getLoanUsage());

        if(info.getLoanDays()==0){
            projectInfo.setLoanTerm(String.valueOf(info.getMonth()).concat("个月"));
        }else{
            projectInfo.setLoanTerm(String.valueOf(info.getLoanDays()).concat("天"));
        }
        projectInfo.setAssetsType(info.getAssetsType());
        projectInfo.setRepaymentOrigin("经营收入");
        String str = Jackson.getBaseJsonData(projectInfo);
        Gson gson = new Gson();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map = gson.fromJson(str, map.getClass());
        return map;
    }

    private LinkedHashMap BorrowerInfoToJson(BorrowerDetail detail){
        if(!StringUtils.isBlank(detail.getAddress())){
            /*int replaceIndex = detail.getAddress().indexOf("市");*/
            StringBuffer buffer = new StringBuffer(detail.getAddress());
            String address = null;
            if(detail.getAddress().length()>=5) {
                address  = buffer.replace(5, detail.getAddress().length(), "*****").toString();
            }else {
                address  = buffer.append("********").substring(0,10);
            }
            detail.setAddress(address);
        }
        detail.setSubjectType(LoanUserEnum.get(Integer.valueOf(detail.getSubjectType())));
        String str = Jackson.getBaseJsonData(detail);
        Gson gson = new Gson();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map = gson.fromJson(str, map.getClass());
        return map;
    }

    private int showAfterLoanInfo(int loanDay , int loanMonth ,Date interestDate) {
        if(interestDate!=null&&((loanDay!=0 && loanDay<180)||(loanMonth!=0 && loanMonth<6))){
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(interestDate);
            startCalendar.add(Calendar.MONTH, +1);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.SECOND,0);
            startCalendar.set(Calendar.MINUTE,0);
            if(new Date().getTime()>startCalendar.getTime().getTime()){
                return 1;
            }else {
                return 0;
            }
        }
        else if(interestDate!=null&&((loanDay!=0 && loanDay>=180)||(loanMonth!=0 && loanMonth>=6))){
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(interestDate);
            startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3 + 1) * 3);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.SECOND,0);
            startCalendar.set(Calendar.MINUTE,0);
            Date a = startCalendar.getTime();
            if(new Date().getTime()>startCalendar.getTime().getTime()){
                return 1;
            }else {
                return 0;
            }
        }
        return 0;
    }
}
