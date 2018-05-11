package com.fenlibao.pms.controller.investUser;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.investUser.*;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.service.pms.da.cs.account.TransactionService;
import com.fenlibao.service.pms.da.cs.account.UserDetailInfoService;
import com.fenlibao.service.pms.da.cs.investUser.InvestUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 客服管理-投资用户
 * Created by Administrator on 2017/12/5.
 */
@Controller
@RequestMapping("cs/investUser")
public class InvestUserController {


    private Logger log = LoggerFactory.getLogger(InvestUserController.class);

    @Resource
    private UserDetailInfoService userDetailInfoService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private InvestUserService investUserService;

    @Autowired
    private XWUserInfoService xwUserInfoService;

    /**
     * 投资用户列表
     * @param request
     * @param page
     * @param limit
     * @param moblie
     * @param username
     * @param idCard
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request,
                                   @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                   @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                   String moblie, String username, String idCard) {
        ModelAndView view = new ModelAndView("cs/investUser/list");
        if(moblie == null && username == null && idCard == null){
            return view;
        }
        String msg = "";
        RowBounds bounds = new RowBounds(page, limit);
        List<UserDetail> userDetailList = new ArrayList<>();
        userDetailList = userDetailInfoService.getUserDetail(moblie,username,idCard, bounds);
        if(CollectionUtils.isEmpty(userDetailList)){
            msg = "此用户不存在";
        }else{
            UserDetailInfo userDetailInfo = userDetailInfoService.getFullUserDetailInfo(userDetailList.get(0).getUserId()+"");
            view.addObject("userDetailInfo",userDetailInfo);
        }
        return view
                .addObject("moblie",moblie)
                .addObject("username",username)
                .addObject("idCard",idCard)
                .addObject("msg",msg)
                .addObject("userDetailList",userDetailList);
    }

    /**
     * 查看
     * @param userId
     * @return
     */
    @RequestMapping("/getDetailInfo")
    public ModelAndView getDetailInfo(String userId, HttpServletRequest request){
        ModelAndView view = new ModelAndView("cs/investUser/detailInfo");
        try {
            UserDetailInfo userDetailInfo = userDetailInfoService.getFullUserDetailInfo(userId);
            view.addObject("userDetailInfo",userDetailInfo);
            view.addObject("userId",userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    /**
     *投标记录
     * @param request
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getInvestList")
    public ModelAndView getInvestList(HttpServletRequest request,
                                      @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                      @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                      String userId, InvestRecord investRecord, String startTime, String endTime, String actionType){
        ModelAndView view = new ModelAndView("cs/investUser/li/invest_list");
        RowBounds bounds = new RowBounds(page, limit);
        List<InvestRecord> investRecordList = null;
        if (StringUtils.isNotEmpty(actionType) && "BUYOUT".equals(actionType)) {
            // 卖出列表
            investRecordList = investUserService.getInvestBuyOutList(investRecord, userId, bounds, startTime, endTime);
        } else {
            //获取投资记录列表，部分字段逻辑较复杂，不在此次查询 买入/投标列表
            investRecordList = investUserService.getInvestList(investRecord, userId, bounds, startTime, endTime);
        }

        if(CollectionUtils.isNotEmpty(investRecordList)) {
            //补充未查询的列表字段
              // 到期日期
//            List<InvestRecord> expireTimeList = investUserService.getExpireTime(investRecordList);
//            view.addObject("expireTimeList",expireTimeList);
            // 债权利息 和 实际收益
            List<InvestRecord> rightInterestList = investUserService.getRightsInterest(investRecordList,userId);
            view.addObject("rightInterestList",rightInterestList);
            // 投资券（使用卷：返现卷、加息卷）
            List<InvestRecord> couponList = investUserService.getCouponList(investRecordList,userId);
            view.addObject("couponList", couponList);
        }

        UserDetailInfo userDetailInfo = userDetailInfoService.getFullUserDetailInfo(userId);
        view.addObject("userDetailInfo",userDetailInfo);
        PageInfo<InvestRecord> paginator = new PageInfo<>(investRecordList);
        view.addObject("investRecordList", investRecordList);
        view.addObject("userId", userId);
        view.addObject("investRecord", investRecord);
        view.addObject("startTime", startTime);
        view.addObject("endTime", endTime);
        view.addObject("actionType", actionType);
        view.addObject("paginator", paginator);
        view.addObject("getInvestList", true);//li tab标签激活
        return view;
    }

    /**
     * 债权转让列表
     * @param request
     * @param page
     * @param limit
     * @param userId
     * @param rightsRecord
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/getRightsList")
    public ModelAndView getRightsList(HttpServletRequest request,
                                      @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                      @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                      String userId,RightsRecord rightsRecord,String startTime,String endTime){

        ModelAndView view = new ModelAndView("cs/investUser/li/rights_list");
        RowBounds bounds = new RowBounds(page, limit);

        //获取债权记录列表，部分字段逻辑较复杂，不在此次查询
        List<RightsRecord> rightsRecordList = investUserService.getRightsList(rightsRecord,userId,bounds, startTime, endTime);

        if(CollectionUtils.isNotEmpty(rightsRecordList)){
            //TODO:获取手续费
//            List<RightsRecord> rightsRecordFeeList = investUserService.getRightsRecordFeeList(rightsRecordList);
        }
        PageInfo<RightsRecord> paginator = new PageInfo<>(rightsRecordList);
        view.addObject("rightsRecordList",rightsRecordList);
        view.addObject("userId",userId);
        view.addObject("rightsRecord",rightsRecord);
        view.addObject("startTime",startTime);
        view.addObject("endTime",endTime);
        view.addObject("paginator", paginator);
        return view;
    }





    /**
     * 获取计划记录
     * @param
     * @return
     */
    @RequestMapping(value = "/getPlanList")
    public ModelAndView getPlanList(HttpServletRequest request,
                                    @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                    @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                    String userId, UserInvestPlan userInvestPlan, String startTime, String endTime){
        ModelAndView view = new ModelAndView("cs/investUser/li/plan_list");
        RowBounds bounds = new RowBounds(page, limit);
        List<UserInvestPlan> list = this.investUserService.getPlanList(userId,userInvestPlan, bounds);
        //补充未查询的列表字段
        //收益 / 加息卷 / 红包
        for (UserInvestPlan investPlan : list) {
            BigDecimal scope = investUserService.getPlanScope(investPlan.getRecordId(), userId);
            if (scope == null) {
                scope = BigDecimal.ZERO;
            }
            BigDecimal redPacketMoney = investUserService.getPlanRedPacketMoney(investPlan.getRecordId(), userId);
            if (redPacketMoney == null) {
                redPacketMoney = BigDecimal.ZERO;
            }
            investPlan.setScope(scope);
            investPlan.setRedPacketMoney(redPacketMoney);
            if (investPlan.getStatus().equals("6")) {
                BigDecimal interestAmount = investUserService.getActualInterest(investPlan.getRecordId(), investPlan.getPlanId(), userId);
                investPlan.setInterestAmount(interestAmount);
            } else {
                BigDecimal interestAmount = BigDecimal.ZERO;
                if (investPlan.getStatus().equals("4")) {
                    BigDecimal investAmount = investPlan.getTradeAmount();
                    BigDecimal totalRate = investPlan.getInvestRate().add(investPlan.getBidScope()).add(investPlan.getScope());
                    String cycleType = investPlan.getCycleType();
                    int cycle =  investPlan.getCycle();
                    //借款周期参数
                    int period = "d".equals(cycleType) ? 365 : 12;
                    //总收益
                    interestAmount = investAmount.multiply(totalRate).multiply(new BigDecimal(cycle))
                            .divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP);
                } else {
                    interestAmount = investUserService.getExpectInterest(investPlan.getRecordId(), investPlan.getPlanId(), userId);
                }
                investPlan.setInterestAmount(interestAmount);
            }
        }
        UserDetailInfo userDetailInfo = userDetailInfoService.getFullUserDetailInfo(userId);
        view.addObject("userDetailInfo",userDetailInfo);
        PageInfo<UserInvestPlan> paginator = new PageInfo<UserInvestPlan>(list);
        view.addObject("list",list);
        view.addObject("userId",userId);
        view.addObject("userInvestPlan",userInvestPlan);
        view.addObject("paginator", paginator);
        view.addObject("getPlanList", true);//li tab标签激活
        return view;
    }


    /**
     * 计划详情
     * @param request
     * @param page
     * @param limit
     * @param userId
     * @param userInvestPlan
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/getPlanDetail")
    public ModelAndView getPlanDetail(HttpServletRequest request,
                                      @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                      @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                      String userId, UserInvestPlan userInvestPlan, String startTime, String endTime){
        ModelAndView view = new ModelAndView("cs/investUser/li/plan_detail");
        RowBounds bounds = new RowBounds(page, limit);

        List<PlanDetail> details = investUserService.getPlanDetail(userId,userInvestPlan, bounds);

        PageInfo<PlanDetail> paginator = new PageInfo<PlanDetail>(details);
        view.addObject("details",details);
        view.addObject("userId",userId);
        view.addObject("paginator", paginator);



        return view;

    }

    /**
     * 交易记录
     * @param request
     * @param page
     * @param limit
     * @param tradeForm
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getTransactionList")
    public ModelAndView getTransactionList(HttpServletRequest request,
                                           @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                           @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit,
                                           TradeInfo tradeForm,
                                           String userId, String startTime1, String endTime1){

        ModelAndView view = new ModelAndView("cs/investUser/li/transaction_list");
        RowBounds bounds = new RowBounds(page, limit);
        try {
            if (StringUtils.isNotEmpty(startTime1)) {
                tradeForm.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse(startTime1));
            }
            if (StringUtils.isNotEmpty(endTime1)) {
                tradeForm.setEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(endTime1));
            }
        } catch (Exception e) {}
        XWFundAccount xwFundAccount = xwUserInfoService.getFundAccount(Integer.parseInt(userId), SysFundAccountType.XW_INVESTOR_WLZH);
        List<TradeInfo> list = new ArrayList<>();
        if (xwFundAccount != null) {
            list = this.investUserService.getTransactionList("" + xwFundAccount.getId(), tradeForm, bounds);
        }
        UserDetailInfo userDetailInfo = userDetailInfoService.getFullUserDetailInfo(userId);
        List<TradeType> tradeTypes = investUserService.getTradeTypes();
        view.addObject("userDetailInfo",userDetailInfo);
        PageInfo<TradeInfo> paginator = new PageInfo<>(list);
        view.addObject("list", list);
        view.addObject("userId", userId);
        view.addObject("tradeForm", tradeForm);
        view.addObject("startTime1", startTime1);
        view.addObject("endTime1", endTime1);
        view.addObject("tradeTypes", tradeTypes);
        view.addObject("paginator", paginator);
        view.addObject("getTransactionList", true);//li tab标签激活
        return view;
    }

    @RequestMapping(value = "export", method = {RequestMethod.GET, RequestMethod.POST})
    public void export(HttpServletResponse response, TradeInfo tradeForm,
                       String userId, String startTime1, String endTime1) {
        RowBounds bounds = new RowBounds();
        try {
            if (StringUtils.isNotEmpty(startTime1)) {
                tradeForm.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse(startTime1));
            }
            if (StringUtils.isNotEmpty(endTime1)) {
                tradeForm.setEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(endTime1));
            }
        } catch (Exception e) {}

        XWFundAccount xwFundAccount = xwUserInfoService.getFundAccount(Integer.parseInt(userId), SysFundAccountType.XW_INVESTOR_WLZH);
        List<TradeInfo> list = new ArrayList<>();
        if (xwFundAccount != null) {
            list = this.investUserService.getTransactionList("" + xwFundAccount.getId(), tradeForm, bounds);
        }
        for (TradeInfo tradeInfo : list) {
            if (tradeInfo.getStartTime() != null) {
                tradeInfo.setStartTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tradeInfo.getStartTime()));
            }
            if(tradeInfo.getEndTime() != null) {
                tradeInfo.setEndTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tradeInfo.getEndTime()));
            }
        }
        String headers[] = {"创建时间", "交易类型", "交易金额" ,"账户余额（元）", "备注", "订单状态", "订单号", "完成时间"};
        String fieldNames[] = {"startTimeStr", "orderTypeName", "amount", "balance", "remark", "orderStatusName", "orderNum", "endTimeStr"};
        POIUtil.export(response, headers, fieldNames, list);
    }

}
