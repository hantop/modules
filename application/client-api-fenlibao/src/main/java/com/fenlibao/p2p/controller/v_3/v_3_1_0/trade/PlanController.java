package com.fenlibao.p2p.controller.v_3.v_3_1_0.trade;

import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.common.util.repayment.CalCapitalInterestUtil;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.bid.DirectionalBid;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.finacing.PlanCreditInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacing;
import com.fenlibao.p2p.model.entity.trade.BidRecords;
import com.fenlibao.p2p.model.entity.trade.PlanRecords;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.trade.DueInAmount;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.fiancing.InvestInfoDetailVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.IUserDmService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.service.user.RiskTestService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *计划控制器
 * @author chenzhiliang
 * @date 2017年1月22日
 */
@RequestMapping(value = "plan", headers = APIVersion.v_3_1_0)
@RestController("v_3_1_0/PlanController")
public class PlanController {
    private static final Logger logger = LogManager.getLogger(com.fenlibao.p2p.controller.v_3.v_3_0_0.trade.TradeController.class);

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

    /**
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "recordList", method = RequestMethod.GET)
    public HttpResponse recordList(@ModelAttribute BaseRequestForm  paramForm, Integer pageNo, Integer pageSize,
                                    String planId,String isUp) {
        HttpResponse response = new HttpResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        if (!paramForm.validate() || StringUtils.isBlank(planId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM
            );
            return response;
        }
        if(pageNo==null)pageNo=1;
        if(pageSize==null)pageSize= InterfaceConst.PAGESIZE;
        List<PlanRecords> list  =tradeService.getPlanRecordsList(Integer.valueOf(planId),pageNo,pageSize);
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
    public HttpResponse recordList(@ModelAttribute BaseRequestForm  paramForm, String planRecordId) {
        HttpResponse response = new HttpResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        if (!paramForm.validate() || StringUtils.isBlank(planRecordId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        List<BidRecords> list = tradeService.getBidRecordsList(Integer.valueOf(planRecordId));
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
                                       @RequestParam(required = false, value = "planRecordId") String planRecordId
    ) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(planRecordId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        PlanFinacing infoDetail = tradeService.getUserPlanDetail(Integer.valueOf(userId),Integer.valueOf(planRecordId));
        if (infoDetail == null) {
            return response;
        }


        infoDetail.setInvestStatus(planStaus(infoDetail.getId()));

        InvestInfoDetailVO investInfoDetailVO = new InvestInfoDetailVO();
        investInfoDetailVO.setPlanId(infoDetail.getId());
        investInfoDetailVO.setPlanRecordId(infoDetail.getPlanRecordId());
        investInfoDetailVO.setPlanTitle(infoDetail.getBidTitle());
        investInfoDetailVO.setIsNoviceBid("S".equals(infoDetail.getIsNoviceBid())?1:0);
        investInfoDetailVO.setCreditId(infoDetail.getPlanRecordId());
        investInfoDetailVO.setInvestAmount(new BigDecimal(infoDetail.getInvestAmount()));
        investInfoDetailVO.setInterestRise(infoDetail.getInterest());
        List<PlanCreditInfo> planRiseList = this.tradeService.getPlanInterestRise(Integer.valueOf(planRecordId));
        if(planRiseList!=null&&planRiseList.size()>0){
            investInfoDetailVO.setInterestRise(infoDetail.getInterest()+planRiseList.get(0).getInterestRise());
        }

        investInfoDetailVO.setRemark(infoDetail.getRemark());
        investInfoDetailVO.setPurchaseTime(infoDetail.getPurchaseTime().getTime());
        investInfoDetailVO.setInterestPaymentType("GDR");
        investInfoDetailVO.setExpireTime(infoDetail.getExpireTime().getTime());
        investInfoDetailVO.setInterestTime(infoDetail.getInterestTime().getTime());
        infoDetail.setInvestStatus(planStaus(infoDetail.getId()));
        BigDecimal expectedProfit = BigDecimal.ZERO;
        List<BidRecords> bidRecordsList =  tradeService.getBidRecordsList(infoDetail.getId());
        if(bidRecordsList!=null){
            for(BidRecords bidRecords : bidRecordsList){
                expectedProfit =expectedProfit.add(bidRecords.getExpectedProfit());
            }
        }
        investInfoDetailVO.setExpectedProfit(expectedProfit);
        if (Status.TBZ.name().equals(infoDetail.getInvestStatus()) || Status.DFK.name().equals(infoDetail.getInvestStatus())) {
            List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(infoDetail.getId(),1);
            List<PlanCreditInfo> planCreditInfoList = this.tradeService.getPlanInterestRise(Integer.valueOf(planRecordId));
               if(planCreditInfoList!=null&&planCreditInfoList.size()>0){
                   planBidsList.get(0).setInterestRise(planCreditInfoList.get(0).getInterestRise());
               }
                BigDecimal expectedProfitPlan =CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), planBidsList.get(0).getYearYield().add(new BigDecimal(planBidsList.get(0).getBidInterestRise())).add(new BigDecimal(planBidsList.get(0).getInterestRise())), infoDetail.getLoanDate());
              /*   for (PlanBidsStatus planBidsStatus : planBidsList) {
                    expectedProfitPlan = expectedProfitPlan.add(CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), planBidsStatus.getYearYield().add(new BigDecimal(planBidsStatus.getBidInterestRise())).add(new BigDecimal(planBidsStatus.getInterestRise())), infoDetail.getLoanDate()));
                     break;
                }*/
            investInfoDetailVO.setExpectedProfit(expectedProfitPlan);

