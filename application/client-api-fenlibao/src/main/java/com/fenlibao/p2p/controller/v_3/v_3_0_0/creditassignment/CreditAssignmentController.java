/**
 * Copyright © 2015 fenlibao . All rights reserved.
 *
 * @Title: CreditassignmentController.java
 * @Prject: client-api-fenlibao
 * @Package: com.fenlibao.p2p.controller.v_2.v_3_0_0.creditassignment
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-22 上午9:34:57
 * @version: V1.0.0
 */
package com.fenlibao.p2p.controller.v_3.v_3_0_0.creditassignment;

import com.dimeng.util.parser.BigDecimalParser;
import com.fenlibao.p2p.model.entity.Finacing;
import com.fenlibao.p2p.model.entity.creditassignment.AddTransfer;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.entity.creditassignment.TransferOutInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.creditassignment.*;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.creditassignment.TenderTransferManageService;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import com.fenlibao.p2p.service.creditassignment.ZqzrManage;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName: CreditassignmentController
 * @Description: 债权转让相关接口
 * @author: laubrence
 * @date: 2015-10-22 上午9:34:57  
 */

@RestController("v_3_0_0/CreditAssignmentController")
@RequestMapping(value = "creditassignment", headers = APIVersion.v_3_0_0)
public class CreditAssignmentController {

    private static final Logger logger = LogManager.getLogger(CreditAssignmentController.class);

    @Resource
    TransferInService transferInService;

    @Resource
    TenderTransferManageService tenderTransferManageService;

    @Resource
    private ZqzrManage zqzrManageImpl;

    @Resource
    private ITradeService iTradeService;

    @Resource
    BidInfoService bidInfoService;

    @Resource
    FinacingService finacingService;

