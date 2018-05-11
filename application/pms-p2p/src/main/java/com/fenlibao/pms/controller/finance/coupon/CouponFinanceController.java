package com.fenlibao.pms.controller.finance.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.model.pms.da.finance.form.ReturncachRedpacketDetailForm;
import com.fenlibao.model.pms.da.finance.form.ReturncachRedpacketForm;
import com.fenlibao.model.pms.da.finance.vo.ReturncachRedpacketVO;
import com.fenlibao.model.pms.da.finance.vo.UserUsedReturncachRedpacketVO;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.finance.returncash.ReturncashRedpacketService;

@RestController
@RequestMapping("finance/coupon")
public class CouponFinanceController {

	@Resource
	private ReturncashRedpacketService returncashRedpacketService;

	@RequiresPermissions("returncashDetails:export")
	@RequestMapping(value = "detail/export", method = RequestMethod.POST)
	public void returncashDetailExport(HttpServletResponse response,
			ReturncachRedpacketDetailForm returncachRedpacketDetailForm) {
		RowBounds bounds = new RowBounds();
		Date startDate = DateUtil.StringToDate(returncachRedpacketDetailForm.getStartDate() + " 00:00:00",
				DateUtil.DATA_TIME_FORMAT);
		Date endDate = DateUtil.StringToDate(returncachRedpacketDetailForm.getEndDate() + " 23:59:59",
				DateUtil.DATA_TIME_FORMAT);
		Integer redpacketId = returncachRedpacketDetailForm.getRedpacketId();
		String phoneNum = returncachRedpacketDetailForm.getPhoneNum();
		boolean systemgrantFlag = returncachRedpacketDetailForm.getSystemgrantFlag();
		List<UserUsedReturncachRedpacketVO> list = returncashRedpacketService
				.findUserUsedReturncachRedpackets(startDate, endDate, redpacketId, systemgrantFlag, phoneNum, bounds);
		List<ReturncachDetailExportObject> exports = new ArrayList<>();
		ReturncachDetailExportObject exportObject;
		for (UserUsedReturncachRedpacketVO vo : list) {
			exportObject = new ReturncachDetailExportObject();
			exportObject.setActivityTime(DateUtil.getDateTime(vo.getActivityTime()));
			exportObject.setPhoneNum(vo.getPhoneNum());
			exportObject.setRedMoney(vo.getRedMoney());
			exports.add(exportObject);
		}
		String headers[] = { "激活时间", "用户手机号", "返现券金额" };
		String fieldNames[] = { "activityTime", "phoneNum", "redMoney" };
		POIUtil.export(response, headers, fieldNames, exports);
	}

