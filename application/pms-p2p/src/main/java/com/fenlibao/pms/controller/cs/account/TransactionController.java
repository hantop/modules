package com.fenlibao.pms.controller.cs.account;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.cs.account.ReawrdRecord;
import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlan;
import com.fenlibao.model.pms.da.cs.account.vo.UserInvestPlanBid;
import com.fenlibao.model.pms.da.cs.form.InvestPlanForm;
import com.fenlibao.model.pms.da.cs.form.TransactionForm;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.cs.account.ReawrdRecordService;
import com.fenlibao.service.pms.da.cs.account.TransactionService;
import com.fenlibao.service.pms.da.cs.account.UserDetailInfoService;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by Bogle on 2015/12/22.
 */
@RestController
@RequestMapping("transaction")
public class TransactionController {
	private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
	private TransactionService transactionService;
	@Resource
	private UserDetailInfoService userDetailInfoService;
	@Resource
	private ReawrdRecordService reawrdRecordService;
	@Autowired
	private WebApplicationContext wac;

	@RequestMapping(value = "/reawrd/list", method = RequestMethod.GET)
	public @ResponseBody Object reawrdList(@Valid TransactionForm transaction, BindingResult errorResult,
			@RequestParam(value = "pageNum", required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		JSONObject result = new JSONObject();
		if (errorResult.hasErrors()) {
			result.put("errors", errorResult.getAllErrors());
			return result;
		}
		RowBounds bounds = new RowBounds(page + 1, limit);
		if (transaction.getEndTime() != null) {
			Date endTime = transaction.getEndTime();
			endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
			transaction.setEndTime(endTime);
		}
		// 分开类型，根据不同类型查询 0：返现卷 1：现金 2：体验金 3：加息券
		String awardType = transaction.getAwardType();
		if ("0".equals(awardType)) {
			List<UserRedpackets> list = reawrdRecordService.getUserRedpackets(transaction, bounds);
			String fromStatus = transaction.getCashBackStatus();
			if ("0".equals(fromStatus) || "3".equals(fromStatus)) {
				for (UserRedpackets userRed : list) {
					Byte status = userRed.getStatus();
					Date validDate = userRed.getValidTime();
					int dateCompare = DateUtil.getDayBetweenDates(validDate, new Date());
					if (dateCompare > 0) { // 还没有到期
						if (status == 1) {
							userRed.setStatus(Byte.valueOf("3"));
						}
					}
				}
			}
			PageInfo<UserRedpackets> paginator = new PageInfo<>(list);
			return paginator;
		} else if ("1".equals(awardType)) {
			List<ReawrdRecord> list = reawrdRecordService.getCashRedPackets(transaction, bounds);
			PageInfo<ReawrdRecord> paginator = new PageInfo<>(list);
			return paginator;
		} else if ("2".equals(awardType)){
			List<ReawrdRecord> list = reawrdRecordService.getUserExperienceGold(transaction, bounds);
			PageInfo<ReawrdRecord> paginator = new PageInfo<>(list);
			return paginator;
		} else {// 加息券
			List<ReawrdRecord> list = reawrdRecordService.getUserRateCoupon(transaction, bounds);
			PageInfo<ReawrdRecord> paginator = new PageInfo<>(list);
			return paginator;
		}
	}

	@RequestMapping(value = "detailInfo", method = RequestMethod.POST)
	public Map<String, Object> detailInfo(String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		UserDetailInfo userDetailInfo = null;
		String resultCode = "0000";
		Subject subject = SecurityUtils.getSubject();
		try {
			if (subject.isPermitted("detailInfo:fullBank")) {
				userDetailInfo = userDetailInfoService.getFullUserDetailInfo(userId);
			}else{
				userDetailInfo = userDetailInfoService.getUserDetailInfo(userId);
			}
			if (userDetailInfo == null) {
				resultCode = "1111";
			}
		} catch (Exception e) {
			LOG.error("用户信息->用户详细信息查询异常：", e);
			resultCode = "2222";
		}
		resultMap.put("userDetailInfo", userDetailInfo);
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	/**
	 * 用户信息分两步  1,先查询具体用户  2,查看对应用户信息
	 * @return
	 */
	@RequestMapping(value = "account-search", method = RequestMethod.GET)
	public ModelAndView index() {
		return new ModelAndView("cs/account/account-search").addObject("transaction", new TransactionForm());
	}
	
    @RequestMapping
    public ModelAndView userDetailList(
    		TransactionForm transactionForm,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
        RowBounds bounds = new RowBounds(page, limit);
        List<UserDetail> userDetailList = new ArrayList<>();
        String msg = null;
        try {
        		if ((transactionForm.getPhoneNum() != null && transactionForm.getPhoneNum() != "")  
		        		|| (transactionForm.getName() != null && transactionForm.getName() != "")
		        		|| (transactionForm.getIdCard() != null && transactionForm.getIdCard() != "")) {
		        	userDetailList = userDetailInfoService.getUserDetail(transactionForm.getPhoneNum().trim(),transactionForm.getName().trim(),
		        			transactionForm.getIdCard().trim(), bounds);
		        	if (userDetailList.size() == 0) {
						msg = "此用户不存在";
					}
				}
		} catch (Exception e) {
			LOG.error("用户信息->用户详细信息查询异常：", e);
			msg = "查询失败，内部错误";
		}
        PageInfo<UserDetail> paginator = new PageInfo<>(userDetailList);
        return new ModelAndView("cs/account/account-search")
                .addObject("userDetailList", userDetailList)
                .addObject("transactionForm", transactionForm)
                .addObject("paginator", paginator)
                .addObject("msg", msg);
    }

	
	@RequestMapping(value = "account-index", method = RequestMethod.GET)
	public ModelAndView accountIndex(String phoneNum) {
		return new ModelAndView("cs/account/account-index").addObject("transaction", new TransactionForm());
	}

	/**
	 * 充值记录
	 * 
	 * @param transaction
	 * @param errorResult
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/recharge/list", method = RequestMethod.GET)
	public @ResponseBody Object rechargeList(@Valid TransactionForm transaction, BindingResult errorResult,
			@RequestParam(value = "pageNum", required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		JSONObject result = new JSONObject();
		if (errorResult.hasErrors()) {
			result.put("errors", errorResult.getAllErrors());
			return result;
		}
		RowBounds bounds = new RowBounds(page + 1, limit);
		if (transaction.getEndTime() != null) {
			Date endTime = transaction.getEndTime();
			endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
			transaction.setEndTime(endTime);
		}
		List<Transaction> list = this.transactionService.findRechargeHistoryByPhone(transaction, bounds);
		PageInfo<Transaction> paginator = new PageInfo<>(list);
		return paginator;
	}

	/**
	 * 交易流水
	 * 
	 * @param transaction
	 * @param errorResult
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/traction/list", method = RequestMethod.GET)
	public @ResponseBody Object traction(@Valid TransactionForm transaction, BindingResult errorResult,
			@RequestParam(value = "pageNum", required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		JSONObject result = new JSONObject();
		if (errorResult.hasErrors()) {
			result.put("errors", errorResult.getAllErrors());
			return result;
		}
		RowBounds bounds = new RowBounds(page + 1, limit);
		if (transaction.getEndTime() != null) {
			Date endTime = transaction.getEndTime();
			endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
			transaction.setEndTime(endTime);
		}
		List<Transaction> list = this.transactionService.findTractionHistoryByPhone(transaction, bounds);
		PageInfo<Transaction> paginator = new PageInfo<>(list);
		return paginator;
	}

	/**
	 * 提现记录
	 * 
	 * @param transaction
	 * @param errorResult
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/withdrawal/list", method = RequestMethod.GET)
	public @ResponseBody Object withdrawal(@Valid TransactionForm transaction, BindingResult errorResult,
			@RequestParam(value = "pageNum", required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		JSONObject result = new JSONObject();
		if (errorResult.hasErrors()) {
			result.put("errors", errorResult.getAllErrors());
			return result;
		}
		RowBounds bounds = new RowBounds(page + 1, limit);
		if (transaction.getEndTime() != null) {
			Date endTime = transaction.getEndTime();
			endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
			transaction.setEndTime(endTime);
		}
		List<Transaction> list = this.transactionService.findwithdrawalHistoryByPhone(transaction, bounds);
		PageInfo<Transaction> paginator = new PageInfo<>(list);
		return paginator;
	}

	/**
	 * 投资记录
	 * 
	 * @param transaction
	 * @param errorResult
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/invest/list", method = RequestMethod.GET)
	public @ResponseBody Object invest(@Valid TransactionForm transaction, BindingResult errorResult,
			@RequestParam(value = "pageNum", required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		JSONObject result = new JSONObject();
		if (errorResult.hasErrors()) {
			result.put("errors", errorResult.getAllErrors());
			return result;
		}
		RowBounds bounds = new RowBounds(page + 1, limit);
		if (transaction.getEndTime() != null) {
			Date endTime = transaction.getEndTime();
			endTime.setTime(endTime.getTime() + (1 * 24 * 60 * 60 * 1000) - 1);
			transaction.setEndTime(endTime);
		}
		List<Transaction> list = new ArrayList<>();
//        Integer userId;
		if (transaction.getSold() == 0) {
//            userId = transactionService.findUserIdByPhone(transaction);
//            transaction.setUserId(userId);
			list = this.transactionService.findInvestHistoryByPhone(transaction, bounds);//买入
			if (list.size() != 0) {
				Transaction transaction2 = list.get(0);
				transaction2.setOrigrienMoneyTotal(this.transactionService.getTotalOrigrienMoney(transaction));
				transaction2.setInvestMoneyTotal(this.transactionService.getTotalInvestMoney(transaction));
			}
		}else {
			list = this.transactionService.findInvestSoldHistoryByPhone(transaction, bounds);//卖出
			if (list.size() != 0) {
				Transaction transaction2 = list.get(0);
				transaction2.setOrigrienMoneyTotal(this.transactionService.getTotalOrigrienMoneyOut(transaction));
				transaction2.setInvestMoneyTotal(this.transactionService.getTotalInvestMoneyOut(transaction));
			}
		}
		PageInfo<Transaction> paginator = new PageInfo<>(list);
		return paginator;
	}

	@RequestMapping(value = "/plan/list", method = RequestMethod.GET)
	public @ResponseBody Object plan(@Valid InvestPlanForm planForm, BindingResult errorResult,
                                     @RequestParam(value = "pageNum", required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
                                     @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		JSONObject result = new JSONObject();
		if (errorResult.hasErrors()) {
			result.put("errors", errorResult.getAllErrors());
			return result;
		}
		RowBounds bounds = new RowBounds(page + 1, limit);
		List<UserInvestPlan> list = this.transactionService.findUserPlan(planForm, bounds);
		PageInfo<UserInvestPlan> paginator = new PageInfo<>(list);
		return paginator;
	}

	@RequestMapping(value="/plan-detail")
	public ModelAndView planDetail(String userId, String planType, String planId, String recordId,
								   @RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
								   @RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		InvestPlanForm investPlanForm = new InvestPlanForm();
		investPlanForm.setUserId(Integer.valueOf(userId));
		investPlanForm.setPlanType(Integer.valueOf(planType));
		investPlanForm.setPlanId(Integer.valueOf(planId));
		investPlanForm.setRecordId(Integer.valueOf(recordId));
		RowBounds bounds = new RowBounds(page, limit);
		List<UserInvestPlanBid> list = this.transactionService.findUserPlanBid(investPlanForm, bounds);
		PageInfo<UserInvestPlanBid> paginator = new PageInfo<>(list);
		return new ModelAndView("cs/account/plan-detail").addObject("list", list)
				.addObject("paginator", paginator).addObject("investPlanForm", investPlanForm);
	}
}