    /**
     * 债权转让购买之前判断是否可以购买
     * @param request
     * @param paramForm
     * @param token
     * @param userId
     * @param applyforId
     * @return
     */
    @RequestMapping(value = "transferIn/isUserCanPurchase", method = RequestMethod.POST)
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
    @RequestMapping(value = "transferIn/purchase", method = RequestMethod.POST)
    HttpResponse transferInPurchase(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
                                    @RequestParam(required = true, value = "token") String token,
                                    @RequestParam(required = true, value = "userId") String userId,
                                    @RequestParam(required = true, value = "applyforId") String applyforId,
                                    @RequestParam(required = true, value = "dealPassword") String dealPassword) throws Throwable {
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(applyforId) || StringUtils.isEmpty(dealPassword)) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }
            //验证用户购买交易密码是否正确
            if (!iTradeService.isValidUserPwd(Integer.valueOf(userId), dealPassword)) {
                response.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
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
    @RequestMapping(value = "transferIn/list", method = RequestMethod.GET)
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

            DecimalFormat myformat=new DecimalFormat("0.00");
            transferInInfoVO.setInvestAmount(myformat.format(transferInInfo.getInvestAmount()));
            
            transferInInfoVO.setExpectedProfit(transferInInfo.getExpectedProfit());
            //2016-06-29 junda.feng
            transferInInfoVO.setNextRepaymentDate(transferInInfo.getNextRepaymentDate()==null?null:transferInInfo.getNextRepaymentDate().getTime() / 1000);//下次还款日期    2016-06-29 junda.feng
            transferInInfoVO.setApplyTime(transferInInfo.getApplyTime()==null?null:transferInInfo.getApplyTime().getTime()/1000);
            transferInInfoVO.setSuccessTime(transferInInfo.getSuccessTime()==null?null:transferInInfo.getSuccessTime().getTime()/1000);
            transferInInfoVO.setActualRepaymentDate(transferInInfo.getActualRepaymentDate()==null?null:transferInInfo.getActualRepaymentDate().getTime()/1000);
            //2016-12-21
            transferInInfoVO.setRepaymentMode(transferInInfo.getRepaymentMode());
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
           
            if (!"F".equals(transferInInfo.getIsYq())) {
                transferInInfoVO.setCreditStatus(5);
            }
            if (Status.YJQ.name().equals(transferInInfo.getBidStatus())) {
                transferInInfoVO.setCreditStatus(6);
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
    @RequestMapping(value = "transferIn/detail", method = RequestMethod.GET)
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
        transferInInfoDetailVO.setCreditId(transferInInfo.getCreditId());
        transferInInfoDetailVO.setBidId(transferInInfo.getBidId());
        transferInInfoDetailVO.setCreditTitle(InterfaceConst.CREDIT_NAME_PREFIX + transferInInfo.getCreditCode());
        transferInInfoDetailVO.setPurchaseTime(DateUtil.dateToTimestampToSec(transferInInfo.getPurchaseTime()));
        transferInInfoDetailVO.setYearYield(transferInInfo.getYearYield());
        transferInInfoDetailVO.setSurplusDays(transferInInfo.getSurplusDays()>0?transferInInfo.getSurplusDays():0);//如果过了到期时间，剩余债权天数为0
        DecimalFormat myformat=new DecimalFormat("0.00");
        transferInInfoDetailVO.setInvestAmount(myformat.format(transferInInfo.getInvestAmount()));
        
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

        if (!"F".equals(transferInInfo.getIsYq())) {
            transferInInfoDetailVO.setCreditStatus(5);
        }
        if (Status.YJQ.name().equals(transferInInfo.getBidStatus())) {
            transferInInfoDetailVO.setCreditStatus(6);
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

        //服务协议
        String fwxy = bidInfoService.getGroupItemValue(transferInInfo.getBidId(), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
        transferInInfoDetailVO.setFwxyUrl(fwxy);
        
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
    @RequestMapping(value = "transferOut/cancel", method = RequestMethod.POST)
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
     * @param curpage  当前页数
     * @return
     */
    @RequestMapping(value = "transferOut/list", method = RequestMethod.GET)
    HttpResponse applayforList(@ModelAttribute BaseRequestForm paramForm,
                               @RequestParam(required = false, value = "curpage") String curpage,
                               @RequestParam(required = false, value = "timestamp") String timestamp) throws Exception{
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        int curPage = 0;
        try{
            curPage= Integer.valueOf(curpage);
        }catch (Exception e){
            curPage=1;
        }
        int totalpage = transferInService.getTransferOutListTotalPages(1);
        List<TransferOutInfo> transferOutInfos = transferInService.getTransferOutList(curPage,null,1);

        TransferOutListVO transferOutListVO =new TransferOutListVO();
        transferOutListVO.setCurpage(curPage);
        transferOutListVO.setTotalpage(totalpage);
        List<TransferOutInfoVO> transferOutInfoVOs = new ArrayList<TransferOutInfoVO>();
        for (TransferOutInfo transferOutInfo : transferOutInfos){
            TransferOutInfoVO transferOutInfoVO = new TransferOutInfoVO();
            transferOutInfoVO.setApplyforId(transferOutInfo.getApplyforId());
            transferOutInfoVO.setTransferTitle(InterfaceConst.CREDIT_NAME_PREFIX+transferOutInfo.getCreditCode());
            transferOutInfoVO.setTransferOutValue(transferOutInfo.getTransferOutPrice());
            transferOutInfoVO.setYearYield(transferOutInfo.getYearYield());
            transferOutInfoVO.setSurplusDays(transferOutInfo.getSurplusDays()<0?0:transferOutInfo.getSurplusDays());
            transferOutInfoVO.setDiscountRate(transferOutInfo.getDiscountRate());
            transferOutInfoVO.setAssetTypes(StringUtils.isNotEmpty(transferOutInfo.getAssetTypes())?transferOutInfo.getAssetTypes().split(","):new String[0]);
            transferOutInfoVO.setTimestamp(DateUtil.dateToTimestampToSec(transferOutInfo.getTransferApplyforTime()));
            transferOutInfoVO.setRepaymentMode(transferOutInfo.getRepaymentMode());
            transferOutInfoVOs.add(transferOutInfoVO);
        }
        transferOutListVO.setTransferOutItemList(transferOutInfoVOs);
        response.setData(CommonTool.toMap(transferOutListVO));
        return response;
    }

    /**
     * 债权转让申请详情（转让中）
     * @param paramForm
     * @param applyforId  债权转让申请ID
     * @return
     */
    @RequestMapping(value = "transferOut/detail", method = RequestMethod.GET)
    HttpResponse applayforDetail(@ModelAttribute BaseRequestForm paramForm,
                                 @RequestParam(required = false, value = "applyforId") String applyforId) throws Exception{
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
        transferOutDetailVO.setApplyforId(transferOutInfo.getApplyforId());
        transferOutDetailVO.setBidId(transferOutInfo.getBidId());
        transferOutDetailVO.setYearYield(transferOutInfo.getYearYield());
        transferOutDetailVO.setCollectInterest(transferOutInfo.getCollectInterest());
        transferOutDetailVO.setAssignmentAgreementUrl(Config.get("assignment.agreement.url"));
        transferOutDetailVO.setOriginalCreditAmount(transferOutInfo.getOriginalCreditAmount());
        transferOutDetailVO.setRepaymentMode(transferOutInfo.getRepaymentMode());
        transferOutDetailVO.setTransferOutValue(transferOutInfo.getTransferOutPrice());
        transferOutDetailVO.setTransferTitle(InterfaceConst.CREDIT_NAME_PREFIX+transferOutInfo.getCreditCode());
        transferOutDetailVO.setSurplusDays(transferOutInfo.getSurplusDays()<0?0:transferOutInfo.getSurplusDays());
        transferOutDetailVO.setAssetTypes(StringUtils.isNotEmpty(transferOutInfo.getAssetTypes())?transferOutInfo.getAssetTypes().split(","):new String[0]);
        transferOutDetailVO.setRemark(transferOutInfo.getRemark());
        transferOutDetailVO.setBorrowerUrl(Config.get("bid.borrower.url")+transferOutInfo.getBidId());
        transferOutDetailVO.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(transferOutInfo.getBidId()));
        transferOutDetailVO.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(transferOutInfo.getBidId()));
        transferOutDetailVO.setGroupInfoList(bidInfoService.getGroupInfoList(transferOutInfo.getBidId()));

        //服务协议
        String fwxy = bidInfoService.getGroupItemValue(transferOutInfo.getBidId(), InterfaceConst.BID_EXTEND_ASSIGNMENT_CODE);
        transferOutDetailVO.setFwxyUrl(fwxy);

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
    @RequestMapping(value = "applyfor/info", method = RequestMethod.GET)
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

        if (Status.S.name().equals(finacing.getIsTransfer())) {//正在转让中
            response.setCodeMessage(ResponseCode.ZQZR_IS_TRANSFER.getCode(), ResponseCode.ZQZR_IS_TRANSFER.getMessage());
            return response;
        }
        /*只有一次付清还款方式支持债权转让,其他等额本息，月息到期还本不支持债权转让  update by laubrence 2016-3-25 18:21:18*/
        if (!"YCFQ".equals(finacing.getRepaymentMethod())) {
            response.setCodeMessage(ResponseCode.ZQZR_NOT_SUPPORT_BY_REPAYMENT.getCode(), ResponseCode.ZQZR_NOT_SUPPORT_BY_REPAYMENT.getMessage());
            return response;
        }
        //债权转让次数
        int assignmentCount = this.zqzrManageImpl.getCreditassignmentCount(Integer.parseInt(creditId));
        if (assignmentCount >= InterfaceConst.ZQZR_CS) {
            response.setCodeMessage(ResponseCode.ZQZR_CSGD.getCode(), ResponseCode.ZQZR_CSGD.getMessage());
            return response;
        }
        //债权持有时间超过x天才能转出 2017-1-1 之前投资的债权需要持有30天以上
        Date limitDate = DateUtil.StringToDate("2016-12-31 23:59:59","yyyy-MM-dd HH:mm:ss");
        if(limitDate.before(finacing.getCreateTime()) && DateUtil.getDayBetweenDates2(finacing.getCreateTime(), DateUtil.nowDate()) < 1){// 债权持有时间超过1天
            response.setCodeMessage(ResponseCode.ZQZR_SJWD.getCode(), ResponseCode.ZQZR_SJWD_DAY_1.getMessage());
            return response;
        }else if (limitDate.after(finacing.getCreateTime()) && DateUtil.getDayBetweenDates(finacing.getCreateTime(), DateUtil.nowDate()) < InterfaceConst.ZQZR_CY_DAY) {
            //债权持有时间超过30天
            response.setCodeMessage(ResponseCode.ZQZR_SJWD.getCode(), ResponseCode.ZQZR_SJWD.getMessage());
            return response;
        }

        //距离标的到期时间5天
        //产品要求，不需要控制最后5天不能转让  updated by zcai 20160704
//        if (DateUtil.getDayBetweenDates(DateUtil.nowDate(), finacing.getEndTimestamp()) < InterfaceConst.ZQZR_CY_LAST_DAY) {
//            response.setCodeMessage(ResponseCode.ZQZR_SJKD.getCode(), ResponseCode.ZQZR_SJKD.getMessage());
//            return response;
//        }

        TransferApplyforInfoVO transferApplyforInfoVO = new TransferApplyforInfoVO();
        transferApplyforInfoVO.setCreditId(finacing.getId());
        transferApplyforInfoVO.setCreditCapitalAmount(new BigDecimal(finacing.getOriginalMoney()));
        transferApplyforInfoVO.setAssignmentRate(String.valueOf(InterfaceConst.ASSIGNMENT_RATE));
        transferApplyforInfoVO.setAssignmentAgreement(Config.get("assignment.agreement.url"));
        transferApplyforInfoVO.setCreditassignmentCount(assignmentCount);
        transferApplyforInfoVO.setCanAssignmentCount(InterfaceConst.ZQZR_CS);

        //note:本方式为手动计算日收益，仅用于还款方式为本息到期一次付清 update by laubrence 2016-3-28 21:57:02
        //已过天数的收益（未到账）
        int passedDays = DateUtil.getDayBetweenDates(finacing.getCreateTime(),DateUtil.nowDate());//已算收益天数
        int totalDays = DateUtil.getDayBetweenDates(finacing.getCreateTime(),finacing.getEndTimestamp());//当前债权一共计息的天数
        BigDecimal passedEarning = BigDecimal.ZERO;
        if(totalDays>0){
            BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney())
                    .multiply(new BigDecimal(finacing.getRate()))
                    .multiply(new BigDecimal(finacing.getMonth()))
                    .divide(BigDecimal.valueOf(12),2,BigDecimal.ROUND_HALF_UP);
            passedEarning = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN)
                    .multiply(BigDecimal.valueOf(passedDays));
        }
        transferApplyforInfoVO.setPassedEarning(passedEarning);
        transferApplyforInfoVO.setSurplusDays(totalDays-passedDays<0?0:totalDays-passedDays);
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
    @RequestMapping(value = "transferOut/applyfor", method = RequestMethod.POST)
    HttpResponse applyforSubmit(@ModelAttribute BaseRequestForm paramForm,
                                @RequestParam(required = false, value = "token") String token,
                                @RequestParam(required = false, value = "userId") final String userId,
                                @RequestParam(required = false, value = "zqId") final String zqId,
                                @RequestParam(required = false, value = "transferValue") final String transferValue,
                                @RequestParam(required = false, value = "passedEarning") final String passedEarning,
                                @RequestParam(required = false, value = "dealPassword") String dealPassword) throws Exception {
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(dealPassword) || StringUtils.isEmpty(zqId) || StringUtils.isEmpty(transferValue)) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }

            //验证用户交易密码
            if (!iTradeService.isValidUserPwd(Integer.valueOf(userId), dealPassword)) {
                response.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
                return response;
            }

            List<Finacing> list = this.finacingService.getFinacing(userId, null, zqId);
            if (null == list || list.size() == 0) {
                response.setCodeMessage(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(), ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
                return response;
            }
            Finacing finacing = list.get(0);
            //新手标不能债权转让 update by laubrence 2015-12-20 16:57:53
            if (Status.S.name().equals(finacing.getIsNoviceBid())) {//是新手标，不能转让
                response.setCodeMessage(ResponseCode.ZQZR_IS_NOVICEBID.getCode(), ResponseCode.ZQZR_IS_NOVICEBID.getMessage());
                return response;
            }
            if (finacing.getIsTransfer().equals(Status.S.name())) {//正在转让中
                response.setCodeMessage(ResponseCode.ZQZR_IS_TRANSFER.getCode(), ResponseCode.ZQZR_IS_TRANSFER.getMessage());
                return response;
            }
			/*只有一次付清还款方式支持债权转让,其他等额本息，月息到期还本不支持债权转让  update by laubrence 2016-3-25 18:21:18*/
            if (!"YCFQ".equals(finacing.getRepaymentMethod())) {
                response.setCodeMessage(ResponseCode.ZQZR_NOT_SUPPORT_BY_REPAYMENT.getCode(), ResponseCode.ZQZR_NOT_SUPPORT_BY_REPAYMENT.getMessage());
                return response;
            }
            //债权转让次数
            int assignmentCount = this.zqzrManageImpl.getCreditassignmentCount(Integer.parseInt(zqId));
            if (assignmentCount > InterfaceConst.ZQZR_CS) {
                response.setCodeMessage(ResponseCode.ZQZR_CSGD.getCode(), ResponseCode.ZQZR_CSGD.getMessage());
                return response;
            }
            if (DateUtil.getDayBetweenDates(finacing.getCreateTime(), DateUtil.nowDate()) < InterfaceConst.ZQZR_CY_DAY) {//债权持有时间不超过5天
                response.setCodeMessage(ResponseCode.ZQZR_SJWD.getCode(), ResponseCode.ZQZR_SJWD.getMessage());
                return response;
            }
            //债权持有时间超过x天才能转出 2017-1-1 之前投资的债权需要持有30天以上
            Date limitDate = DateUtil.StringToDate("2016-12-31 23:59:59","yyyy-MM-dd HH:mm:ss");
            if(limitDate.before(finacing.getCreateTime()) && DateUtil.getDayBetweenDates2(finacing.getCreateTime(), DateUtil.nowDate()) < 1){// 债权持有时间超过1天
                response.setCodeMessage(ResponseCode.ZQZR_SJWD.getCode(), ResponseCode.ZQZR_SJWD_DAY_1.getMessage());
                return response;
            }else if (limitDate.after(finacing.getCreateTime()) && DateUtil.getDayBetweenDates(finacing.getCreateTime(), DateUtil.nowDate()) < InterfaceConst.ZQZR_CY_DAY) {
                //债权持有时间超过30天
                response.setCodeMessage(ResponseCode.ZQZR_SJWD.getCode(), ResponseCode.ZQZR_SJWD.getMessage());
                return response;
            }
            //产品要求，不需要控制最后5天不能转让  updated by zcai 20160704
//            if (DateUtil.getDayBetweenDates(DateUtil.nowDate(), finacing.getEndTimestamp()) < InterfaceConst.ZQZR_CY_LAST_DAY) {//距离标的到期时间5天
//                response.setCodeMessage(ResponseCode.ZQZR_SJKD.getCode(), ResponseCode.ZQZR_SJKD.getMessage());
//                return response;
//            }

            //note:本方式为手动计算日收益，仅用于还款方式为本息到期一次付清 update by laubrence 2016-4-1 17:30:31
            //已过天数的收益（未到账）
            int passedDays = DateUtil.getDayBetweenDates(finacing.getCreateTime(),DateUtil.nowDate());//已算收益天数
            int totalDays = DateUtil.getDayBetweenDates(finacing.getCreateTime(),finacing.getEndTimestamp());//当前债权一共计息的天数
            BigDecimal expectedProfit = BigDecimal.ZERO;
            if(totalDays>0){
                BigDecimal totalEarning = new BigDecimal(finacing.getOriginalMoney()).multiply(new BigDecimal(finacing.getRate())).multiply(new BigDecimal(finacing.getMonth())).divide(BigDecimal.valueOf(12),2,BigDecimal.ROUND_HALF_UP);
                expectedProfit = totalEarning.divide(BigDecimal.valueOf(totalDays),2,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(passedDays));
            }
            final BigDecimal finalExpectedProfit = expectedProfit;
            //债权金额
            final double bidValue = finacing.getOriginalMoney();
            //note:添加返回债权转让申请时间 	junda.feng-20160708
            Timestamp applyTime =zqzrManageImpl.transfer(new AddTransfer() {
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
                    return new BigDecimal(InterfaceConst.ASSIGNMENT_RATE);
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
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("applyTime", applyTime);
            response.setData(map);
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable ex) {
            response.setCodeMessage(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage());
            logger.error("[CreditAssignmentController.applyforSubmit]" + ex.getMessage(), ex);
        }
        return response;
    }
}