	@RequiresPermissions("returncash:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public void returncashListExport(HttpServletResponse response, ReturncachRedpacketForm returncachRedpacketForm) {
		RowBounds bounds = new RowBounds();
		Date startDate = null;
		Date endDate = null;
		String startTimeStr = returncachRedpacketForm.getStartDate();
		String endTimeStr = returncachRedpacketForm.getEndDate();
		boolean systemgrantFlag = returncachRedpacketForm.getSystemgrantFlag();
		String activityCode = returncachRedpacketForm.getActivityCode();
		List<ReturncachRedpacketVO> list;
		// 返现券总成本
		BigDecimal totalCost = BigDecimal.ZERO;
		// 总激活数量(统计用户已经使用的)
		Integer totalActiveCount;
		// 总发送数量(所有发送出去的返现券)
		int totalRedNumber = 0;
		ReturncachRedpacketVO totalCostVO = new ReturncachRedpacketVO();
		if (!StringUtils.isEmpty(startTimeStr)) {
			startDate = DateUtil.StringToDate(startTimeStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		}
		if (!StringUtils.isEmpty(endTimeStr)) {
			endDate = DateUtil.StringToDate(endTimeStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		}
		// list =
		// returncashRedpacketService.findUsedReturncachRedpackets(startDate,
		// endDate, activityCode,
		// systemgrantFlag, bounds);
		// totalCost = returncashRedpacketService.findUsedTotalCost(startDate,
		// endDate, activityCode, systemgrantFlag);
		totalActiveCount = this.returncashRedpacketService.findTotlActiveCount(startDate, endDate, activityCode,
				systemgrantFlag);
//		totalRedNumber = this.returncashRedpacketService.findTotlRedNumber(startDate, endDate, activityCode,
//				systemgrantFlag);
		list = returncashRedpacketService.findUsedReturncachRedpackets(startDate, endDate, activityCode,
				systemgrantFlag, bounds);
		for (ReturncachRedpacketVO returncachRedpacketVO : list) { // 查询对应的返现券的发送数量
			Integer eachRedPacketSendAmount = this.returncashRedpacketService.getEachRedPacketSendAmount(startDate,
					endDate, returncachRedpacketVO.getActivityCode(), returncachRedpacketVO.getRedpacketId(),
					systemgrantFlag);
			returncachRedpacketVO.setRedNumber(eachRedPacketSendAmount);
			totalRedNumber += eachRedPacketSendAmount;//直接统计
		}
		for (ReturncachRedpacketVO vo : list) {
			vo.setActivityCode(StringUtils.isEmpty(vo.getActivityCode()) ? "系统自动发放" : vo.getActivityCode());
			totalCost = totalCost.add(vo.getRedMoneySum());
		}
		// 返现券总成本
		totalCostVO.setActivityCode("总成本");
		totalCostVO.setInvestMoney(totalCost);
		list.add(totalCostVO);
		String headers[] = { "返现券代码", "来源", "返现券门槛", "返现券金额", "返现券有效期", "发送数量", "激活数量", "产生成本" };
		String fieldNames[] = { "activityCode", "remarks", "investMoney", "redMoney", "effectDay", "redNumber",
				"activeCount", "redMoneySum" };
		POIUtil.export(response, headers, fieldNames, list);
	}

	@RequiresPermissions("returncash:details")
	@RequestMapping(value = "detail")
	public ModelAndView detail(ReturncachRedpacketDetailForm returncachRedpacketDetailForm,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		ModelAndView mav = new ModelAndView("finance/coupon/detail");
		RowBounds bounds = new RowBounds(page, limit);
		Date startDate = DateUtil.StringToDate(returncachRedpacketDetailForm.getStartDate() + " 00:00:00",
				DateUtil.DATA_TIME_FORMAT);
		Date endDate = DateUtil.StringToDate(returncachRedpacketDetailForm.getEndDate() + " 23:59:59",
				DateUtil.DATA_TIME_FORMAT);
		Integer redpacketId = returncachRedpacketDetailForm.getRedpacketId();
		String phoneNum = returncachRedpacketDetailForm.getPhoneNum();
		boolean systemgrantFlag = returncachRedpacketDetailForm.getSystemgrantFlag();
		List<UserUsedReturncachRedpacketVO> list = returncashRedpacketService
				.findUserUsedReturncachRedpackets(startDate, endDate, redpacketId, systemgrantFlag, phoneNum, bounds);
		PageInfo<UserUsedReturncachRedpacketVO> paginator = new PageInfo<>(list);
		mav.addObject("list", list);
		mav.addObject("paginator", paginator);
		mav.addObject("datailForm", returncachRedpacketDetailForm);
		return mav;
	}

	@RequiresPermissions("returncash:view")
	@RequestMapping
	public ModelAndView list(@Valid ReturncachRedpacketForm returncachRedpacketForm, BindingResult bindingResult,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		ModelAndView mav = new ModelAndView("finance/coupon/index");
		RowBounds bounds = new RowBounds(page, limit);
		if (bindingResult.hasErrors()) {
			mav.addObject("errors", bindingResult.getAllErrors());
		} else {
			Date startDate;
			Date endDate;
			String startTimeStr = returncachRedpacketForm.getStartDate();
			String endTimeStr = returncachRedpacketForm.getEndDate();
			boolean systemgrantFlag = returncachRedpacketForm.getSystemgrantFlag();
			String activityCode = returncachRedpacketForm.getActivityCode();
			List<ReturncachRedpacketVO> list;
			// 返现券总成本
			BigDecimal totalCost;
			// 总激活数量(统计用户已经使用的)
			Integer totalActiveCount;
			// 总发送数量(所有发送出去的返现券)
			int totalRedNumber = 0;
			if (StringUtils.isEmpty(startTimeStr)) {
				// 上个月第一天
				/*Calendar provMonthFirstDayCalendar = Calendar.getInstance();
				provMonthFirstDayCalendar.add(Calendar.MONTH, -1);
				provMonthFirstDayCalendar.set(Calendar.DATE, 1);
				String provMonthFirstDayStr = DateUtil.getDate(provMonthFirstDayCalendar.getTime());*/
				//昨天
				String provMonthFirstDayStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));//
				returncachRedpacketForm.setStartDate(provMonthFirstDayStr);
				startDate = DateUtil.StringToDate(provMonthFirstDayStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			} else {
				startDate = DateUtil.StringToDate(startTimeStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			}
			if (StringUtils.isEmpty(endTimeStr)) {
				// 上个月最后一天
				/*Calendar provMonthLastDayCalendar = Calendar.getInstance();
				provMonthLastDayCalendar.add(Calendar.MONTH, -1);
				provMonthLastDayCalendar.set(Calendar.DATE,
						provMonthLastDayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				String provMonthLastDayStr = DateUtil.getDate(provMonthLastDayCalendar.getTime());*/
				//昨天
				String provMonthLastDayStr = DateUtil.getDate(DateUtil.dateAdd(DateUtil.nowDate(), -1));
				returncachRedpacketForm.setEndDate(provMonthLastDayStr);
				endDate = DateUtil.StringToDate(provMonthLastDayStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			} else {
				endDate = DateUtil.StringToDate(endTimeStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
			list = returncashRedpacketService.findUsedReturncachRedpackets(startDate, endDate, activityCode,
					systemgrantFlag, bounds);
			for (ReturncachRedpacketVO returncachRedpacketVO : list) { // 查询对应的返现券的发送数量
				Integer eachRedPacketSendAmount = this.returncashRedpacketService.getEachRedPacketSendAmount(startDate,
						endDate, returncachRedpacketVO.getActivityCode(), returncachRedpacketVO.getRedpacketId(),
						systemgrantFlag);
				returncachRedpacketVO.setRedNumber(eachRedPacketSendAmount);
				totalRedNumber += eachRedPacketSendAmount;//直接统计
			}
			totalCost = returncashRedpacketService.findUsedTotalCost(startDate, endDate, activityCode, systemgrantFlag);
			totalActiveCount = this.returncashRedpacketService.findTotlActiveCount(startDate, endDate, activityCode,
					systemgrantFlag);
//			totalRedNumber = this.returncashRedpacketService.findTotlRedNumber(startDate, endDate, activityCode,
//					systemgrantFlag);
//			
			PageInfo<ReturncachRedpacketVO> paginator = new PageInfo<>(list);
			mav.addObject("totalCost", totalCost);
			mav.addObject("totalActiveCount", totalActiveCount);
			mav.addObject("totalRedNumber", totalRedNumber);
			mav.addObject("list", list);
			mav.addObject("paginator", paginator);
			mav.addObject("returncachRedpacketForm", returncachRedpacketForm);
		}
		return mav;
	}

	private class ReturncachDetailExportObject {
		private String activityTime;// 激活时间
		private String phoneNum;// 手机号
		private BigDecimal redMoney;// 返现券金额

		public String getActivityTime() {
			return activityTime;
		}

		public void setActivityTime(String activityTime) {
			this.activityTime = activityTime;
		}

		public String getPhoneNum() {
			return phoneNum;
		}

		public void setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
		}

		public BigDecimal getRedMoney() {
			return redMoney;
		}

		public void setRedMoney(BigDecimal redMoney) {
			this.redMoney = redMoney;
		}
	}

}
