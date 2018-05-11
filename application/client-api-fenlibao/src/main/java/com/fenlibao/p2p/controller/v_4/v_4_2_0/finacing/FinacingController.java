package com.fenlibao.p2p.controller.v_4.v_4_2_0.finacing;

import com.fenlibao.p2p.common.util.repayment.CalCapitalInterestUtil;
import com.fenlibao.p2p.model.entity.PlanBidsStatus;
import com.fenlibao.p2p.model.entity.bid.BidExtendInfo;
import com.fenlibao.p2p.model.entity.finacing.*;
import com.fenlibao.p2p.model.entity.trade.PlanBidProfit;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.BidOriginEnum;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.fiancing.*;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StatusUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户投资相关接口
 */
@RestController("v_4_2_0/FinacingController")
@RequestMapping(value = "finacing", headers = APIVersion.v_4_2_0)
public class FinacingController {

	private static final Logger logger = LogManager.getLogger(FinacingController.class);

	@Resource
	private FinacingService finacingService;

	@Resource
	private BidInfoService bidInfoService;

	@Resource
	private ITradeService tradeService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private PlanService planService;
	/**
	 * 用户投资列表
	 *
	 * @param paramForm
	 * @param userId
	 * @param token
	 * @param timestamp
	 * @param isUp      0:获取最新数据  , 1: 查询历史记录
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public HttpResponse getUserInvestRecordList(@ModelAttribute BaseRequestForm paramForm,
                                                String userId, String token, String timestamp, String isUp,
                                                String status, VersionTypeEnum versionType ) throws Exception {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || versionType == null ) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		List<InvestInfoVO> investInfoVOList = new ArrayList<>();
		if (StringUtils.isEmpty(isUp)) {
			isUp = "0";
		}
		//用户的老计划和标
		List<InvestInfo> finacingList = tradeService.getUserProjectList(
				Integer.valueOf(userId), Integer.parseInt(isUp), timestamp, status,versionType.getIndex());
		if(finacingList!=null&&finacingList.size()>0){

			for (InvestInfo investInfo : finacingList) {
				InvestInfoVO investInfoVO = new InvestInfoVO();
				investInfoVO.setRepaymentMode(investInfo.getRepaymentMode());
				investInfoVO.setStatus(Integer.valueOf(status));//判断排序规则
				if (investInfo.getItemType()==1) {
					investInfoVO.setBidId(investInfo.getBidId());
					investInfoVO.setBidTitle(investInfo.getBidTitle());
					investInfoVO.setPlanType(2);//省心计划
					investInfoVO.setInvestAmount(investInfo.getInvestAmount());
					investInfoVO.setYearYield(investInfo.getYearYield());
					investInfoVO.setCreditId(investInfo.getCreditId());
					investInfoVO.setBidInterestRise(investInfo.getBidInterestRise());

					investInfoVO.setItemType(investInfo.getItemType());

					investInfoVO.setPurchaseTime(investInfo.getPurchaseTime().getTime());
					investInfoVO.setLoanDays(investInfo.getLoanDays());
					investInfoVO.setLoanMonths(investInfo.getLoanMonths());
					investInfoVO.setNextRepaymentDate(investInfo.getNextRepaymentDate()==null?null:investInfo.getNextRepaymentDate().getTime() / 1000);
					investInfoVO.setActualRepaymentDate(investInfoVO.getNextRepaymentDate());
					investInfoVO.setNewPlan(investInfo.getNewPlan());
					if (investInfo.getNewPlan() == 0) {//3.2之前的计划
						investInfoVO.setPlanCanQuit(0);
						BigDecimal profitPlan = BigDecimal.ZERO;
						//计划已获收益
						List<PlanBidProfit> profitList = this.tradeService.getPlanProfit(investInfo.getCreditId());
						List<PlanCreditInfo> planCreditInfoList = this.tradeService.getPlanInterestRise(Integer.valueOf(investInfo.getCreditId()));
						if(planCreditInfoList!=null&&planCreditInfoList.size()>0){
							investInfoVO.setInterestRise(planCreditInfoList.get(0).getInterestRise());
						}

						if(null!=profitList&&profitList.size()>0){
							for(PlanBidProfit planBidProfit : profitList){
								profitPlan = profitPlan.add(planBidProfit.getProfit()).add(planBidProfit.getRaiseProfit());
							}
						}
						//计划状态
						investInfoVO.setExpectedProfit(profitPlan);
						investInfoVO.setExpectedProfitString(profitPlan.toString());
						investInfo.setBidStatus(planStaus(investInfo.getBidId()));
						investInfoVO.setIsNoviceBid(investInfo.getIsNoviceBid().equals('S')?1:0);
						if (Status.TBZ.name().equals(investInfo.getBidStatus()) || Status.DFK.name().equals(investInfo.getBidStatus())) {
							investInfoVO.setInvestStatus(1);
							List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(investInfo.getBidId(),1);
							if(planCreditInfoList!=null&&planCreditInfoList.size()>0){
								planBidsList.get(0).setInterestRise(planCreditInfoList.get(0).getInterestRise());
							}
							BigDecimal expectedProfitPlan =CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), planBidsList.get(0).getYearYield().add(new BigDecimal(planBidsList.get(0).getBidInterestRise())).add(new BigDecimal(planBidsList.get(0).getInterestRise())), investInfo.getLoanDays());
							investInfoVO.setExpectedProfit(expectedProfitPlan);
							investInfoVO.setExpectedProfitString(expectedProfitPlan.toString());
						}
						if (Status.HKZ.name().equals(investInfo.getBidStatus())) {
							List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(investInfo.getBidId(),1);
							if(planCreditInfoList!=null&&planCreditInfoList.size()>0){
								planBidsList.get(0).setInterestRise(planCreditInfoList.get(0).getInterestRise());
							}
							BigDecimal expectedProfitPlan =CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), planBidsList.get(0).getYearYield().add(new BigDecimal(planBidsList.get(0).getBidInterestRise())).add(new BigDecimal(planBidsList.get(0).getInterestRise())), investInfo.getLoanDays());
							investInfoVO.setExpectedProfit(expectedProfitPlan);
							investInfoVO.setExpectedProfitString(expectedProfitPlan.toString());
							investInfoVO.setInvestStatus(2);
						}
						if (Status.YJQ.name().equals(investInfo.getBidStatus())) {
							investInfoVO.setInvestStatus(6);
						}

						investInfoVO.setIsNoviceBid(investInfo.getIsNoviceBid().equals("S")?1:0);
						List<PlanCreditInfo> planRiseList = this.tradeService.getPlanInterestRise(investInfo.getCreditId());
						if(planRiseList!=null&&planRiseList.size()>0){
							investInfo.setInterestRise(investInfo.getInterestRise()+planRiseList.get(0).getInterestRise());
						}
						// 改为子查询
						Map investmentExt =tradeService.getOldPlanExt(investInfo.getCreditId());
						if(investmentExt!=null){
							Date actualRepaymentDate= investmentExt.get("actualRepaymentDate")==null?null: (Date) investmentExt.get("actualRepaymentDate");
							investInfoVO.setActualRepaymentDate(actualRepaymentDate==null?null:actualRepaymentDate.getTime() / 1000);
						}
					}
					investInfoVOList.add(investInfoVO);
				}else{
					investInfoVO.setExpectedProfit(investInfo.getExpectedProfit()==null?null:investInfo.getExpectedProfit().add(investInfo.getRaiseProfit()));
					if (Status.TBZ.name().equals(investInfo.getBidStatus()) || Status.DFK.name().equals(investInfo.getBidStatus())) {
						investInfoVO.setInvestStatus(1);
						//当未放款时，手动去计算此投资的预期收益
						BigDecimal YearYield=new BigDecimal(investInfo.getYearYield())
								.add(new BigDecimal(investInfo.getInterestRise())).add(new BigDecimal(investInfo.getBidInterestRise()));
						if (investInfo.getLoanMonths() == 0 && investInfo.getLoanDays() > 0) {
							BigDecimal earnings = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), YearYield, investInfo.getLoanDays());
							investInfoVO.setExpectedProfit(earnings);
						}
						if (investInfo.getLoanMonths() > 0 && investInfo.getLoanDays() == 0) {
							BigDecimal earnings = CalCapitalInterestUtil.calTotalInterest(investInfo.getInvestAmount(), YearYield, investInfo.getLoanMonths(), investInfo.getRepaymentMode());
							investInfoVO.setExpectedProfit(earnings);
						}
					}
					if (Status.HKZ.name().equals(investInfo.getBidStatus())) {
						investInfoVO.setInvestStatus(2);
					}
					if ("S".equals(investInfo.getIsTransfer())) {
						investInfoVO.setInvestStatus(3);
					}
					if (!"F".equals(investInfo.getIsYq())) {
						investInfoVO.setInvestStatus(5);
					}
					if (Status.YJQ.name().equals(investInfo.getBidStatus())) {
						investInfoVO.setInvestStatus(6);
					}
					if (investInfo.getTransferOrderId() > 0) {
						investInfoVO.setExpectedProfit(tradeService.getArrivalEarningsByCreditId(investInfo.getCreditId()));
						investInfoVO.setInvestStatus(4);
					}

					investInfoVO.setPlanCanQuit(1);
					if(investInfo.getIsNoviceBid().equals("S")){
						investInfoVO.setPlanCanQuit(0);
					}
					investInfoVO.setBidId(investInfo.getBidId());
					investInfoVO.setBidTitle(investInfo.getBidTitle());
					investInfoVO.setIsNoviceBid("S".equals(investInfo.getIsNoviceBid())?1:0);
					investInfoVO.setCreditId(investInfo.getCreditId());
					investInfoVO.setInvestAmount(investInfo.getInvestAmount());
					investInfoVO.setYearYield(investInfo.getYearYield());
					// 改为子查询
					Map investmentExt =tradeService.getInvestmentExt(investInfo.getCreditId());
					if(investmentExt!=null){
						Date actualRepaymentDate= investmentExt.get("actualRepaymentDate")==null?null: (Date) investmentExt.get("actualRepaymentDate");
						Date applyTime=investmentExt.get("applyTime")==null?null: (Date) investmentExt.get("applyTime");
						investInfoVO.setActualRepaymentDate(actualRepaymentDate==null?null:actualRepaymentDate.getTime() / 1000);
						investInfoVO.setApplyTime(applyTime==null?null:applyTime.getTime() / 1000);
					}
					if (investInfo.getRepaymentMode().equals("YCFQ")) {
						investInfoVO.setActualRepaymentDate(investInfo.getNextRepaymentDate()==null?null:investInfo.getNextRepaymentDate().getTime() / 1000);
					}
					investInfoVO.setNextRepaymentDate(investInfo.getNextRepaymentDate()==null?null:investInfo.getNextRepaymentDate().getTime() / 1000);//下次还款日期    2016-06-28 junda.feng
					investInfoVO.setSuccessTime(investInfo.getSuccessTime()==null?null:investInfo.getSuccessTime().getTime() / 1000);
					investInfoVO.setLoanDays(investInfo.getLoanDays());
					investInfoVO.setLoanMonths(investInfo.getLoanMonths());
					//标的状态

					investInfoVO.setInterestRise(investInfo.getInterestRise());
					investInfoVO.setBidInterestRise(investInfo.getBidInterestRise());
					investInfoVO.setItemType(investInfo.getItemType());
					investInfoVO.setPurchaseTime(investInfo.getPurchaseTime().getTime());

					investInfoVOList.add(investInfoVO);
				}
				}
		}

		List<PlanFinacingInfo> planList = planService.getPlanFinacing(
				Integer.valueOf(userId), Integer.parseInt(isUp), timestamp, status,versionType.getIndex());

		if(planList!=null&&planList.size()>0){
			for (PlanFinacingInfo investInfo : planList) {
				InvestInfoVO investInfoVO = new InvestInfoVO();
				investInfoVO.setRepaymentMode(investInfo.getRepaymentMode());
				investInfoVO.setStatus(Integer.valueOf(status));//判断排序规则
					investInfoVO.setBidId(investInfo.getBidId());
					investInfoVO.setBidTitle(investInfo.getBidTitle());
					investInfoVO.setPlanType(investInfo.getType());
					investInfoVO.setInvestAmount(investInfo.getInvestAmount());
					investInfoVO.setYearYield(investInfo.getYearYield());
					investInfoVO.setCreditId(investInfo.getCreditId());
					investInfoVO.setBidInterestRise(investInfo.getBidInterestRise());

					investInfoVO.setItemType(investInfo.getItemType());

					investInfoVO.setPurchaseTime(investInfo.getPurchaseTime().getTime());
					investInfoVO.setLoanDays(investInfo.getLoanDays());
					investInfoVO.setLoanMonths(investInfo.getLoanMonths());

					investInfoVO.setNewPlan(investInfo.getNewPlan());

					int holdDays = 0;//持有天数
					int month = 0;//用于计算月利率的月份
					if(investInfo.getHoldStatus()==0||investInfo.getHoldStatus()==1){
						holdDays = investInfo.getInterestTime()==null?0: DateUtil.daysOfTwo(investInfo.getInterestTime(),new Date());
						month = investInfo.getInterestTime()==null?0:planService.getPlanMonthNum(investInfo.getHoldStatus(), investInfo.getInterestTime().getTime() , 0);
					}else{
						holdDays = investInfo.getInterestTime()==null?0: DateUtil.daysOfTwo(investInfo.getInterestTime(),investInfo.getExitTime());
						month = investInfo.getInterestTime()==null?0:planService.getPlanMonthNum(investInfo.getHoldStatus(),investInfo.getInterestTime().getTime(),investInfo.getExitTime().getTime());
					}
				    double couponRise = investInfo.getInterestRise();
				    if(investInfo.getType()==1){//月升计划利率
						investInfoVO.setYearYield(investInfo.getLowRate()+investInfo.getBonusRate()*month+investInfo.getInterestRise()+couponRise);
					}
					BigDecimal rate = BigDecimal.ZERO ;
					BigDecimal expectedProfitPlan = BigDecimal.ZERO;
					investInfoVO.setIsNoviceBid(investInfo.getIsNoviceBid().equals("1")?1:0);
					// 转换类型
					investInfo.setBidStatus(StatusUtil.status(investInfo.getPlanStatus()));

					investInfoVO.setPlanCanQuit(0);
					if(!investInfo.getIsNoviceBid().equals("1")){
					  investInfoVO.setPlanCanQuit(1);
					}


					investInfoVO.setInterestRise(couponRise);
					investInfoVO.setExpectedProfit(investInfo.getExpectedProfit());
					if (investInfo.getNextRepaymentDate() == null) {
						investInfoVO.setNextRepaymentDate(null);
					}else {
						investInfoVO.setNextRepaymentDate(investInfo.getNextRepaymentDate().getTime()/1000);
						investInfoVO.setActualRepaymentDate(investInfo.getNextRepaymentDate().getTime()/1000);
					}
						if (Status.TBZ.name().equals(investInfo.getBidStatus()) || Status.DFK.name().equals(investInfo.getBidStatus())) {

							if(investInfo.getType()==1){//月升

									rate = new BigDecimal (investInfo.getHighRate()+investInfo.getInterestRise()+couponRise);
									if(investInfo.getHoldStatus()==0||investInfo.getHoldStatus()==1){
										if (investInfo.getLoanDays() != 0) {
											expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, investInfo.getLoanDays());
										} else {
											expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(investInfo.getInvestAmount(), rate, investInfo.getLoanMonths(),investInfo.getRepaymentMode());
										}
									}else{
										//已退出按持有天数算收益
										rate = new BigDecimal (investInfo.getLowRate()+investInfo.getBonusRate()*month+investInfo.getInterestRise()+couponRise);
										expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, holdDays);
                                    }

								investInfoVO.setPresentRate(investInfo.getLowRate()+investInfo.getBonusRate()*month);

							}else {//省心
								rate = new BigDecimal(investInfo.getYearYield()+investInfo.getBidInterestRise()+couponRise);
                                if (investInfo.getHoldStatus()==0||investInfo.getHoldStatus()==1) {
                                    if (investInfo.getLoanDays() != 0) {
                                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, investInfo.getLoanDays());
                                    } else {
                                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(investInfo.getInvestAmount(), rate, investInfo.getLoanMonths(),investInfo.getRepaymentMode());
                                    }
                                }else{
                                    expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, holdDays);
                                }
							}

							investInfoVO.setExpectedProfit(expectedProfitPlan);
							investInfoVO.setInvestStatus(1);
						}
						if (Status.HKZ.name().equals(investInfo.getBidStatus())) {
							if(investInfo.getType()==1){//月月升
								rate = new BigDecimal (investInfo.getHighRate()+couponRise);
								if(investInfo.getHoldStatus()==0||investInfo.getHoldStatus()==1) {
                                    if (investInfo.getLoanDays() != 0) {
                                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, investInfo.getLoanDays());
                                    } else {
                                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(investInfo.getInvestAmount(), rate, investInfo.getLoanMonths(),investInfo.getRepaymentMode());
                                    }
								}else {//已退出
									rate = new BigDecimal(investInfo.getLowRate()+investInfo.getBonusRate()*month+investInfo.getBidInterestRise()+couponRise);
									expectedProfitPlan   = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, holdDays);
								}
								investInfoVO.setPresentRate(investInfo.getLowRate()+investInfo.getBonusRate()*month);

							}else {//省心
								rate = new BigDecimal(investInfo.getYearYield()+investInfo.getBidInterestRise()+couponRise);
                                if (investInfo.getHoldStatus()==0||investInfo.getHoldStatus()==1) {
                                    if (investInfo.getLoanDays() != 0) {
                                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, investInfo.getLoanDays());
                                    } else {
                                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(investInfo.getInvestAmount(), rate, investInfo.getLoanMonths(),investInfo.getRepaymentMode());
                                    }
                                }else {
                                    expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(investInfo.getInvestAmount(), rate, holdDays);
                                }
							}

							investInfoVO.setExpectedProfit(expectedProfitPlan);
							investInfoVO.setInvestStatus(2);
						}
						if ("1".equals(investInfo.getIsTransfer())) {
							investInfoVO.setInvestStatus(3);
						}else if("2".equals(investInfo.getIsTransfer())){
							investInfoVO.setInvestStatus(4);
						}
						if (!"F".equals(investInfo.getIsYq())) {
							investInfoVO.setInvestStatus(5);
						}
						if (Status.YJQ.name().equals(investInfo.getBidStatus())) {
							investInfoVO.setPresentRate(investInfo.getLowRate()+investInfo.getBonusRate()*month);
							investInfoVO.setInvestStatus(6);
						}

						investInfoVO.setApplyTime(investInfo.getApplyTime()==null?null:investInfo.getApplyTime().getTime()/1000);

					investInfoVOList.add(investInfoVO);
				}

		}
		List<InvestInfoVO> list = new ArrayList<>(10);
		if (investInfoVOList != null&&investInfoVOList.size()>0) {
			Collections.sort(investInfoVOList);
			list = investInfoVOList.subList(0, investInfoVOList.size()>=10?10:investInfoVOList.size());
		}

		response.getData().put("investInfoList", list);
		return response;
	}

	/**
	 * 计划回款详情
	 *
	 * @param paramForm
	 * @throws Exception
	 */
	@RequestMapping(value = "repayment/detail", method = RequestMethod.GET)
    HttpResponse getPlanRepaymentDetail(@ModelAttribute BaseRequestForm paramForm,
                                        String userId, String planRecordId) throws Exception {
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(planRecordId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		List<PlanRepaymentDetail> list = finacingService.getPlanRepaymentDetail(Integer.parseInt(planRecordId));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("items", list);
		response.setData(map);
		return  response;
	}

	/**
	 * 用户投资债权详情
	 *
	 * @param paramForm	//
	 * @param userId	//债权人id
	 * @param creditId	//债权id
	 * @throws Exception
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
    HttpResponse getUserFinacingDetail(@ModelAttribute BaseRequestForm paramForm,
                                       @RequestParam(required = false, value = "userId") String userId,
                                       @RequestParam(required = false, value = "creditId") String creditId
	) throws Exception {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(creditId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		InvestInfoDetail infoDetail = finacingService.getUserFinacingDetail(userId, creditId);
		if (infoDetail == null) {
			return response;
		}

		InvestInfoDetailVO investInfoDetailVO = new InvestInfoDetailVO();
		investInfoDetailVO.setBidId(infoDetail.getBidId());
		investInfoDetailVO.setBidTitle(infoDetail.getBidTitle());
		investInfoDetailVO.setIsNoviceBid("S".equals(infoDetail.getIsNoviceBid())?1:0);
		investInfoDetailVO.setCreditId(infoDetail.getCreditId());
		investInfoDetailVO.setInvestAmount(infoDetail.getInvestAmount());
		investInfoDetailVO.setExpectedProfit(infoDetail.getExpectedProfit());
		investInfoDetailVO.setPurchaseTime(DateUtil.dateToTimestampToSec(infoDetail.getPurchaseTime()));
		investInfoDetailVO.setLoanMonths(infoDetail.getLoanMonths());
		investInfoDetailVO.setLoanDays(infoDetail.getLoanDays());
		investInfoDetailVO.setYearYield(infoDetail.getYearYield());
		investInfoDetailVO.setInvestTime(DateUtil.dateToTimestampToSec(infoDetail.getInvestTime()));

		investInfoDetailVO.setInterestTime(DateUtil.dateToTimestampToSec(infoDetail.getInterestTime()));
		investInfoDetailVO.setExpireTime(DateUtil.dateToTimestampToSec(infoDetail.getExpireTime()));
		
		if(infoDetail.getInterestTime() == null || infoDetail.getExpireTime() == null){
			CreditInfo creditInfo = finacingService.getUserCreditInfo(Integer.valueOf(creditId));
			if(creditInfo==null){
				return response;
			}
			int bidId = creditInfo.getBidId();
			BidExtendInfo bidExtendInfo = bidInfoService.getBidExtendInfo(bidId);
			if(bidExtendInfo==null){
				return response;
			}
			investInfoDetailVO.setInterestTime(DateUtil.dateToTimestampToSec(creditInfo.getInterestDate()));
			investInfoDetailVO.setExpireTime(DateUtil.dateToTimestampToSec(bidExtendInfo.getNextRepaymentDate()));
		}

		investInfoDetailVO.setLawFiles(bidInfoService.getBidPublicAccessoryFiles(infoDetail.getBidId()));
		investInfoDetailVO.setLawFileUrl(bidInfoService.getBidPublicAccessoryFileUrl(infoDetail.getBidId()));
		investInfoDetailVO.setRemark(infoDetail.getRemark());
		//investInfoDetailVO.setGroupInfoList(bidInfoService.getGroupInfoList(infoDetail.getBidId()));
		investInfoDetailVO.setBorrowerUrl(Config.get("bid.borrower.url") + infoDetail.getBidId());
		investInfoDetailVO.setPlanCanQuit(1);
		if(infoDetail.getIsNoviceBid().equals("S")){
			investInfoDetailVO.setPlanCanQuit(0);
		}
		//标的状态
		if (Status.TBZ.name().equals(infoDetail.getBidStatus()) || Status.DFK.name().equals(infoDetail.getBidStatus())) {
			investInfoDetailVO.setInvestStatus(1);
		}
		if (Status.HKZ.name().equals(infoDetail.getBidStatus())) {
			investInfoDetailVO.setInvestStatus(2);
		}
		
		if ("S".equals(infoDetail.getIsTransfer())) {
			investInfoDetailVO.setInvestStatus(3);
		}
		if (!"F".equals(infoDetail.getIsYq())) {
			investInfoDetailVO.setInvestStatus(5);
		}
		if (Status.YJQ.name().equals(infoDetail.getBidStatus())) {
			investInfoDetailVO.setInvestStatus(6);
		}
		if (infoDetail.getTransferOrderId() > 0) {
			investInfoDetailVO.setExpectedProfit(tradeService.getArrivalEarningsByCreditId(infoDetail.getCreditId()));
			investInfoDetailVO.setInvestStatus(4);
		}
		
		 //2016-06-30 junda.feng
//		investInfoDetailVO.setInterestPaymentType(infoDetail.getInterestPaymentType());
		investInfoDetailVO.setInterestPaymentType("GDR");//按产品要求修改成GDR
		investInfoDetailVO.setRepaymentMode(infoDetail.getRepaymentMode());
		investInfoDetailVO.setNextRepaymentDate(infoDetail.getNextRepaymentDate()==null?null:infoDetail.getNextRepaymentDate().getTime()/1000);
		investInfoDetailVO.setApplyTime(infoDetail.getApplyTime()==null?null:infoDetail.getApplyTime().getTime()/1000);
		investInfoDetailVO.setSuccessTime(infoDetail.getSuccessTime()==null?null:infoDetail.getSuccessTime().getTime()/1000);
		investInfoDetailVO.setActualRepaymentDate(infoDetail.getActualRepaymentDate()==null?null:infoDetail.getActualRepaymentDate().getTime()/1000);
		if(infoDetail.getRepaymentMode().equals("YCFQ")){
			investInfoDetailVO.setActualRepaymentDate(infoDetail.getNextRepaymentDate()==null?null:infoDetail.getNextRepaymentDate().getTime()/1000);
		}
		//加息
		investInfoDetailVO.setInterestRise(infoDetail.getInterestRise());
		investInfoDetailVO.setInterestRiseAmount(infoDetail.getInterestRiseAmount().toPlainString());

		investInfoDetailVO.setInterestRise(infoDetail.getInterestRise());
		investInfoDetailVO.setBidInterestRise(infoDetail.getBidInterestRise());
		investInfoDetailVO.setAnytimeQuit(infoDetail.getAnytimeQuit());

		//服务协议
		investInfoDetailVO.setFwxyUrl(userInfoService.getUserServiceAgreementUrl(Integer.valueOf(userId)));
		//担保借款合同
		if(BidOriginEnum.qqm.getCode().equals(infoDetail.getBidOrigin())){
			investInfoDetailVO.setGuaranteeFileUrl(Config.get("bid.guaranteeFile.url")+infoDetail.getBidId());
		}
		response.setData(CommonTool.toMap(investInfoDetailVO));
		return response;
	}

	/**
	 * 用户投资债权的还款计划
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param creditId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "repayment/list", method = RequestMethod.GET)
    HttpResponse getUserFinacingRepaymentList(@ModelAttribute BaseRequestForm paramForm,
                                              @RequestParam(required = true, value = "token") String token,
                                              @RequestParam(required = true, value = "userId") String userId,
                                              @RequestParam(required = true, value = "creditId") String creditId) throws Exception {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(creditId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		CreditInfo creditInfo = finacingService.getUserCreditInfo( Integer.valueOf(creditId));
		if(creditInfo==null){
			return response;
		}
		int bidId = creditInfo.getBidId();
		BigDecimal collectInterest = finacingService.getUserCollectInterest(Integer.valueOf(userId), Integer.valueOf(creditId));
		BidExtendInfo bidAllInfo = bidInfoService.getBidAllInfo(bidId);
		if(bidAllInfo==null){
			return response;
		}
		RepaymentInfoVO repaymentInfoVO = new RepaymentInfoVO();
		repaymentInfoVO.setCollectInterest(collectInterest.toString());
		repaymentInfoVO.setLeftPeriod(bidAllInfo.getLeftRepaymentPeriod());
		//如果债权转让了 剩余期数改为0
		if(creditInfo.getHoldAmount().compareTo(BigDecimal.ZERO)==0){
			repaymentInfoVO.setLeftPeriod(0);
		}

		repaymentInfoVO.setRepaymentMode(bidAllInfo.getRepaymentMode());
		repaymentInfoVO.setNextRepaymentDate(DateUtil.dateToTimestampToSec(bidAllInfo.getNextRepaymentDate()));
		repaymentInfoVO.setExpireRepaymentDate(DateUtil.dateToTimestampToSec(bidAllInfo.getExpireDate()));
		repaymentInfoVO.setTotalRepaymentStatus(bidAllInfo.getBidStatus());
		//兼容数据 如果到期时间字段为空则到期时间为下次还款日
		//create by laubrence 2016-3-29 15:44:36
		if(bidAllInfo.getExpireDate() == null){
			repaymentInfoVO.setExpireRepaymentDate(DateUtil.dateToTimestampToSec(bidAllInfo.getNextRepaymentDate()));
		}

		List<RepaymentItemVO> repaymentItemVOs = new ArrayList<RepaymentItemVO>();
		List<RepaymentInfo> repaymentInfos = finacingService.getUserRepaymentItem(Integer.valueOf(userId), Integer.valueOf(creditId));

		//合并 2016-09-23
		for(int i=0; i<repaymentInfos.size(); i++){
			RepaymentInfo repaymentInfo = repaymentInfos.get(i);
			if(repaymentInfo.getTradeType() == 7004 || repaymentInfo.getTradeType() == 7005){
				//将7004和7005的金额合并
				repaymentInfo.setTradeTypeName("其他");
				for(int j=i+1; j<repaymentInfos.size(); j++){
					RepaymentInfo riftemp = repaymentInfos.get(j);
					boolean tradeTypeFlag = (riftemp.getTradeType() == 7004 || riftemp.getTradeType() == 7005);
					boolean periodFlag = (repaymentInfo.getPeriod() == riftemp.getPeriod());
					if(tradeTypeFlag && periodFlag){
						long timeInte = repaymentInfo.getExpectedRepaymentDate().getTime() - riftemp.getExpectedRepaymentDate().getTime();
						if(timeInte>0){
							repaymentInfo.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));//合并金额
							repaymentInfo.setRepaymentStatus(checkRepaymentStatus(repaymentInfo.getRepaymentStatus(),riftemp.getRepaymentStatus()));//合并还款状态
							repaymentInfos.remove(j);
							j--;
						}else{
							riftemp.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							riftemp.setRepaymentStatus(checkRepaymentStatus(riftemp.getRepaymentStatus(),repaymentInfo.getRepaymentStatus()));
							riftemp.setTradeTypeName("其他");
							repaymentInfos.remove(i);
							i--;
							break;
						}
					}
				}
			}
			//先将7002和7023合并
			if(repaymentInfo.getTradeType() == 7002 || repaymentInfo.getTradeType() == 7023){
				for(int j=i+1; j<repaymentInfos.size(); j++){
					RepaymentInfo riftemp = repaymentInfos.get(j);
					boolean tradeTypeFlag = (riftemp.getTradeType() == 7002 || riftemp.getTradeType() == 7023);
					boolean periodFlag = (repaymentInfo.getPeriod() == riftemp.getPeriod());
					if(tradeTypeFlag && periodFlag){
						if(repaymentInfo.getTradeType() == 7002){
							repaymentInfo.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							repaymentInfo.setRepaymentStatus(checkRepaymentStatus(repaymentInfo.getRepaymentStatus(),riftemp.getRepaymentStatus()));
							repaymentInfos.remove(j);
							j--;
						}
						if(repaymentInfo.getTradeType() == 7023){
							riftemp.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							riftemp.setRepaymentStatus(checkRepaymentStatus(riftemp.getRepaymentStatus(),repaymentInfo.getRepaymentStatus()));
							repaymentInfos.remove(i);
							i--;
							break;
						}
					}
				}
			}

			//将7002和7022合并
			if(repaymentInfo.getTradeType() == 7002 || repaymentInfo.getTradeType() == 7022){
				for(int j=i+1; j<repaymentInfos.size(); j++){
					RepaymentInfo riftemp = repaymentInfos.get(j);
					boolean tradeTypeFlag = (riftemp.getTradeType() == 7002 || riftemp.getTradeType() == 7022);
					boolean periodFlag = (repaymentInfo.getPeriod() == riftemp.getPeriod());
					if(tradeTypeFlag && periodFlag){
						if(repaymentInfo.getTradeType() == 7002){
							repaymentInfo.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							repaymentInfo.setRepaymentStatus(checkRepaymentStatus(repaymentInfo.getRepaymentStatus(),riftemp.getRepaymentStatus()));
							repaymentInfos.remove(j);
							j--;
						}
						if(repaymentInfo.getTradeType() == 7022){
							riftemp.setRepaymentAmount(repaymentInfo.getRepaymentAmount().add(riftemp.getRepaymentAmount()));
							riftemp.setRepaymentStatus(checkRepaymentStatus(riftemp.getRepaymentStatus(),repaymentInfo.getRepaymentStatus()));
							repaymentInfos.remove(i);
							i--;
							break;
						}
					}
				}
			}
		}

		for(RepaymentInfo repaymentInfo : repaymentInfos){
			RepaymentItemVO repaymentItemVO = new RepaymentItemVO();
			repaymentItemVO.setRepaymentAmount(repaymentInfo.getRepaymentAmount().toString());
			repaymentItemVO.setTradeType(repaymentInfo.getTradeType());
			repaymentItemVO.setRepaymentDate(DateUtil.dateToTimestampToSec(repaymentInfo.getExpectedRepaymentDate()));
			repaymentItemVO.setRepaymentStatus(repaymentInfo.getRepaymentStatus());
			repaymentItemVO.setTradeTypeName(repaymentInfo.getTradeTypeName());
			repaymentItemVOs.add(repaymentItemVO);
		}
		repaymentInfoVO.setRepaymentItemList(repaymentItemVOs);
		response.setData(CommonTool.toMap(repaymentInfoVO));
		return response;
	}


	private String checkRepaymentStatus(String a, String b){
		try {
			String[] tagArr = {"WH","HKZ","DF","TQH","YH"};
			List<String> tag = Arrays.asList(tagArr);
			int indexA = tag.indexOf(a);
			int indexB = tag.indexOf(b);
			int index = indexA > indexB ? indexB : indexA;
			return tag.get(index);
		}catch (Exception e){
			if(StringUtils.isNotBlank(a)){
				return a;
			}
			if(StringUtils.isNotBlank(b)){
				return b;
			}
			return "";
		}
	}

	/**
	 * 用户所有投资的还款计划
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "repayment/allList", method = RequestMethod.GET)
    HttpResponse getUserFinacingRepaymentAllList(@ModelAttribute BaseRequestForm paramForm,
                                                 String token, String userId, Integer type, String timestamp, String versionType) throws Exception {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || StringUtils.isEmpty(userId)  || type==null) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}

		VersionTypeEnum versionTypeEnum = VersionTypeEnum.CG.getCode().equals(versionType) ? VersionTypeEnum.CG : VersionTypeEnum.PT;

		Date startTime = StringUtils.isBlank(timestamp) ? null : DateUtil.timestampToDateBySec(Long.valueOf(timestamp));
		List<RepaymentInfoExt> repaymentInfoList= finacingService.getAllUserRepaymentItem(Integer.valueOf(userId), type, versionTypeEnum, startTime, 1);

		if(repaymentInfoList != null && repaymentInfoList.size() > 0){
			RepaymentInfoExt repaymentInfoExt = repaymentInfoList.get(repaymentInfoList.size() - 1);
			if(type == 1){
				startTime = repaymentInfoExt.getExpectedRepaymentDate();
			}
			if(type == 2){
				startTime = repaymentInfoExt.getActualRepaymentDate();
			}
			List<RepaymentInfoExt> repaymentInfoSuppleList= finacingService.getAllUserRepaymentItem(Integer.valueOf(userId), type, versionTypeEnum, startTime, 2);

			//先删除当天的
			repaymentInfoList = removeUserRepayment(repaymentInfoList, startTime, type);
			//再加上当天完整的
			repaymentInfoList.addAll(repaymentInfoSuppleList);
		}

		List<RepaymentInfoExtVO> repaymentInfoListV = new ArrayList<>();
		for (RepaymentInfoExt repaymentInfoExt : repaymentInfoList) {
			RepaymentInfoExtVO vo = new RepaymentInfoExtVO(repaymentInfoExt);
			repaymentInfoListV.add(vo);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("items", repaymentInfoListV);
		response.setData(map);
		return response;
	}

	/**
	 * 删除指定某天的数据
	 * @param repaymentInfoList
	 * @param startTime
	 * @param type
     * @return
     */
	private static List<RepaymentInfoExt> removeUserRepayment(List<RepaymentInfoExt> repaymentInfoList, Date startTime, Integer type){
		Iterator<RepaymentInfoExt> iterator = repaymentInfoList.iterator();
		while (iterator.hasNext()) {
			RepaymentInfoExt repaymentInfoExt = iterator.next();
			Date targetTime = null;
			if(type == 1){
				targetTime = repaymentInfoExt.getExpectedRepaymentDate();
			}
			if(type == 2){
				targetTime = repaymentInfoExt.getActualRepaymentDate();
			}
			if(startTime != null && targetTime != null && DateUtils.isSameDay(startTime, targetTime)){
				iterator.remove();
			}
		}
		return repaymentInfoList;
	}

	/**
	 * 回款总额
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "repayment/sum", method = RequestMethod.GET)
    HttpResponse getUserFinacingSumRepayment(@ModelAttribute BaseRequestForm paramForm,
                                             String token, String userId, VersionTypeEnum versionType) throws Exception {
		HttpResponse response = new HttpResponse();

		if (!paramForm.validate() || StringUtils.isEmpty(userId)|| versionType ==null) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		List<UserRepaymentAmout> list= finacingService.userRepaymentAmout(
				Integer.valueOf(userId),versionType.getIndex());
	    UserRepaymentAmoutVO vo=new UserRepaymentAmoutVO(list);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("expected", vo.getExpected());
		map.put("history", vo.getHistory());
		response.setData(map);
		return response;
	}
	private String planStaus(int PlanId) {
		List<PlanBidsStatus> planBidsList = this.bidInfoService.getPlanBidsStatus(PlanId,Integer.MAX_VALUE);
		String status = "";
		int i = 0;
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
