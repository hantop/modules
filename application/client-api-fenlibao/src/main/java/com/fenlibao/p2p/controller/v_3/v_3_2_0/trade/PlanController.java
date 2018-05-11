package com.fenlibao.p2p.controller.v_3.v_3_2_0.trade;

import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.common.util.repayment.CalCapitalInterestUtil;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.finacing.PlanCreditInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacing;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.entity.trade.BidRecords;
import com.fenlibao.p2p.model.entity.trade.PlanBidProfit;
import com.fenlibao.p2p.model.entity.trade.PlanRecords;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.plan.PlanStatusEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.fiancing.InvestInfoDetailVO;
import com.fenlibao.p2p.model.vo.plan.QuitPlanInfoVO;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.IUserDmService;
import com.fenlibao.p2p.service.bid.PlanInfoService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.user.RiskTestService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StatusUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *计划控制器
 * @author chenzhiliang
 * @date 2017年1月22日
 */
@RequestMapping(value = "plan", headers = APIVersion.v_3_2_0)
@RestController("v_3_2_0/PlanController")
public class PlanController {
    private static final Logger logger = LogManager.getLogger(TradeController.class);

    @Resource
    private ITradeService tradeService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private BidInfoService bidInfoService;
    @Resource
    private PlanService planService;
    @Resource
    private RedisService redisService;
    @Resource
    private RiskTestService riskTestService;
    @Resource
    private IUserDmService userDmService;
    @Resource
    private PlanInfoService planInfoService;
    @Resource
    private FinacingService finacingService;
    /**
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "recordList", method = RequestMethod.GET)
    public HttpResponse recordList(@ModelAttribute BaseRequestForm  paramForm, Integer pageNo, Integer pageSize,
                                    String planId,String isUp,String newPlan) {
        HttpResponse response = new HttpResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        if (!paramForm.validate() || StringUtils.isBlank(planId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM
            );
            return response;
        }
        int planType ;
        if (newPlan != null && newPlan.equals("1")) {
            planType = 1;
        } else {
            planType = 0;
        }

        if(pageNo==null)pageNo=1;
        if(pageSize==null)pageSize= InterfaceConst.PAGESIZE;
        List<PlanRecords> list;
        if (0 == planType) {
            list = tradeService.getPlanRecordsList(Integer.valueOf(planId), pageNo, pageSize);
        } else {
            list = tradeService.getNewRecordsList(Integer.valueOf(planId), pageNo, pageSize);
        }

        if(list!=null&&list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                if (null == list.get(i)) {
                    continue;
                }
                String fullName = list.get(i).getFullName();
                if (StringUtils.isEmpty(fullName)) {
                    continue;
                }

                list.get(i).setInvestorName(fullName.substring(0, 1) + "**");
                list.get(i).setFullName("");
                list.get(i).setInvestTime(list.get(i).getInvestDate().getTime());
            }
        }
        /*List<TradeRecordForm> list = tradeService.getRecordList(params.getUserId(), pageNo, pagesize);*/
        data.put("items", list);
        response.setData(data);
        return response;
    }

    @RequestMapping(value = "bidRecordList", method = RequestMethod.GET)
    public HttpResponse recordList(@ModelAttribute BaseRequestForm  paramForm, String planRecordId,int newPlan) {
        HttpResponse response = new HttpResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        if (!paramForm.validate() || StringUtils.isBlank(planRecordId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        List<BidRecords> list ;
        if (0 == newPlan) {
            list = tradeService.getBidRecordsList(Integer.valueOf(planRecordId));
        }else{
            list = tradeService.getNewBidRecordsList(Integer.valueOf(planRecordId));
        }

        /*List<TradeRecordForm> list = tradeService.getRecordList(params.getUserId(), pageNo, pagesize);*/
        //data.put("list", list);
        data.put("bidNum",list==null?0:list.size());
        data.put("list",list);
        response.setData(data);
        return response;
    }

    /**
     * 用户投资计划详情
     *
     * @param paramForm	//
     * @param userId	//债权人id
     * @param planRecordId	//记录id
     * @throws Exception
     */
    @RequestMapping(value = "recordDetail", method = RequestMethod.GET)
    HttpResponse getUserFinacingDetail(@ModelAttribute BaseRequestForm paramForm,
                                       @RequestParam(required = false, value = "userId") String userId,
                                       @RequestParam(required = false, value = "planRecordId") String planRecordId,
                                       @RequestParam(required = false, value = "newPlan") String newPlan
    ) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(planRecordId)||StringUtils.isEmpty(newPlan)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        PlanFinacing infoDetail = null;
        InvestInfoDetailVO investInfoDetailVO = new InvestInfoDetailVO();
        double couponRise = 0;
        if(newPlan.equals("0")) {
             infoDetail = tradeService.getUserPlanDetail(Integer.valueOf(userId), Integer.valueOf(planRecordId));
            if(infoDetail!=null) {
                infoDetail.setInvestStatus(planStaus(infoDetail.getId()));
                List<PlanCreditInfo> planRiseList = this.tradeService.getPlanInterestRise(Integer.valueOf(planRecordId));
                if(planRiseList!=null&&planRiseList.size()>0){
                    investInfoDetailVO.setInterestRise(infoDetail.getInterest()+planRiseList.get(0).getInterestRise());
                }
            }
        }else {
             infoDetail = tradeService.getUserPlanRecord(Integer.valueOf(userId), Integer.valueOf(planRecordId));
             if(infoDetail!=null) {
                  couponRise = this.tradeService.getNewPlanCouponRise(Integer.valueOf(planRecordId));
                 investInfoDetailVO.setInterestRise(infoDetail.getInterest()+couponRise);
                 investInfoDetailVO.setPlanType(Integer.valueOf(infoDetail.getPlanType()));
            }
        }
        if (infoDetail == null) {
            return response;
        }

        if(newPlan.equals(1)&&!infoDetail.getIsNoviceBid().equals("S")){
            investInfoDetailVO.setPlanCanQuit(1);
        }
        investInfoDetailVO.setPlanId(infoDetail.getId());
        investInfoDetailVO.setPlanRecordId(infoDetail.getPlanRecordId());
        investInfoDetailVO.setPlanTitle(infoDetail.getBidTitle());
        investInfoDetailVO.setIsNoviceBid("S".equals(infoDetail.getIsNoviceBid())?1:0);
        investInfoDetailVO.setCreditId(infoDetail.getPlanRecordId());
        investInfoDetailVO.setInvestAmount(new BigDecimal(infoDetail.getInvestAmount()));



        investInfoDetailVO.setRemark(infoDetail.getRemark());
        investInfoDetailVO.setPurchaseTime(infoDetail.getPurchaseTime().getTime());
        investInfoDetailVO.setInterestPaymentType("GDR");
        investInfoDetailVO.setExpireTime(0L);
        investInfoDetailVO.setActualRepaymentDate(0L);
        investInfoDetailVO.setNextRepaymentDate(0L);
        investInfoDetailVO.setInterestTime(0L);
        if(infoDetail.getExpireTime()!=null) {
            investInfoDetailVO.setExpireTime(DateUtil.dateToTimestampToSec(infoDetail.getExpireTime()));
            investInfoDetailVO.setActualRepaymentDate(DateUtil.dateToTimestampToSec(infoDetail.getExpireTime()));
            investInfoDetailVO.setNextRepaymentDate(DateUtil.dateToTimestampToSec(infoDetail.getExpireTime()));
        }
        if(infoDetail.getNextRepayTime()!=null){
            investInfoDetailVO.setNextRepaymentDate(DateUtil.dateToTimestampToSec(infoDetail.getNextRepayTime()));
        }
        if (infoDetail.getInterestTime() != null) {
            investInfoDetailVO.setInterestTime(DateUtil.dateToTimestampToSec(infoDetail.getInterestTime()));
        }
        if (infoDetail.getActualRepaymentDate() != null) {
            investInfoDetailVO.setActualRepaymentDate(DateUtil.dateToTimestampToSec(infoDetail.getActualRepaymentDate()));
        }
        BigDecimal expectedProfit = BigDecimal.ZERO;
        if(newPlan.equals("0")) {
            BigDecimal profitPlan = BigDecimal.ZERO;
            /*infoDetail.setInvestStatus(planStaus(infoDetail.getId()));*/
           /* List<BidRecords> bidRecordsList = tradeService.getBidRecordsList(infoDetail.getId());
            if (bidRecordsList != null) {
                for (BidRecords bidRecords : bidRecordsList) {
                    expectedProfit = expectedProfit.add(bidRecords.getExpectedProfit());
                }
            }
            investInfoDetailVO.setExpectedProfit(expectedProfit);*/
            List<PlanBidProfit> profitList = this.tradeService.getPlanProfit(Integer.valueOf(planRecordId));
            if(null!=profitList&&profitList.size()>0){
                for(PlanBidProfit planBidProfit : profitList){
                    profitPlan = profitPlan.add(planBidProfit.getProfit()).add(planBidProfit.getRaiseProfit());
                }
            }
            investInfoDetailVO.setExpectedProfit(profitPlan);
            if (Status.TBZ.name().equals(infoDetail.getInvestStatus()) || Status.DFK.name().equals(infoDetail.getInvestStatus())) {
                List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(infoDetail.getId(), 1);
                List<PlanCreditInfo> planCreditInfoList = this.tradeService.getPlanInterestRise(Integer.valueOf(planRecordId));
                if (planCreditInfoList != null && planCreditInfoList.size() > 0) {
                    planBidsList.get(0).setInterestRise(planCreditInfoList.get(0).getInterestRise());
                }
                BigDecimal expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), planBidsList.get(0).getYearYield().add(new BigDecimal(planBidsList.get(0).getBidInterestRise())).add(new BigDecimal(planBidsList.get(0).getInterestRise())), infoDetail.getLoanDate());
                investInfoDetailVO.setExpectedProfit(expectedProfitPlan);
                investInfoDetailVO.setInvestStatus(1);
            }
            if (Status.HKZ.name().equals(infoDetail.getInvestStatus())) {
                List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(infoDetail.getId(), 1);
                List<PlanCreditInfo> planCreditInfoList = this.tradeService.getPlanInterestRise(Integer.valueOf(planRecordId));
                if (planCreditInfoList != null && planCreditInfoList.size() > 0) {
                    planBidsList.get(0).setInterestRise(planCreditInfoList.get(0).getInterestRise());
                }
                BigDecimal expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), planBidsList.get(0).getYearYield().add(new BigDecimal(planBidsList.get(0).getBidInterestRise())).add(new BigDecimal(planBidsList.get(0).getInterestRise())), infoDetail.getLoanDate());
                investInfoDetailVO.setExpectedProfit(expectedProfitPlan);
                investInfoDetailVO.setInvestStatus(2);
            }
            if (Status.YJQ.name().equals(infoDetail.getInvestStatus())) {
                investInfoDetailVO.setInvestStatus(6);
            }

        }else {
            int holdDays = 0;//持有天数\
            long interestTime = 0;
            if(infoDetail.getInterestTime()!=null) {
                interestTime = infoDetail.getInterestTime().getTime();//起息时间
                if (infoDetail.getOwnsStatus() == 0 || infoDetail.getOwnsStatus() == 1) {
                    holdDays = DateUtil.daysOfTwo(infoDetail.getInterestTime(), new Date());
                } else {
                    holdDays = DateUtil.daysOfTwo(infoDetail.getInterestTime(), infoDetail.getExitTime());
                }
            }
            if(!StringUtils.isEmpty(infoDetail.getExpectedProfit())) {
                investInfoDetailVO.setExpectedProfit(new BigDecimal(infoDetail.getExpectedProfit()));
            }
            infoDetail.setInvestStatus(StatusUtil.status(infoDetail.getInvestStatus()));
            if (Status.TBZ.name().equals(infoDetail.getInvestStatus()) || Status.DFK.name().equals(infoDetail.getInvestStatus())) {
               // BigDecimal expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), infoDetail.getHighRate()), infoDetail.getLoanDate());
                BigDecimal rate = new BigDecimal("0");
                BigDecimal expectedProfitPlan =  new BigDecimal("0");
                if(infoDetail.getPlanType().equals("1")){//月月
                    int month=0;
                    if(infoDetail.getInterestTime()!=null) {//开始计息
                        if (infoDetail.getOwnsStatus() == 0 || infoDetail.getOwnsStatus() == 1) {//未退出
                            month = planService.getPlanMonthNum(infoDetail.getOwnsStatus(),interestTime, 0);
                            rate = new BigDecimal(infoDetail.getHighRate()+infoDetail.getInterest()+couponRise);
                            //未退出计算预期收益
                            if(infoDetail.getLoanType().equals("d")) {
                                expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate());
                            }else {
                                expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate(), infoDetail.getRepaymentMode());
                            }
                        } else  {//已退出
                            month = planService.getPlanMonthNum(infoDetail.getOwnsStatus(), interestTime, infoDetail.getExitTime().getTime());
                            //已退出按持有天数算收益
                            rate = new BigDecimal (infoDetail.getLowRate()+infoDetail.getBonusRate()*month+infoDetail.getInterest()+couponRise);
                            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, holdDays);

                        }
                    }else {
                        if (infoDetail.getOwnsStatus() == 0 || infoDetail.getOwnsStatus() == 1) {//未退出
                            rate = new BigDecimal(infoDetail.getHighRate()+infoDetail.getInterest()+couponRise);
                            //未退出计算预期收益
                            if(infoDetail.getLoanType().equals("d")) {
                                expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate());
                            }else {
                                expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate(), infoDetail.getRepaymentMode());
                            }
                        }

                    }
                    investInfoDetailVO.setPresentRate(infoDetail.getLowRate()+infoDetail.getBonusRate()*month+couponRise);

                }else {//省心
                    rate = new BigDecimal(infoDetail.getYearYield()+infoDetail.getInterest()+couponRise);
                    if (infoDetail.getOwnsStatus() == 0 || infoDetail.getOwnsStatus() == 1) {
                        if (infoDetail.getLoanType().equals("d")) {
                            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate());
                        } else {
                            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate(), infoDetail.getRepaymentMode());
                        }
                    }else {
                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, holdDays);
                    }
                }

                investInfoDetailVO.setExpectedProfit(expectedProfitPlan);
            }
            if (Status.HKZ.name().equals(infoDetail.getInvestStatus())) {
                BigDecimal rate = new BigDecimal("0");
                BigDecimal expectedProfitPlan = new BigDecimal("0");
                if(infoDetail.getPlanType().equals("1")){
                    int month = 0;
                    if(infoDetail.getOwnsStatus()==0||infoDetail.getOwnsStatus()==1) {
                        month = planService.getPlanMonthNum(infoDetail.getOwnsStatus(),interestTime,0);
                        rate = new BigDecimal(infoDetail.getHighRate()+infoDetail.getInterest()+couponRise);
                        if(infoDetail.getLoanType().equals("d")) {
                            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate());
                        }else {
                            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate(), infoDetail.getRepaymentMode());
                        }
                    }else if(infoDetail.getOwnsStatus()==2) {//已退出
                        month = planService.getPlanMonthNum(infoDetail.getOwnsStatus(),interestTime,infoDetail.getExitTime().getTime());
                        rate = new BigDecimal(infoDetail.getLowRate()+infoDetail.getBonusRate()*month+infoDetail.getInterest()+couponRise);
                        expectedProfitPlan   = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, holdDays);
                    }
                    investInfoDetailVO.setPresentRate(infoDetail.getLowRate()+infoDetail.getBonusRate()*month);

                }else {
                    rate = new BigDecimal(infoDetail.getYearYield()+infoDetail.getInterest()+couponRise);
                    if (infoDetail.getOwnsStatus() == 0 || infoDetail.getOwnsStatus() == 1) {
                        if (infoDetail.getLoanType().equals("d")) {
                            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate());
                        } else {
                            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(new BigDecimal(infoDetail.getInvestAmount()), rate, infoDetail.getLoanDate(), infoDetail.getRepaymentMode());
                        }
                    }else {
                        expectedProfitPlan   = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), rate, holdDays);
                    }
                }

                investInfoDetailVO.setExpectedProfit(expectedProfitPlan);
                investInfoDetailVO.setInvestStatus(2);
            }
            if (Status.YJQ.name().equals(infoDetail.getInvestStatus())) {
                investInfoDetailVO.setInvestStatus(6);
            }
            investInfoDetailVO.setHoldDays(holdDays);
        }

        investInfoDetailVO.setYearYield(infoDetail.getYearYield());
        if(infoDetail.getLoanType().equals("d")){
            investInfoDetailVO.setLoanDays(infoDetail.getLoanDate());
        }else {
            investInfoDetailVO.setLoanMonths(infoDetail.getLoanDate());
        }
        if(infoDetail.getOwnsStatus()==1){
            investInfoDetailVO.setInvestStatus(3);
        }else if(infoDetail.getOwnsStatus()==2){
            investInfoDetailVO.setInvestStatus(4);
        }
        investInfoDetailVO.setApplyTime(DateUtil.dateToTimestampToSec(infoDetail.getApplyTime()));
        investInfoDetailVO.setSuccessTime(DateUtil.dateToTimestampToSec(infoDetail.getSuccessTime()));
        /*investInfoDetailVO.setActualRepaymentDate(DateUtil.dateToTimestampToSec(infoDetail.getActualRepaymentDate()));*/
        investInfoDetailVO.setInvestTime(DateUtil.dateToTimestampToSec(infoDetail.getInvestTime()));
        investInfoDetailVO.setRepaymentMode(infoDetail.getRepaymentMode());
        investInfoDetailVO.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId), infoDetail.getIsNoviceBid(),infoDetail.getPlanType()));
        //investInfoDetailVO.setNextRepaymentDate(DateUtil.dateToTimestampToSec(infoDetail.getNextRepayTime()));
        //服务协议
        //investInfoDetailVO.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId),infoDetail.getIsNoviceBid(),infoDetail.getPlanType()));
        //担保借款合同
        investInfoDetailVO.setStatus(1);
        response.setData(CommonTool.toMap(investInfoDetailVO));
        return response;
    }

    /**
     * 投资计划
     * @param paramForm
     * @param planId
     * @param sgSum
     * @param isCheck
     * @param dealPassword
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "doPlan", method = RequestMethod.POST)
    public HttpResponse doPlan( @ModelAttribute BaseRequestFormExtend paramForm, String planId, String sgSum, String isCheck, String fxhbIds, String jxqId, String dealPassword, String newPlan)  throws Throwable{
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate() || StringUtils.isBlank(planId)
                || StringUtils.isBlank(sgSum)){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }

        //红包和加息卷不能同时使用
        if(!StringUtils.isBlank(fxhbIds) && !StringUtils.isBlank(jxqId)){
            apiResponse.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            return apiResponse;
        }

        String requestCacheKey = RedisConst.$REQUEST_CACHE_KEY_USERID.concat(paramForm.getUserId().toString());
        if (redisService.existsKey(requestCacheKey)) {
            apiResponse.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT);
            return apiResponse;
        }
        try{
            //风险测试校验 Mingway 20161221
            if(!riskTestService.queryHadTestedByUid(paramForm.getUserId())){
                apiResponse.setCodeMessage(ResponseCode.USER_RISK_UNTEST);
                return apiResponse;
            }
            //未实名认证
            if(!userDmService.isSmrz(paramForm.getUserId())){
                apiResponse.setCodeMessage(ResponseCode.USER_IDENTITY_UNAUTH);
                return apiResponse;
            }

            int planIds = IntegerParser.parse(planId);   // 计划的ID
            BigDecimal amount = BigDecimalParser.parse(sgSum);  //投资金额

            if("1".equals(newPlan)){
                InvestPlan investPlan = planService.getInvestPlan(planIds);
                if(investPlan == null){
                    apiResponse.setCodeMessage(ResponseCode.BID_NOT_EXIST);
                    return apiResponse;
                }

                if(investPlan.getStatus() != PlanStatusEnum.TZZ.getCode()){
                    apiResponse.setCodeMessage(ResponseCode.BID_INVESTMENT_FAILURE);
                    return apiResponse;
                }

                planInfoService.checkCanInvest(planIds,paramForm.getUserId(),VersionTypeEnum.PT);
                //新手判断
                if (investPlan.getIsNovice() == 1) {
                    // 添加判断 非新用户不能投资新手标
                    boolean isNovice = bidInfoService.isNovice(paramForm.getUserId()); //是否是新手
                    if (!isNovice) {
                        throw new BusinessException(ResponseCode.BID_NOVICE_NOT_NEW_USER);
                    }
                    BigDecimal max = new BigDecimal(Config.get("bid.novice.invest.limit"));
                    if (amount.compareTo(max) > 0) {
                        throw new BusinessException(ResponseCode.BID_NOVICE_OVER_MAX_AMOUNT); //新手标投标金额不能大于最大限制金额
                    }
                }

                //首先验证交易密码是否正确
                if(!tradeService.isValidUserPwd(paramForm.getUserId(), dealPassword)){
                    apiResponse.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
                    return apiResponse;
                }
                planService.doInvestPlan(planIds, amount, paramForm.getUserId(), fxhbIds, jxqId, isCheck);
            }else if("0".equals(newPlan) || StringUtils.isEmpty(newPlan)){
                Plan plan = planService.getPlan(planIds);
                if(plan == null){
                    apiResponse.setCodeMessage(ResponseCode.BID_NOT_EXIST);
                    return apiResponse;
                }

                //新手判断
                if ("S".equalsIgnoreCase(plan.getIsNoviceBid())) {
                    // 添加判断 非新用户不能投资新手标
                    boolean isNovice = bidInfoService.isNovice(paramForm.getUserId()); //是否是新手
                    if (!isNovice) {
                        throw new BusinessException(ResponseCode.BID_NOVICE_NOT_NEW_USER);
                    }
                    BigDecimal max = new BigDecimal(Config.get("bid.novice.invest.limit"));
                    if (amount.compareTo(max) > 0) {
                        throw new BusinessException(ResponseCode.BID_NOVICE_OVER_MAX_AMOUNT); //新手标投标金额不能大于最大限制金额
                    }
                }

                //首先验证交易密码是否正确
                if(!tradeService.isValidUserPwd(paramForm.getUserId(), dealPassword)){
                    apiResponse.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
                    return apiResponse;
                }
                planService.doPlan(plan, amount, paramForm.getUserId(), fxhbIds, jxqId, isCheck);
            }else{
                apiResponse.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
                return apiResponse;
            }
        } catch (BusinessException busi) {
            logger.debug("投资计划失败，planId=[{}],投资金额=[{}],userId=[{}],msg=[{}]", planId, sgSum, paramForm.getUserId(),busi.getMessage());
            apiResponse.setCodeMessage(busi);
        } catch (Throwable throwable){
            logger.error("投资计划失败，planId=[{}],投资金额=[{}],userId=[{}]", planId, sgSum, paramForm.getUserId());
            logger.error("投资计划失败", throwable);
            apiResponse.setCodeMessage(ResponseCode.FAILURE);
        } finally {
            redisService.removeKey(requestCacheKey);
        }
        return apiResponse;
    }

    /**
     * 计划退出申请信息(申请时需显示的信息)
     *
     * @param paramForm
     * @param token
     * @param userId
     * @param planRecordId 计划投资记录id
     * @return notic:除非出现逾期，否则债权转让不受其他条件限制
     */
    @RequestMapping(value = "applyfor/info", method = RequestMethod.GET)
    HttpResponse applayforInfo(@ModelAttribute BaseRequestForm paramForm,
                               @RequestParam(required = false, value = "token") String token,
                               @RequestParam(required = false, value = "userId") String userId,
                               @RequestParam(required = false, value = "planRecordId") String planRecordId) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() ||  StringUtils.isEmpty(planRecordId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        PlanRecordInfo planRecordInfo = planInfoService.getPlanRecordInfo(IntegerParser.parse(planRecordId),Integer.valueOf(userId));

        if (planRecordInfo==null ) {
            response.setCodeMessage(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(), ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
            return response;
        }

        Map<String, Object> resultMap ;
        //校验规则
        resultMap = validate(planRecordInfo);
        response = (HttpResponse) resultMap.get("response");
        if (!response.getCode().equals(ResponseCode.SUCCESS.getCode())) {
            return response;
        }

        QuitPlanInfoVO quitPlanInfoVO = new QuitPlanInfoVO();
        quitPlanInfoVO.setPlanRecordId(planRecordInfo.getUserPlanId());
        quitPlanInfoVO.setCreditCapitalAmount(planRecordInfo.getInvestAmount().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());

        String assignmentRate = String.valueOf(InterfaceConst.ASSIGNMENT_RATE);
        if ( planRecordInfo.getType() == 1) {//随时退出计划和省心计划的费率不一样
            assignmentRate = String.valueOf(InterfaceConst.ASSIGNMENT_RATE_ANNYTIME_QUIT);
        }
        quitPlanInfoVO.setAssignmentRate(assignmentRate);
        quitPlanInfoVO.setAssignmentAgreement(Config.get("assignment.agreement.url"));
        int holdDays = DateUtil.daysOfTwo(planRecordInfo.getInterestTime(),new Date());
        quitPlanInfoVO.setHoldDays(holdDays);

        BigDecimal rate ;
        BigDecimal expectedProfitPlan = BigDecimal.ZERO;
        if (planRecordInfo.getType() == 2) { //省心计划用投资时的利率
            quitPlanInfoVO.setBidYield(planRecordInfo.getInvestRate()+planRecordInfo.getRaiseRate()+planRecordInfo.getCouponRise());
            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(planRecordInfo.getInvestAmount(),new BigDecimal(quitPlanInfoVO.getBidYield()),holdDays);
        } else if (planRecordInfo.getType() == 1) {

            int month = DateUtil.monthsBetweenDates(planRecordInfo.getInterestTime(),new Date());
            rate = new BigDecimal(planRecordInfo.getLowRate()+month*planRecordInfo.getMoIncreaseRate()+planRecordInfo.getCouponRise());
            quitPlanInfoVO.setBidYield(rate.doubleValue());
            expectedProfitPlan   = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(planRecordInfo.getInvestAmount(), rate, holdDays);
        }
        quitPlanInfoVO.setPassedEarning(expectedProfitPlan.doubleValue());

        response.setData(CommonTool.toMap(quitPlanInfoVO));
        return response;
    }

    /**
     * 确认计划退出
     * @param paramForm
     * @param token
     * @param userId         用户ID
     * @param planRecordId
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "transferOut/applyfor", method = RequestMethod.POST)
    HttpResponse applyforSubmit(@ModelAttribute BaseRequestForm paramForm,
                                @RequestParam(required = false, value = "token") String token,
                                @RequestParam(required = false, value = "userId") String userId,
                                @RequestParam(required = false, value = "planRecordId") String planRecordId,
                                @RequestParam(required = false, value = "dealPassword") String dealPassword) throws Exception {
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(dealPassword) || StringUtils.isEmpty(planRecordId)) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }

            //验证用户交易密码
            if (!tradeService.isValidUserPwd(Integer.valueOf(userId), dealPassword)) {
                response.setCodeMessage(ResponseCode.USER_TRADE_PASSWORD_ERROR);//交易密码错误
                return response;
            }

            PlanRecordInfo planRecordInfo = planInfoService.getPlanRecordInfo(IntegerParser.parse(planRecordId), Integer.valueOf(userId));

            if (planRecordInfo == null || planRecordInfo.getUserId() != IntegerParser.parse(userId)) {
                response.setCodeMessage(ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getCode(), ResponseCode.ZQZR_ZQ_INFO_NOT_FOUND.getMessage());
                return response;
            }
            Map<String, Object> resultMap ;
            //校验规则
            resultMap = validate(planRecordInfo);
            response = (HttpResponse) resultMap.get("response");
            if(!response.getCode().equals(ResponseCode.SUCCESS.getCode())){
                return response;
            }
            int holdDays = DateUtil.daysOfTwo(planRecordInfo.getInvestTime(),new Date());
            BigDecimal rate ;
            BigDecimal expectedProfitPlan = BigDecimal.ZERO;
            if (planRecordInfo.getType() == 2) { //省心计划用投资时的利率
                expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(planRecordInfo.getInvestAmount(),new BigDecimal(planRecordInfo.getInvestRate()),holdDays);
            } else if (planRecordInfo.getType() == 1) {
                int month = DateUtil.monthsBetweenDates(planRecordInfo.getInterestTime(),new Date());
                rate = new BigDecimal(planRecordInfo.getLowRate()+month*planRecordInfo.getMoIncreaseRate()+planRecordInfo.getCouponRise());
                expectedProfitPlan   = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(planRecordInfo.getInvestAmount(), rate, holdDays);
            }
            Map<String,Object> map=new HashMap<String,Object>();
            Timestamp applyTime = planService.quitUserPlan(planRecordInfo,expectedProfitPlan);
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

    //计划退出校验的规则
    public Map<String,Object> validate(PlanRecordInfo planRecordInfo){
        Map<String, Object> resultMap = new HashMap<>();
        HttpResponse response = new HttpResponse();
        if (1 != planRecordInfo.getStatus()||planService.checkExitingPlan(planRecordInfo.getUserPlanId())) {//正在转让中
            response.setCodeMessage(ResponseCode.ZQZR_IS_TRANSFER.getCode(), ResponseCode.ZQZR_IS_TRANSFER.getMessage());
            resultMap.put("response", response);
            return resultMap;
        }

        //新手计划不能债权转让
        if (1 == planRecordInfo.getIsNovice()) {//是新手计划，不能转让
            response.setCodeMessage(ResponseCode.TCJH_IS_NOVICEBID.getCode(), ResponseCode.TCJH_IS_NOVICEBID.getMessage());
            resultMap.put("response", response);
            return resultMap;
        }

        //计划满额
        if (planRecordInfo.getPlanStatus()!=PlanStatusEnum.HKZ.getCode()) {
            response.setCodeMessage(ResponseCode.TCJH_SJWD);
            resultMap.put("response", response);
            return resultMap;
        }

        //计划债权持有时间超过3天
        if (null==planRecordInfo.getInterestTime()||DateUtil.getDayBetweenDates(planRecordInfo.getInterestTime(), DateUtil.nowDate()) < InterfaceConst.ZQZR_CY_DAY) {
            response.setCodeMessage(ResponseCode.TCJH_SJWD);
            resultMap.put("response", response);
            return resultMap;
        }

        //下一个回款时间3天内
        if (planRecordInfo.getNextRepaymentDate() != null && DateUtil.getDayBetweenDates(DateUtil.nowDate(), planRecordInfo.getNextRepaymentDate()) < InterfaceConst.ZQZR_CY_LAST_DAY) {
            response.setCodeMessage(ResponseCode.TCJH_SJKD);
            resultMap.put("response", response);
            return resultMap;
        }

        List<UserPlanProduct> userPlanProducts = planInfoService.getUserPlanProducts(planRecordInfo.getUserPlanId());
        if (userPlanProducts == null || userPlanProducts.size() == 0) {
            resultMap.put("response", response);
            return resultMap;
        }

        for (int i = 0; i < userPlanProducts.size(); i++) {
            //还没有生成债权
            if (userPlanProducts.get(i).getProductId() == 0) {
                continue;
            }

            if (!"F".equals(userPlanProducts.get(i).getYq())) {//是否有逾期
                response.setCodeMessage(ResponseCode.TCJH_OVER_DATE);
                resultMap.put("response", response);
                return resultMap;
            }
        }
        resultMap.put("response", response);
        return resultMap;
    }

    private String planStaus(int PlanId) {
        List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(PlanId,Integer.MAX_VALUE);
        String status = "";
        int i =0;
        for(PlanBidsStatus planBidsStatus : planBidsList){
            if(planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())>=100){
                status = "TBZ";
                break;
            }else if (planBidsStatus.getStatus().equals("DFK")||(planBidsStatus.getStatus().equals("TBZ")&&Double.valueOf(planBidsStatus.getSurplusAmount())<100)){
                status = "DFK";
                if(i>0){//当有些标处于放款，有些标未放款
                    break;
                }
            }else if(planBidsStatus.getStatus().equals("HKZ")){
                status = "HKZ";
                i=i+1;
				/*break;*/
            }else if(planBidsStatus.getStatus().equals("YLB")){
                status = status;//流标的情况维持原状态不变
            }
            else {
                status = "YJQ";
            }
        }
        return status;
    }
}