            investInfoDetailVO.setInvestStatus(1);

        }
        if (Status.HKZ.name().equals(infoDetail.getInvestStatus())) {
           /* List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(infoDetail.getId());
            BigDecimal expectedProfitPlan = BigDecimal.ZERO;
            for (PlanBidsStatus planBidsStatus : planBidsList) {
                expectedProfitPlan = expectedProfitPlan.add(CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), planBidsStatus.getYearYield().add(new BigDecimal(planBidsStatus.getBidInterestRise())).add(new BigDecimal(planBidsStatus.getInterestRise())), infoDetail.getLoanDate()));
                break;
            }*/
            List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(infoDetail.getId(),1);
            List<PlanCreditInfo> planCreditInfoList = this.tradeService.getPlanInterestRise(Integer.valueOf(planRecordId));
            if(planCreditInfoList!=null&&planCreditInfoList.size()>0){
                planBidsList.get(0).setInterestRise(planCreditInfoList.get(0).getInterestRise());
            }
            BigDecimal expectedProfitPlan =CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(infoDetail.getInvestAmount()), planBidsList.get(0).getYearYield().add(new BigDecimal(planBidsList.get(0).getBidInterestRise())).add(new BigDecimal(planBidsList.get(0).getInterestRise())), infoDetail.getLoanDate());
            investInfoDetailVO.setExpectedProfit(expectedProfitPlan);
            investInfoDetailVO.setInvestStatus(2);
        }
        if (Status.YJQ.name().equals(infoDetail.getInvestStatus())) {
            investInfoDetailVO.setInvestStatus(6);
        }



        investInfoDetailVO.setYearYield(infoDetail.getYearYield());
        if(infoDetail.getLoanType().equals("d")){
            investInfoDetailVO.setLoanDays(infoDetail.getLoanDate());
        }else {
            investInfoDetailVO.setLoanMonths(infoDetail.getLoanDate());
        }

        investInfoDetailVO.setInvestTime(DateUtil.dateToTimestampToSec(infoDetail.getInvestTime()));
        investInfoDetailVO.setRepaymentMode(infoDetail.getRepaymentMode());





        //服务协议
        investInfoDetailVO.setFwxyUrl(userInfoService.getUserServicePlanAgreementUrl(Integer.valueOf(StringUtils.isBlank(userId) ? "-1" : userId),infoDetail.getIsNoviceBid(),"2"));
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
    public HttpResponse doPlan( @ModelAttribute BaseRequestFormExtend paramForm, String planId, String sgSum, String isCheck, String fxhbIds, String jxqId, String dealPassword)  throws Throwable{
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

