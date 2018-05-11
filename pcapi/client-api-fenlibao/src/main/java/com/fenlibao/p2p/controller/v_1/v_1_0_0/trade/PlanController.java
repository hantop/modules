package com.fenlibao.p2p.controller.v_1.v_1_0_0.trade;

import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.common.util.repayment.CalCapitalInterestUtil;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.borrow.BorrowerEntity;
import com.fenlibao.p2p.model.entity.finacing.PlanInvestInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.entity.trade.BidRecords;
import com.fenlibao.p2p.model.entity.trade.PlanBidProfit;
import com.fenlibao.p2p.model.entity.trade.PlanRecords;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.plan.PlanStatusEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.plan.PlanFinacingVO;
import com.fenlibao.p2p.model.vo.plan.QuitPlanInfoVO;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.FinacingService;
import com.fenlibao.p2p.service.bid.*;
import com.fenlibao.p2p.service.trade.ITradeService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.StatusUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 计划控制器
 */
@RequestMapping("plan")
@RestController("v_1_0_0/PlanController")
public class PlanController {
    private static final Logger logger = LogManager.getLogger(PlanController.class);

    @Resource
    private BidService bidService;
    @Resource
    private BidInfoService bidInfoService;
    @Resource
    private PlanService planService;
    @Resource
    private IUserDmService userDmService;
    @Resource
    private ITradeService tradeService;
    @Resource
    private PlanInfoService planInfoService;
    @Resource
    private FinacingService finacingService;

    @Resource
    private PlanBidService planBidService;

    /**
     * 出借计划
     *
     * @param paramForm
     * @param planId
     * @param sgSum
     * @param isCheck
     * @param dealPassword
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "doPlan", method = RequestMethod.POST)
    public HttpResponse doPlan(@ModelAttribute BaseRequestFormExtend paramForm, String planId, String sgSum, String fxhbIds, String jxqId, String isCheck, String dealPassword, String newPlan, String versionType) throws Throwable {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if (!paramForm.validate() || StringUtils.isBlank(planId)
                || StringUtils.isBlank(sgSum)) {
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
            return apiResponse;
        }

        //红包和加息卷不能同时使用
        if (!StringUtils.isBlank(fxhbIds) && !StringUtils.isBlank(jxqId)) {
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
            return apiResponse;
        }

        try {
            //未实名认证
            if (!userDmService.isSmrz(paramForm.getUserId())) {
                apiResponse.setCodeMessage(ResponseEnum.RESPONSE_BANK_CERTIFICATION.getCode(), ResponseEnum.RESPONSE_BANK_CERTIFICATION.getMessage());
                return apiResponse;
            }

            int planIds = IntegerParser.parse(planId);   // 计划的ID
            BigDecimal amount = BigDecimalParser.parse(sgSum);  //出借金额

            //整数判断
            BigDecimal amountTemp = amount.stripTrailingZeros();
            if (amountTemp.toPlainString().contains(".")) {
                throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_INTEGER); //投标金额必须为整数
            }

            if("1".equals(newPlan)){
                InvestPlan investPlan = planService.getInvestPlan(planIds);
                if (investPlan == null) {
                    apiResponse.setCodeMessage("120302", "指定的标不存在");
                    return apiResponse;
                }

                if(investPlan.getStatus() != PlanStatusEnum.TZZ.getCode()){
                    apiResponse.setCodeMessage(ResponseCode.BID_INVESTMENT_FAILURE);
                    return apiResponse;
                }

                //新手判断
                if (investPlan.getIsNovice() == 1) {
                    // 添加判断 非新用户不能出借新手标
                    boolean isNovice = bidInfoService.isNovice(paramForm.getUserId()); //是否是新手
                    if (!isNovice) {
                        throw new BusinessException(ResponseCode.BID_NOVICE_NOT_NEW_USER);
                    }
                    BigDecimal max = new BigDecimal(Config.get("bid.novice.invest.limit"));
                    if (amount.compareTo(max) > 0) {
                        throw new BusinessException(ResponseCode.BID_NOVICE_OVER_MAX_AMOUNT); //新手标投标金额不能大于最大限制金额
                    }
                }

                boolean canInvest;
                if(VersionTypeEnum.CG.equals(VersionTypeEnum.parse(investPlan.getIsCG()))){
                    canInvest = planInfoService.checkCanInvest(planIds,paramForm.getUserId(),VersionTypeEnum.CG);
                }else{
                    canInvest = planInfoService.checkCanInvest(planIds,paramForm.getUserId(),VersionTypeEnum.PT);
                }
                if(!canInvest){
                    throw new BusinessException(ResponseCode.BID_ANYTIME_QUIT_USER_INVEST);
                }

                if(VersionTypeEnum.CG.equals(VersionTypeEnum.parse(investPlan.getIsCG()))){
                    //planService.doInvestPlanForCG(planIds, amount, paramForm.getUserId(), fxhbIds, jxqId, isCheck);
                    planBidService.doInvestPlan(planIds, amount,  paramForm.getUserId(),  fxhbIds,  jxqId);
                }else{
                    //首先验证交易密码是否正确
                    if (!bidService.isValidPassword(paramForm.getUserId(), dealPassword)) {
                        apiResponse.setCodeMessage("30621", "交易密码不正确，建议您重置交易密码");//交易密码错误
                        return apiResponse;
                    }
                    planService.doInvestPlan(planIds, amount, paramForm.getUserId(), fxhbIds, jxqId, isCheck);
                }
            }else if("0".equals(newPlan) || StringUtils.isEmpty(newPlan)){
                Plan plan = planService.getPlan(planIds);

                if (plan == null) {
                    apiResponse.setCodeMessage("120302", "指定的标不存在");
                    return apiResponse;
                }

                //新手判断
                if ("S".equalsIgnoreCase(plan.getIsNoviceBid())) {
                    // 添加判断 非新用户不能出借新手标
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
                if (!bidService.isValidPassword(paramForm.getUserId(), dealPassword)) {
                    apiResponse.setCodeMessage("30621", "交易密码不正确，建议您重置交易密码");//交易密码错误
                    return apiResponse;
                }

                planService.doPlan(plan, amount, paramForm.getUserId(), fxhbIds, jxqId, isCheck);
            }else{
                apiResponse.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
                return apiResponse;
            }
        } catch (BusinessException busi) {
            logger.debug("出借计划失败，planId=[{}],出借金额=[{}],userId=[{}],msg=[{}]", planId, sgSum, paramForm.getUserId(), busi.getMessage());
            apiResponse.setCodeMessage(busi);
        } catch (XWTradeException xwt) {
            logger.debug("出借计划失败，planId=[{}],出借金额=[{}],userId=[{}],msg=[{}]", planId, sgSum, paramForm.getUserId(),xwt.getMessage());
            apiResponse.setCodeMessage(xwt);
        }catch (Throwable throwable) {
            logger.debug("出借计划失败，planId=[{}],出借金额=[{}],userId=[{}],msg=[{}]", planId, sgSum, paramForm.getUserId(), throwable.getMessage());
            throwable.printStackTrace();
            String message = throwable.getMessage();
            if (!StringUtils.isEmpty(message)) {
                if ("30600".equals(message)) {
                    apiResponse.setCodeMessage("30600", "出借失败");    //借款逾期未还，不能投标！
                } else if ("30601".equals(message)) {
                    apiResponse.setCodeMessage("30601", "出借金额需为100元的整数倍"); //投标金额必须为整数
                } else if ("30602".equals(message)) {
                    apiResponse.setCodeMessage("40602", "您加入的标已过期，请于下次出借日加入");    //指定的标记录不存在
                } else if ("30603".equals(message)) {
                    apiResponse.setCodeMessage("30603", "出借失败");    //您是该标的借款人，不能投标
                } else if ("30604".equals(message)) {
                    apiResponse.setCodeMessage("30604", "出借失败");        //指定的标不是投标中状态,不能投标
                } else if ("30605".equals(message)) {
                    apiResponse.setCodeMessage("30605", "出借金额不正确");    //投标金额大于可投金额
                } else if ("30606".equals(message)) {
                    apiResponse.setCodeMessage("30606", "出借金额不满100元"); //投标金额不能低于最低起投金额
                } else if ("30607".equals(message)) {
                    apiResponse.setCodeMessage("30607", "出借失败");    //剩余可投金额不能低于最低起投金额
                } else if ("30608".equals(message)) {
                    apiResponse.setCodeMessage("30608", "出借失败");    //用户往来账户不存在
                } else if ("30609".equals(message)) {
                    apiResponse.setCodeMessage("40609", "您的账户余额不足，请及时充值");  //账户余额不足
                } else if ("30610".equals(message)) {
                    apiResponse.setCodeMessage("30610", "出借失败");    //订单明细记录不存在
                } else if ("30611".equals(message)) {
                    apiResponse.setCodeMessage("30611", "出借失败");    //不可投本账号发的标
                } else if ("30612".equals(message)) {
                    apiResponse.setCodeMessage("30612", "出借失败");    //平台账号不存在
                } else if ("30613".equals(message)) {
                    apiResponse.setCodeMessage("30613", "出借失败");    //出借人往来账户不存在
                } else if ("30614".equals(message)) {
                    apiResponse.setCodeMessage("30614", "出借失败");    //出借人锁定账户不存在
                } else if ("30615".equals(message)) {
                    apiResponse.setCodeMessage("40615", "交易密码不正确，建议您重置交易密码");   //交易密码错误
                } else if ("30616".equals(message)) {
                    apiResponse.setCodeMessage("30616", "出借失败");    //查询记录有误,没有查询到用户信息
                } else if ("30617".equals(message)) {
                    apiResponse.setCodeMessage("30617", "未设置交易密码");
                } else if ("30621".equals(message)) {
                    apiResponse.setCodeMessage("30621", "交易密码验证失败");//交易密码验证出现异常
                } else if ("30623".equals(message)) {
                    apiResponse.setCodeMessage("30623", "出借金额必须为100的整数倍");//投标金额必须为100的整数倍
                } else if ("120319".equals(message)) {
                    apiResponse.setCodeMessage("120319", "加息券使用条件不满足");//投标金额必须为100的整数倍
                } else if (ResponseCode.USER_IS_HAVE_BID.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.USER_IS_HAVE_BID.getCode(), ResponseCode.USER_IS_HAVE_BID.getMessage());//投标金额必须为100的整数倍
                } else if (ResponseCode.NOVICE_OVER_MAX_AMOUNT.getCode().endsWith(message)) {
                    apiResponse.setCodeMessage(ResponseCode.NOVICE_OVER_MAX_AMOUNT.getCode(), ResponseCode.NOVICE_OVER_MAX_AMOUNT.getMessage());
                } else if (ResponseCode.NOVICE_NOT_USE_FXHB.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.NOVICE_NOT_USE_FXHB.getCode(), ResponseCode.NOVICE_NOT_USE_FXHB.getMessage());
                } else if (ResponseCode.NOVICE_NOT_NEW_USER.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.NOVICE_NOT_NEW_USER.getCode(), ResponseCode.NOVICE_NOT_NEW_USER.getMessage());
                } else if (ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode().equals(message)) {
                    apiResponse.setCodeMessage(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode(), ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getMessage());
                } else {
                    //扩展返现红包提示 throw new LogicalException("30703&" + useMoney);
                    if (!StringUtils.isBlank(message)) {
                        String[] msgArr = message.split("\\&");
                        String msgTip = "";
                        if (msgArr.length > 1) {
                            String code = msgArr[0];
                            String tips = msgArr[1];
                            //返现红包错误提示
                            if ("30701".equals(code)) {
                                apiResponse.setCodeMessage("30701", tips + "返现红包已经使用");
                            } else if ("30702".equals(code)) {
                                apiResponse.setCodeMessage("30702", tips + "返现红包已经过期");
                            } else if ("30703".equals(code)) {
                                apiResponse.setCodeMessage("30703", "出借金额不能使用" + tips + "返现红包");
                            } else {
                                apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
                            }
                        } else {
                            if ("30700".equals(msgArr[0])) {
                                apiResponse.setCodeMessage("30700", "返现红包不存在");
                            } else if ("30704".equals(msgArr[0])) {
                                apiResponse.setCodeMessage("30704", "返现红包使用不符合规则");//返现红包使用不符合规则
                            } else {
                                apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
                            }
                        }
                    }
                }
            } else {
                apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
            }
        }
        return apiResponse;
    }

    /**
     * 我的计划出借对应标记录
     *
     * @param pageRequestForm
     * @param paramForm
     * @param planRecordId
     * @return
     */
    @RequestMapping(value = "bidRecordList", method = RequestMethod.GET)
    public HttpResponse bidRecordList(@ModelAttribute PageRequestForm pageRequestForm, @ModelAttribute BaseRequestFormExtend paramForm, String planRecordId, int newPlan) {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate() || !pageRequestForm.validate() || StringUtils.isBlank(planRecordId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));
            List<BidRecords> bidRecordsList = null;
            if (0 == newPlan) {
                bidRecordsList = tradeService.getPlanBidRecordsList(paramForm.getUserId(), Integer.valueOf(planRecordId), pageBounds);
            }else{
                bidRecordsList = tradeService.getNewPlanBidRecordsList(paramForm.getUserId(), Integer.valueOf(planRecordId), pageBounds);
            }
            Pager pager = new Pager(bidRecordsList);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[PlanController.bidRecordList]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 计划出借记录
     *
     * @param pageRequestForm
     * @return
     */
    @RequestMapping(value = "recordList", method = RequestMethod.GET)
    public HttpResponse recordList(@ModelAttribute PageRequestForm pageRequestForm, String planId, String newPlan) {
        HttpResponse response = new HttpResponse();
        if (!pageRequestForm.validate() || StringUtils.isBlank(planId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        int planType ;
        if (newPlan != null && newPlan.equals("1")) {
            planType = 1;
        } else {
            planType = 0;
        }
        try {
            PageBounds pageBounds = new PageBounds(Integer.valueOf(pageRequestForm.getPage()), Integer.valueOf(pageRequestForm.getLimit()));
            List<PlanRecords> bidRecordsList = null;
            if (0 == planType) {
                bidRecordsList = tradeService.getPlanRecordsList(Integer.valueOf(planId), pageBounds);
            } else {
                bidRecordsList = tradeService.getNewPanRecordsList(Integer.valueOf(planId), pageBounds);
            }
            Pager pager = new Pager(bidRecordsList);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[PlanController.recordList]", e);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 我的持有中的计划列表
     * notice: 时间有关的参数除以1000传给前台
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "myPlanList", method = RequestMethod.GET)
    public HttpResponse myPlanList(@ModelAttribute BaseRequestFormExtend paramForm,
                                   String page,
                                   String limit,
                                   String versionType) {
        HttpResponse response = new HttpResponse();
        if ( !paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        VersionTypeEnum vte = VersionTypeEnum.parse(versionType);
        vte = vte != null ? vte : VersionTypeEnum.PT;

        try {
            List<PlanFinacingVO> planVOs = new ArrayList<>();
            PageBounds pageBounds = new PageBounds(Integer.valueOf(page), Integer.valueOf(limit));
            List<PlanInvestInfo> planRecordInfos = planService.getAllMyPlan(paramForm.getUserId(), vte, pageBounds);
            for (PlanInvestInfo recordInfo : planRecordInfos) {
                PlanFinacingVO vo = new PlanFinacingVO();
                vo.setRecordId(recordInfo.getCreditId());
                vo.setVoteAmount(recordInfo.getVoteAmount());
                vo.setNewPlan(recordInfo.getNewPlan());
                vo.setLoanDays(recordInfo.getLoanDays());
                vo.setLoanMonths(recordInfo.getLoanMonths());
                vo.setJxFlag(recordInfo.getJxFlag());
                vo.setPlanId(recordInfo.getPlanId());
                vo.setPlanTitle(recordInfo.getPlanTitle());
                vo.setPurchaseTime(recordInfo.getPurchaseTime().getTime()/1000);
                vo.setBidInterestRise(recordInfo.getBidInterestRise());
                vo.setNovicePlan(recordInfo.getIsNoviceBid());
                vo.setInterestTime(recordInfo.getInterestTime()!=null?recordInfo.getInterestTime().getTime()/1000:null);
                if (recordInfo.getNewPlan() == 0) {//老计划
                    vo.setInvestStatus(StatusUtil.investStatus(recordInfo.getBidStatus()));
                    vo.setMonthYearYield(recordInfo.getYearYield());
                    BigDecimal expectedProfitPlan;
                    if (recordInfo.getLoanDays() != 0) {
                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(recordInfo.getInvestAmount(),new BigDecimal(vo.getMonthYearYield()), recordInfo.getLoanDays());
                    } else {
                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(recordInfo.getInvestAmount(), new BigDecimal(vo.getMonthYearYield()), recordInfo.getLoanMonths(),recordInfo.getRepaymentMode());
                    }
                    vo.setInvestAmount(recordInfo.getInvestAmount());
                    vo.setExpectedProfit(expectedProfitPlan);
                } else if (recordInfo.getNewPlan() == 1) {//新计划
                    recordInfo.setPlanStatus(StatusUtil.status(recordInfo.getPlanStatus()));
                    vo.setInvestStatus(StatusUtil.investStatus(recordInfo.getPlanStatus()));
                    if (recordInfo.getHoldStatus() == 2) vo.setInvestStatus(StatusUtil.investStatus("ZRZ"));
                    //这里表示到期退出时间
                    vo.setExpiredDate(recordInfo.getExpireDate()==null?null:recordInfo.getExpireDate().getTime()/1000);
                    double rate = planService.getNewPlanRate(recordInfo);
                    vo.setMonthYearYield(rate);
                    vo.setInvestAmount(recordInfo.getInvestAmount());
                    BigDecimal expectedProfitPlan;
                    double expectdRate = recordInfo.getType()==1?recordInfo.getHighRate()+recordInfo.getBidInterestRise().doubleValue()
                            :recordInfo.getYearYield()+recordInfo.getBidInterestRise().doubleValue();//这个是预期收益的利率
                    if (recordInfo.getLoanDays() != 0) {
                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(recordInfo.getInvestAmount(), new BigDecimal(expectdRate), recordInfo.getLoanDays());
                    } else {
                        expectedProfitPlan = CalCapitalInterestUtil.calTotalInterest(recordInfo.getInvestAmount(), new BigDecimal(expectdRate), recordInfo.getLoanMonths(),recordInfo.getRepaymentMode());
                    }
                    vo.setExpectedProfit(expectedProfitPlan);

                    if(!recordInfo.getIsNoviceBid().equals("S")){
                        vo.setPlanCanQuit(1);
                    }
                }
                vo.setPlanStatus(recordInfo.getPlanStatus());
                planVOs.add(vo);
            }

            Pager pager = new Pager(planRecordInfos);
            pager.setItems(planVOs);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[PlanController.myPlanList]", ex);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 我的退出中的计划列表
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "myQuitPlanList", method = RequestMethod.GET)
    public HttpResponse quitPlanList(@ModelAttribute BaseRequestFormExtend paramForm,
                                   String page,
                                   String limit,
                                   String versionType) {
        HttpResponse response = new HttpResponse();
        if ( !paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        VersionTypeEnum versionTypeEnum = VersionTypeEnum.parse(versionType);
        versionTypeEnum = versionTypeEnum != null ? versionTypeEnum : VersionTypeEnum.PT;

        try {
            List<PlanFinacingVO> planVOs = new ArrayList<>();
            PageBounds pageBounds = new PageBounds(Integer.valueOf(page), Integer.valueOf(limit));
            List<PlanInvestInfo> planRecordInfos = planService.getMyQuitPlan(paramForm.getUserId(), versionTypeEnum, pageBounds);
            for (PlanInvestInfo recordInfo : planRecordInfos) {
                PlanFinacingVO vo = new PlanFinacingVO();
                vo.setVoteAmount(recordInfo.getVoteAmount());
                vo.setRecordId(recordInfo.getCreditId());
                vo.setRecordId(recordInfo.getCreditId());
                vo.setNewPlan(recordInfo.getNewPlan());
                vo.setLoanDays(recordInfo.getLoanDays());
                vo.setLoanMonths(recordInfo.getLoanMonths());
                vo.setJxFlag(recordInfo.getJxFlag());
                vo.setPlanId(recordInfo.getPlanId());
                vo.setPlanTitle(recordInfo.getPlanTitle());
                vo.setPurchaseTime(recordInfo.getPurchaseTime().getTime()/1000);
                vo.setBidInterestRise(recordInfo.getBidInterestRise());
                if(recordInfo.getApplyTime()!=null) {
                    vo.setApplyTime(recordInfo.getApplyTime().getTime() / 1000);
                }
                vo.setHoldDays(recordInfo.getInterestTime()==null?null:DateUtil.daysOfTwo(recordInfo.getInterestTime(),new Date()));
                recordInfo.setPlanStatus(StatusUtil.status(recordInfo.getPlanStatus()));
                vo.setInvestStatus(StatusUtil.investStatus(recordInfo.getPlanStatus()));
                if (recordInfo.getHoldStatus() == 2) vo.setInvestStatus(StatusUtil.investStatus("ZRZ"));

                double rate = planService.getNewPlanRate(recordInfo);
                vo.setMonthYearYield(rate);
                vo.setInvestAmount(recordInfo.getInvestAmount());
                BigDecimal expectedProfitPlan = BigDecimal.ZERO;
                if (vo.getHoldDays() != null && vo.getHoldDays() > 0) {
                    expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(recordInfo.getInvestAmount(), new BigDecimal(rate).add(recordInfo.getBidInterestRise()), vo.getHoldDays());
                }

                vo.setExpectedProfit(expectedProfitPlan);
                vo.setPlanStatus(recordInfo.getPlanStatus());
                planVOs.add(vo);
            }

            Pager pager = new Pager(planRecordInfos);
            pager.setItems(planVOs);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[PlanController.myQuitPlanList]", ex);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 我的成功退出的计划列表
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "mySuccessPlans", method = RequestMethod.GET)
    public HttpResponse mySuccessPlans(@ModelAttribute BaseRequestFormExtend paramForm,
                                       String page,
                                       String limit,
                                       String versionType) {
        HttpResponse response = new HttpResponse();
        if ( !paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }

        VersionTypeEnum versionTypeEnum = VersionTypeEnum.parse(versionType);
        versionTypeEnum = versionTypeEnum != null ? versionTypeEnum : VersionTypeEnum.PT;

        try {
            List<PlanFinacingVO> planVOs = new ArrayList<>();
            PageBounds pageBounds = new PageBounds(Integer.valueOf(page), Integer.valueOf(limit));
            List<PlanInvestInfo> planRecordInfos = planService.getMySuccessPlans(paramForm.getUserId(), versionTypeEnum, pageBounds);
            for (PlanInvestInfo recordInfo : planRecordInfos) {
                PlanFinacingVO vo = new PlanFinacingVO();
                vo.setVoteAmount(recordInfo.getVoteAmount());
                vo.setRecordId(recordInfo.getCreditId());
                vo.setNewPlan(recordInfo.getNewPlan());
                vo.setLoanDays(recordInfo.getLoanDays());
                vo.setLoanMonths(recordInfo.getLoanMonths());
                vo.setJxFlag(recordInfo.getJxFlag());
                vo.setPlanId(recordInfo.getPlanId());
                vo.setPlanTitle(recordInfo.getPlanTitle());
                vo.setPurchaseTime(recordInfo.getPurchaseTime().getTime()/1000);
                vo.setBidInterestRise(recordInfo.getBidInterestRise());
                vo.setInterestTime(recordInfo.getInterestTime()==null?null:recordInfo.getInterestTime().getTime());
                vo.setInvestAmount(recordInfo.getInvestAmount());

                if (recordInfo.getExpireDate() != null) {
                    vo.setExpiredDate(recordInfo.getExpireDate().getTime()/1000);
                }
                if (recordInfo.getExitTime() != null) {
                    vo.setExpiredDate(recordInfo.getExitTime().getTime()/1000);
                }
                if (recordInfo.getNewPlan() == 0) {//老计划
                    vo.setHoldDays(recordInfo.getLoanDays());
                    vo.setInvestStatus(StatusUtil.investStatus(recordInfo.getBidStatus()));
                    vo.setMonthYearYield(recordInfo.getYearYield());
                    BigDecimal expectedProfitPlan = BigDecimal.ZERO;
                    //计划已获收益
                    List<PlanBidProfit> profitList = tradeService.getOldPlanProfit(recordInfo.getCreditId());
                    if(null!=profitList&&profitList.size()>0){
                        for(PlanBidProfit planBidProfit : profitList){
                            expectedProfitPlan = expectedProfitPlan.add(planBidProfit.getProfit()).add(planBidProfit.getRaiseProfit());
                        }
                    }
                    vo.setExpectedProfit(expectedProfitPlan);
                } else if (recordInfo.getNewPlan() == 1) {//新计划
                    vo.setHoldDays(DateUtil.daysOfTwo(recordInfo.getInterestTime(),recordInfo.getExitTime()));
                    recordInfo.setPlanStatus(StatusUtil.status(recordInfo.getPlanStatus()));
                    vo.setInvestStatus(StatusUtil.investStatus(recordInfo.getPlanStatus()));
                    //判断是否是提前退出的
                    if (recordInfo.getExitTime().compareTo(recordInfo.getExpireDate()) < 0) {
                        int month = DateUtil.monthsBetweenDates(recordInfo.getInterestTime(), recordInfo.getExitTime());
                        BigDecimal rate = new BigDecimal(recordInfo.getLowRate()+recordInfo.getBonusRate()*month)
                                .add(recordInfo.getBidInterestRise()!=null?recordInfo.getBidInterestRise():BigDecimal.ZERO);
                        vo.setInvestStatus(6);
                        vo.setMonthYearYield(rate.doubleValue());
                    }else {
                        vo.setMonthYearYield(recordInfo.getHighRate());
                    }

                    if(recordInfo.getType() == 2){
                        vo.setMonthYearYield(recordInfo.getYearYield());
                    }

                    vo.setExpectedProfit(recordInfo.getExpectedProfit());

                }
                vo.setPlanStatus(recordInfo.getPlanStatus());
                planVOs.add(vo);
            }

            Pager pager = new Pager(planRecordInfos);
            pager.setItems(planVOs);
            response.setData(CommonTool.toMap(pager));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[PlanController.mySuccessPlans]", ex);
            response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(), ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
        }
        return response;
    }

    /**
     * 计划退出申请信息(申请时需显示的信息)
     *
     * @param paramForm
     * @param token
     * @param userId
     * @param planRecordId 计划出借记录id
     * @return notic:除非出现逾期，否则债权转让不受其他条件限制
     */
    @RequestMapping(value = "applyfor/info", method = RequestMethod.GET)
    HttpResponse applayforInfo(@ModelAttribute BaseRequestForm paramForm,
                               String token,
                               String userId,
                               String planRecordId) throws Exception {
        HttpResponse response = new HttpResponse();

        if (!paramForm.validate() ||  StringUtils.isEmpty(planRecordId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }

        PlanRecordInfo planRecordInfo = planInfoService.getPlanRecordInfo(IntegerParser.parse(planRecordId),IntegerParser.parse(userId));

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
        quitPlanInfoVO.setCreditCapitalAmount(planRecordInfo.getInvestAmount());

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
        if (planRecordInfo.getType() == 2) { //省心计划用出借时的利率
            quitPlanInfoVO.setBidYield(planRecordInfo.getInvestRate()+planRecordInfo.getRaiseRate()+planRecordInfo.getCouponRise());
            rate = new BigDecimal(planRecordInfo.getInvestRate()+planRecordInfo.getCouponRise()+planRecordInfo.getRaiseRate());
            expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(planRecordInfo.getInvestAmount()),rate,holdDays);
        } else if (planRecordInfo.getType() == 1) {

            int month = DateUtil.monthsBetweenDates(planRecordInfo.getInterestTime(), new Date());
            rate = new BigDecimal(planRecordInfo.getLowRate()+month*planRecordInfo.getMoIncreaseRate()+planRecordInfo.getCouponRise());
            quitPlanInfoVO.setBidYield(rate.doubleValue());
            expectedProfitPlan   = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(planRecordInfo.getInvestAmount()), rate, holdDays);
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
                                String token,
                                String userId,
                                String planRecordId,
                                String dealPassword,
                                String versionType) throws Exception {
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || StringUtils.isEmpty(planRecordId)) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
                return response;
            }

            //验证用户交易密码
            if (versionType!=null&&VersionTypeEnum.PT.getCode().equals(versionType)&&!tradeService.isValidUserPwd(Integer.valueOf(userId), dealPassword)) {
                response.setCodeMessage(ResponseCode.TRADE_USER_NOT_RIGHT_PASSWORD);//交易密码错误
                return response;
            }

            PlanRecordInfo planRecordInfo = planInfoService.getPlanRecordInfo(IntegerParser.parse(planRecordId), IntegerParser.parse(userId));

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
            if (planRecordInfo.getType() == 2) { //省心计划用出借时的利率
                expectedProfitPlan = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(planRecordInfo.getInvestAmount()),new BigDecimal(planRecordInfo.getInvestRate()),holdDays);
            } else if (planRecordInfo.getType() == 1) {

                int month = DateUtil.monthsBetweenDates(planRecordInfo.getInterestTime(),new Date());
                rate = new BigDecimal(planRecordInfo.getLowRate()+month*planRecordInfo.getMoIncreaseRate());
                expectedProfitPlan   = CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(new BigDecimal(planRecordInfo.getInvestAmount()), rate, holdDays);
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
        if(planRecordInfo.getType()!=1) {
            //计划债权持有时间超过3天
            if (null == planRecordInfo.getInterestTime() || DateUtil.getDayBetweenDates(planRecordInfo.getInterestTime(), DateUtil.nowDate()) <= InterfaceConst.PLAN_ZQZR_CY_DAY) {
                response.setCodeMessage(ResponseCode.TCJH_SJWD);
                resultMap.put("response", response);
                return resultMap;
            }
        }else {
            if (null == planRecordInfo.getInterestTime() || DateUtil.getDayBetweenDates(planRecordInfo.getInterestTime(), DateUtil.nowDate()) < InterfaceConst.ZQZR_CY_DAY) {
                response.setCodeMessage(ResponseCode.TCJH_SJWD);
                resultMap.put("response", response);
                return resultMap;
            }
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

    @RequestMapping(value = "/bidList", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse bidList(@ModelAttribute PageRequestForm pageRequestForm,
                                       @RequestParam(required = false, value = "planId") String planId) {
        HttpResponse response = new HttpResponse();
        if (!pageRequestForm.validate() || StringUtils.isEmpty(planId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        int limit = Integer.valueOf(pageRequestForm.getLimit());
        int page = Integer.valueOf(pageRequestForm.getPage());
        try {


            PageBounds pageBounds = new PageBounds(page, limit);
            List<BorrowerEntity> bidList = this.planInfoService.getPlanBidList(Integer.valueOf(planId), pageBounds);
            Pager pager = new Pager(bidList);

            response.setData(CommonTool.toMap(pager));

        } catch (Throwable ex) {
            response.setCodeMessage(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage());
            logger.error("[planController.bidList]" + ex.getMessage(), ex);

        }
        return response;

    }

}

