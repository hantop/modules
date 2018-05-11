package com.fenlibao.pms.controller.reward.cashRedPacket;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.common.pms.util.constant.Pagination;
import com.fenlibao.common.pms.util.exception.ImportExcelException;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.common.pms.util.tool.POIUtil;
import com.fenlibao.common.pms.util.tool.StringHelper;
import com.fenlibao.model.pms.da.bidType.BidType;
import com.fenlibao.model.pms.da.global.FeeCode;
import com.fenlibao.model.pms.da.reward.*;
import com.fenlibao.model.pms.da.reward.form.UserRedpacketsForm;
import com.fenlibao.p2p.common.util.pagination.PageInfo;
import com.fenlibao.service.pms.da.bidtype.BidTypeService;
import com.fenlibao.service.pms.da.exception.ExcelException;
import com.fenlibao.service.pms.da.exception.GrantCashRedPacketException;
import com.fenlibao.service.pms.da.reward.cashRedPacket.RedPacketService;
import com.fenlibao.service.pms.da.reward.cashRedPacket.UserRedpacketsService;
import com.fenlibao.service.pms.da.reward.common.RewardService;
import com.fenlibao.service.pms.da.reward.common.async.RewardAsyncService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bogle on 2015/11/26.
 */
@Controller
@RequestMapping("reward/red-packet")
public class RedPacketController {

	private static final Logger LOG = LoggerFactory.getLogger(RedPacketController.class);

//	private static int CASH_BBACK_COUPON = 1;// 返现券
	private static int REWARD_TYPE = 3;// 导入记录返现券
	private static int GRANT_STATUS = 0;// 发放状态

	@Autowired
	private RedPacketService redPacketService;

	@Autowired
	private UserRedpacketsService userRedpacketsService;

	@Autowired
	private RewardService rewardService;

	@Autowired
	private BidTypeService bidTypeService;

	@Autowired
	private RewardAsyncService asyncService;

	/**
	 * 返现券管理首页
	 *
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequiresPermissions("cashBackVoucher:view")
	@RequestMapping("/back-voucher-list")
	public ModelAndView backVoucher(RewardRecordCondition rewardRecordCondition,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		rewardRecordCondition.setRewardType((byte) REWARD_TYPE);
		RowBounds bounds = new RowBounds(page, limit);
		// 默认当天
		String nowDateStr = DateUtil.getDate(DateUtil.nowDate());
		if (StringHelper.isNull(rewardRecordCondition.getStartDate())) {
			rewardRecordCondition.setStartDate(nowDateStr);
		}
		if (StringHelper.isNull(rewardRecordCondition.getEndDate())) {
			rewardRecordCondition.setEndDate(nowDateStr);
		}
		List<RewardRecord> list = rewardService.getRewardRecords(rewardRecordCondition, bounds);
		//查询当前是否有在发放的红包
		Integer running = userRedpacketsService.getInServiceRedpackets();
		PageInfo<RewardRecord> paginator = new PageInfo<>(list);
		return new ModelAndView("reward/cashRedPacket/back-voucher-list").addObject("list", list).addObject("running",running)
				.addObject("paginator", paginator).addObject("rewardRecordCondition", rewardRecordCondition);
	}

	/**
	 * todo 异步发放返现红包 @jing 20160617
	 * @return
     */
	@RequestMapping(value="/async-grant", method = RequestMethod.POST)
	public @ResponseBody Object asyncGrantBackVoucher(@RequestBody RewardRecord rewardRecord){
		boolean dispose = true;
		UserRedpacketsForm userRedpacketsForm = new UserRedpacketsForm();
		// 交易类型-返现券
		userRedpacketsForm.setTradeType(FeeCode.REGISTERRETURNCACH_REDPACKET);
		// 2:注册现金红包;2:生日;3:充值;4:投资倍数;5:投资额度
		userRedpacketsForm.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金，2：现金红包，3：返现券)
		userRedpacketsForm.setGrantStatus((byte) GRANT_STATUS);// 发放的状态
		String message = "返现券正在发放，请注意发放状态";

		synchronized (this){
			//查询当前是否有在发放的红包
			Integer running = userRedpacketsService.getInServiceRedpackets();
			if(running != null && running > 0){
				dispose = false;
				message = "返现券后台发放中，请等待发送完成后再继续发放！！！";
			}

			if (dispose){
				//查询是否已经发放过了
				Byte granted = userRedpacketsService.selectGrantedById(rewardRecord.getId());
				if (granted != null && granted == 0) {
					//将发放状态修改为发放中...
					RewardRecord doInService = new RewardRecord();
					doInService.setId(rewardRecord.getId());
					doInService.setInService((byte) 1);
					userRedpacketsService.updateRewardRecord(doInService);
				}else {
					dispose = false;
					message = "该返现券已经发放";
				}
			}








		}

		//异步的发放红包操作
		if(dispose){
			rewardRecord = rewardService.getRewardRecordById(rewardRecord);
			asyncService.multiThreadGrantRedPacket(rewardRecord, userRedpacketsForm);
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", message);
		return jsonObject;
	}

	@RequiresPermissions("cashBackVoucher:grant")
	@RequestMapping("/back-voucher-grant")
	public @ResponseBody Object grantBackVoucher(@RequestBody RewardRecord rewardRecord) {
		UserRedpacketsForm userRedpacketsForm = new UserRedpacketsForm();
		// 交易类型-返现券
		userRedpacketsForm.setTradeType(FeeCode.REGISTERRETURNCACH_REDPACKET);
		userRedpacketsForm.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金，2：现金红包，3：返现券)
		userRedpacketsForm.setGrantStatus((byte) GRANT_STATUS);// 发放的状态
		String message = "发送完成";
		try {
			Byte granted = userRedpacketsService.selectGrantedById(rewardRecord.getId());
			/*
			 * RewardRecord RewardRecordDb =
			 * userRedpacketsService.getRewardRecordById(rewardRecord); Byte
			 * granted = RewardRecordDb.getGranted(); Integer grantCount =
			 * RewardRecordDb.getGrantCount(); BigDecimal grantSumDb =
			 * RewardRecordDb.getGrantSum();
			 */

			if (granted != null && granted == 0) {
				List<UserRedpackets> userRedpacketsList = userRedpacketsService.findUserRedpacketsAll(rewardRecord, userRedpacketsForm);

				synchronized (this) {
					/* 返现卷发放的次数限制 add Jing 20160420 */
					int size = userRedpacketsList.size(); // 全部的数量
					int batchNum = Integer.valueOf(Config.get("batch.processing.amount")); // 每次默认执行500
																							// 个
					int count = size % batchNum > 0 ? size / batchNum + 1 : size / batchNum; // 要发起请求的次数
					int fromIndex = 0;
					int toIndex = 0;
					boolean status = false;
					BigDecimal grantSum = new BigDecimal(0);
					int doBatch = 0; // 操作成功的条数
					for (int i = 0; i < count; i++) {
						fromIndex = i * batchNum;
						if (i == count - 1 && (size % batchNum != 0)) {
							batchNum = size % batchNum;
							toIndex = size;

						} else {
							toIndex = (i + 1) * batchNum;
						}
						LOG.info("fromIndex: " + fromIndex + " toIndex: " + toIndex + " batchNum: " + batchNum + " i: "
								+ (i + 1));
						try {
							RewardRecord tmpRewardRecord = userRedpacketsService.grantBackVoucherResult(rewardRecord,
									userRedpacketsList.subList(fromIndex, toIndex));
							if (tmpRewardRecord != null) {
								// grantSum =
								// grantSum.add(tmpRewardRecord.getGrantSum());
								doBatch += batchNum;
							}
						} catch (Exception e) {
							e.printStackTrace();
							LOG.error(e.getMessage());
						}
					}

					RewardRecord doRewardRecord = new RewardRecord();
					// 全部成功才更新表的状态
					if (size == doBatch) {
						doRewardRecord.setId(rewardRecord.getId());
						doRewardRecord.setGranted((byte) 1);
						doRewardRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
						userRedpacketsService.updateRewardRecord(doRewardRecord);
						// 一次全部更新成功
						status = true;
					}

					/*
					 * doRewardRecord.setId(rewardRecord.getId());
					 * 
					 * int rowsBatch = doBatch; if(grantCount != null &&
					 * grantCount > 0){ rowsBatch += grantCount; } if(grantSumDb
					 * != null && grantSumDb.compareTo(new BigDecimal(0)) > 0){
					 * grantSum = grantSum.add(grantSumDb); }
					 * doRewardRecord.setGrantCount(rowsBatch);
					 * doRewardRecord.setGrantSum(grantSum);
					 */

					if (status) {
						message = "发送成功";
					} else {
						message = "部分发送成功，请在执行发送";
					}
				}
			} else {
				message = "已发放过，不可重复发放";
			}

		} catch (Exception e) {
			message = "发送失败";
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", message);
		return jsonObject;
	}

	/**
	 * 发放返现券详情记录表
	 *
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequiresPermissions("cashBackVoucherDetail:view")
	@RequestMapping("/back-voucher-detial-list")
	public ModelAndView backVoucherDetialList(UserRedpacketsForm userRedpackets,
			@RequestParam(required = false, defaultValue = "false") boolean granted,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		RowBounds bounds = new RowBounds(page, limit);
		// 交易类型-返现券
		userRedpackets.setTradeType(FeeCode.REGISTERRETURNCACH_REDPACKET);
		userRedpackets.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金，2：现金红包，3：返现券)
		List<UserRedpackets> list = userRedpacketsService.findPager(userRedpackets, bounds);
		PageInfo<UserRedpackets> paginator = new PageInfo<>(list);
		List<BackVoucherGrantStatistics> statisticsList = null;
		if (granted) {
			statisticsList = userRedpacketsService.backVoucherGrantStatistics(userRedpackets.getGrantId());
		}
		return findAllBackVoucher(new ModelAndView("reward/cashRedPacket/back-voucher-detial-list")
				.addObject("list", list).addObject("paginator", paginator).addObject("userRedpackets", userRedpackets),
				userRedpackets).addObject("statisticsList", statisticsList).addObject("granted", granted);
	}

	@RequiresPermissions("cashBackVoucherDetail:export")
	@RequestMapping("/back-voucher-detial-export")
	public void backVoucherDetialExport(UserRedpacketsForm userRedpackets, HttpServletResponse response) {
//		userRedpackets.setRedType((byte) CASH_BBACK_COUPON); // 红包活动类型,1:注册返金红包
//																// 2:注册现金红包;2:生日;3:充值;4:投资倍数;5:投资额度
		// 交易类型-返现券
		userRedpackets.setTradeType(FeeCode.REGISTERRETURNCACH_REDPACKET);
		userRedpackets.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金，2：现金红包，3：返现券)
		List<UserRedpackets> list = userRedpacketsService.findAllReport(userRedpackets);
		POIUtil.export(response, new String[] { "发送手机号", "返现券类型", "发送状态" },
				new String[] { "phone", "activityCode", "grantStatusName" }, list, "返现券发送详情");
	}

	/**
	 * 查询获取指定记录的所有类型编码
	 *
	 * @param modelAndView
	 * @param userRedpackets
	 * @return
	 */
	private ModelAndView findAllBackVoucher(ModelAndView modelAndView, UserRedpacketsForm userRedpackets) {
		List<UserRedpackets> activityCodes = userRedpacketsService.findAllActivityCode(userRedpackets);
		modelAndView.addObject("activityCodes", activityCodes);
		return modelAndView;
	}

	/**
	 * 导入返现券列表信息
	 *
	 * @param rewardRecord
	 * @param file
	 * @return
	 */
	@RequiresPermissions("cashBackVoucher:import")
	@RequestMapping(value = "/import-back-voucher", method = RequestMethod.POST)
	public @ResponseBody Object importBackVoucher(RewardRecord rewardRecord, @RequestParam("file") MultipartFile file)
			throws Throwable {
		String operator = rewardRecord.getOperator();
		String headers[] = { "发送手机号", "返现券类型" };
		List<String[]> list = null;
		List<String> result = new ArrayList<>();
		try {
			list = POIUtil.getImportedData(file, headers);
		} catch (ImportExcelException e) {
			result.add(e.getMessage());
			return result;
		}
		String grantName = file.getOriginalFilename();
		if (grantName.indexOf(".") > 0) {
			grantName = grantName.substring(0, grantName.lastIndexOf("."));
		}
		// 开始插入记录
		RewardRecord newRecord = new RewardRecord();
		newRecord.setGrantName(grantName);
		newRecord.setOperator(operator);
		newRecord.setGranted((byte) 0);// 未发放
		newRecord.setRewardType((byte) REWARD_TYPE);// 体验金

		try {
			result = userRedpacketsService.insertSelective(newRecord, list);
		} catch (ExcelException e) {
			return e.getErrors();
		}
		return result;
	}

	/********************************************************
	 * 返现券类型 增删改查
	 ************************************************/
	@RequiresPermissions("cashBackVoucherEdit:view")
	@RequestMapping("/back-voucher-type-list")
	public ModelAndView backVoucherTypeList(RedPacket redPacket,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		RowBounds bounds = new RowBounds(page, limit);
		if (redPacket.getActivityCode() == null || redPacket.getActivityCode().equals("")) {
			redPacket.setActivityCode(null);
		}
		if (redPacket.getRemarks() == null || redPacket.getRemarks().equals("")) {
			redPacket.setRemarks(null);
		}
//		redPacket.setRedType(CASH_BBACK_COUPON);
        // 交易类型-返现券
        redPacket.setTradeType(FeeCode.REGISTERRETURNCACH_REDPACKET);
		List<RedPacket> list = redPacketService.findRedPacketPager(redPacket, bounds);
		for (RedPacket temp : list) {
			List<String> bidTypeNames = this.bidTypeService.getBidTypesByRedPacketId(temp.getId());
			temp.setBidTypeNames(bidTypeNames);
			temp.setBidTypeAlias(StringUtils.strip( bidTypeNames.toString(), "[]"));
			//如果当前的返现券已经发放了,就不能对该返现券进行编辑
			temp.setGranted(this.redPacketService.isGrantRedPacket(temp.getId()));
		}
		PageInfo<RedPacket> paginator = new PageInfo<>(list);
		return new ModelAndView("reward/cashRedPacket/back-voucher-type-list").addObject("list", list)
				.addObject("paginator", paginator).addObject("redPacket", redPacket);
	}

	/**
	 * 进入编辑页面，进行新增或修改操作
	 *
	 * @param id
	 * @return
	 */
	@RequiresPermissions(value = { "cashBackVoucherEdit:create", "cashBackVoucherEdit:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/back-voucher-type-edit", method = RequestMethod.GET)
	public ModelAndView backVoucherTypeEdit(@RequestParam(required = false, defaultValue = "0") int id,
			@RequestParam(required = false) List<Integer> bidTypeIds) {
		// 进入编辑列表界面的时候默认先查询出所有的表的类型供返现券选择 add lee
		List<BidType> bidTypes = this.bidTypeService.getAllUsedBidType();
		RedPacket redPacket = new RedPacket();
		String title = "新增";
		redPacket.setInvestDeadLine(0);// 初始化单选框默认值
		if (id > 0) {
			title = "编辑";
			redPacket = redPacketService.getRedPacketById(id);
			bidTypeIds = this.bidTypeService.getBidTypeIdsByRedPacketId(id);
			redPacket.setBidTypeIds(bidTypeIds);// 回显
			if (redPacket.getInvestDeadLine() != null) {
				redPacket.setInvestDeadLineType(true);
			}
		}else {
            bidTypeIds = new ArrayList<>();
            for (BidType bidType : bidTypes) {
                bidTypeIds.add(bidType.getId());
            }
        }
		redPacket.setBidTypeIds(bidTypeIds);
		return new ModelAndView("reward/cashRedPacket/back-voucher-type-edit").addObject("redPacket", redPacket)
				.addObject("bidTypes", bidTypes).addObject("title", title);
	}

	/**
	 * 新增或编辑返现券或红包
	 *
	 * @param redPacket
	 * @param result
	 * @return
	 */
	@RequiresPermissions(value = { "cashBackVoucherEdit:create", "cashBackVoucherEdit:edit" }, logical = Logical.OR)
	@RequestMapping(value = "/back-voucher-type-edit", method = RequestMethod.POST)
	public ModelAndView backVoucherTypeEdit(@Valid @ModelAttribute("redPacket") RedPacket redPacket,
			BindingResult result) {
		String title = "新增";
		if (redPacket.getId() > 0) {
			title = "编辑";
		}
		if (result.hasErrors()) {
			LOG.error("提交数据格式有错误");
			return new ModelAndView("reward/cashRedPacket/back-voucher-type-edit").addObject("redPacket", redPacket)
					.addObject("bidTypes", this.bidTypeService.getAllUsedBidType()).addObject("title", title);
		}
		redPacket.setTradeType(FeeCode.REGISTERRETURNCACH_REDPACKET);// 交易类型
		// ===========投资日期限制=========ypb
		if (redPacket.isInvestDeadLineType()) {
			redPacket.setInvestDeadLine(redPacket.getInvestDeadLine());
		} else {
			redPacket.setInvestDeadLine(null);
		}
		// ==========add lee ==========
		int id = redPacketService.saveOrUpdateRedPacket(redPacket);
		String url = "redirect:/reward/red-packet/back-voucher-type-edit?success=" + (id > 0 ? true : false);
		ModelAndView modelAndView = new ModelAndView().addObject("title", title).addObject("redPacket", redPacket)
				.addObject("code", id).addObject("bidTypes", this.bidTypeService.getAllUsedBidType());
		if (id < 0) {
			url = "reward/cashRedPacket/back-voucher-type-edit";
		}
		modelAndView.setViewName(url);
		return modelAndView;
	}

	@RequiresPermissions("cashBackVoucherEdit:delete")
	@RequestMapping(value = "back-voucher-type-remove", method = RequestMethod.DELETE, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity backVoucherTypeRemove(@RequestBody @NotEmpty LinkedList<Integer> redPackets,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		boolean flag = redPacketService.redpacketRemove(redPackets) > 0 ? true : false;
		return new ResponseEntity<>(flag, flag ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	/**
	 * 查询现金红包发放记录
	 *
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequiresPermissions("cashRedPacket:view")
	@RequestMapping("cash-red-packet-index")
	public ModelAndView getCashRedPacketRecords(RewardRecordCondition rewardRecordCondition,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		rewardRecordCondition.setRewardType((byte) 2);
		RowBounds bounds = new RowBounds(page, limit);
		// 默认当天
		String nowDateStr = DateUtil.getDate(DateUtil.nowDate());
		if (StringHelper.isNull(rewardRecordCondition.getStartDate())) {
			rewardRecordCondition.setStartDate(nowDateStr);
		}
		if (StringHelper.isNull(rewardRecordCondition.getEndDate())) {
			rewardRecordCondition.setEndDate(nowDateStr);
		}
		List<RewardRecord> list = rewardService.getRewardRecords(rewardRecordCondition, bounds);
		PageInfo<RewardRecord> paginator = new PageInfo<>(list);
		return new ModelAndView("reward/cashRedPacket/cash-red-packet-index").addObject("list", list)
				.addObject("paginator", paginator).addObject("rewardRecordCondition", rewardRecordCondition);
	}

	@RequiresPermissions("cashRedPacketDetail:view")
	@RequestMapping("cash-red-packet-detail")
	public ModelAndView getCashRedPacketRecordsDetail(UserCashRedPacket userCashRedPacket,
			@RequestParam(required = false, defaultValue = "false") boolean granted,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = Pagination.DEFAULT_LIMIT) int limit) {
		RowBounds bounds = new RowBounds(page, limit);
		if ("".equals(userCashRedPacket.getGrantStatus())) {
			userCashRedPacket.setGrantStatus(null);
		}
		RewardRecord rewardRecord = null;
		if (granted) {
			RewardRecord condition = new RewardRecord();
			condition.setId(userCashRedPacket.getGrantId());
			rewardRecord = rewardService.getRewardRecordById(condition);
		}
		List<UserCashRedPacket> list = userRedpacketsService.getCashRedPacketRecordsDetail(userCashRedPacket, bounds);
		PageInfo<UserCashRedPacket> paginator = new PageInfo<>(list);
		return new ModelAndView("reward/cashRedPacket/cash-red-packet-detail").addObject("list", list)
				.addObject("paginator", paginator).addObject("userCashRedPacket", userCashRedPacket)
				.addObject("granted", granted)
				.addObject("grantCount", rewardRecord != null ? rewardRecord.getGrantCount() : null)
				.addObject("grantSum", rewardRecord != null ? rewardRecord.getGrantSum() : null);
	}

	@RequiresPermissions("cashRedPacketDetail:export")
	@RequestMapping("cash-red-packet-detail-export")
	public void exportCashRedPacketRecordsDetail(UserCashRedPacket userCashRedPacket, HttpServletResponse response) {
		RowBounds bounds = new RowBounds();
		if ("".equals(userCashRedPacket.getGrantStatus())) {
			userCashRedPacket.setGrantStatus(null);
		}
		List<UserCashRedPacket> list = userRedpacketsService.getCashRedPacketRecordsDetail(userCashRedPacket, bounds);
		List<DetailExport> list2 = new ArrayList<>();
		for (UserCashRedPacket item : list) {
			DetailExport d = new DetailExport();
			d.phone = item.getPhone();
			d.money = item.getMoney() != null ? item.getMoney().doubleValue() : null;
			String status = item.getGrantStatus();
			if ("DZZ".equals(status)) {
				d.grantStatus = "未发送";
			} else if ("CG".equals(status)) {
				d.grantStatus = "已成功";
			} else if ("ZF".equals(status)) {
				d.grantStatus = "已作废";
			} else if ("SB".equals(status)) {
				d.grantStatus = "发送失败";
			}
			list2.add(d);
		}
		String headers[] = { "用户手机号", "红包金额", "发送状态" };
		String fieldNames[] = { "phone", "money", "grantStatus" };
		POIUtil.export(response, headers, fieldNames, list2, "现金红包发送详情");
	}

	private class DetailExport {
		String phone;
		Double money;
		String grantStatus;
	}

	/**
	 * 导入现金红包记录
	 *
	 * @param rewardRecord
	 * @param file
	 * @return
	 * @throws Throwable
	 */
	@RequiresPermissions("cashRedPacket:import")
	@RequestMapping(value = "cash-red-packet-import", method = RequestMethod.POST)
	public @ResponseBody Object importCashRedPacketRecords(RewardRecord rewardRecord,
			@RequestParam("file") MultipartFile file) throws Throwable {
		List<String> result = new ArrayList<>();
		String grantName = file.getOriginalFilename();
		if (grantName.indexOf(".") > 0) {
			grantName = grantName.substring(0, grantName.lastIndexOf("."));
		}

		String operator = rewardRecord.getOperator();
		String headers[] = { "系统类型", "发送手机号", "现金红包金额" };
		List<String[]> importList = null;
		try {
			importList = POIUtil.getImportedData(file, headers);
		} catch (ImportExcelException e) {
			String fileError = e.getMessage();
			result.add(fileError);
			return result;
		}
		// 开始插入记录
		RewardRecord newRecord = new RewardRecord();
		newRecord.setGrantName(grantName);
		newRecord.setOperator(operator);
		newRecord.setGranted((byte) 0);// 未发放
		newRecord.setRewardType((byte) 2);// 现金红包
		long miniSecond = System.currentTimeMillis();
		Timestamp datetime = new Timestamp(miniSecond);
		newRecord.setGrantTime(datetime);// 未发送取导入时间
		List<String> contentErrorList = userRedpacketsService.importCashRedPacketRecords(newRecord, importList);
		result.addAll(contentErrorList);
		return result;
	}
	

	@RequiresPermissions("cashRedPacket:grant")
	@RequestMapping("cash-red-packet-grant")
	public @ResponseBody Object grantCashRedPacket(@RequestBody RewardRecord rewardRecord) {
		String message = "现金正在发放中,请注意发放状态!";
		RewardRecord temp = userRedpacketsService.getRewardRecordById(rewardRecord);
		if (temp != null && temp.getInService() == (byte) 1) {
			message = "现金发放中,请勿重新操作!";
		}else if (temp != null && temp.getGranted() == (byte) 1) {
			message = "已发放过，不可重复发放";
		}else {
			try {
				rewardRecord.setInService((byte) 1);
				userRedpacketsService.updateRewardRecord(rewardRecord);
				if(rewardRecord.getSysType() == 1 ){
					synchronized (this) {
						message = userRedpacketsService.grantCashRedPacket(rewardRecord);
					}
				}else if (rewardRecord.getSysType() == 2){
					rewardRecord = rewardService.getRewardRecordById(rewardRecord);
					asyncService.multiThreadGrantCashRedPacket(rewardRecord);
					/*List<UserCashRedPacket> userCashRedPacketList = userRedpacketsService.getCustodyCashRedPacket(rewardRecord);
					if(userCashRedPacketList.size() > 0){
						message = userRedpacketsService.grantCustodyCashRedPacket(rewardRecord, userCashRedPacketList);
					}*/
				}else {
					message = "未知系统值异常!";
				}
			} catch (Exception e) {
				if (e instanceof GrantCashRedPacketException) {
					message = e.getMessage();
					rewardRecord.setInService((byte) 0);
					rewardRecord.setGranted((byte) 0);
					userRedpacketsService.updateRewardRecord(rewardRecord);
				} else {
					message = "发送失败";
				}
				e.printStackTrace();
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", message);
		return jsonObject;
	}

	@RequiresPermissions("cashRedPacket:cancel")
	@RequestMapping("cash-red-packet-cancel")
	public @ResponseBody Object cancelCashRedPacket(@RequestBody RewardRecord rewardRecord) {
		String message = "";
		RewardRecord temp = userRedpacketsService.getRewardRecordById(rewardRecord);
		if (temp != null && temp.getInService() == (byte) 1) {
			message = "现金发放中,不可作废!";
		}else if (temp != null && temp.getGranted() == (byte) 1) {
			message = "已发放过，不可作废！";
		}else {
			try {
				message = userRedpacketsService.cancelCashRedPacket(rewardRecord);
			}catch (GrantCashRedPacketException ge){
				message = ge.getMessage();
			}catch (Exception e) {
				message = "作废失败";
				e.printStackTrace();
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", message);
		return jsonObject;
	}

	@RequiresPermissions("cashBackVoucher:cancel")
	@RequestMapping("back-voucher-cancel")
	public @ResponseBody Object cancelBackVoucher(@RequestBody RewardRecord rewardRecord) {
		UserRedpacketsForm userRedpacketsForm = new UserRedpacketsForm();
		// 交易类型-返现券
		userRedpacketsForm.setTradeType(FeeCode.REGISTERRETURNCACH_REDPACKET);
		userRedpacketsForm.setRewardType((byte) REWARD_TYPE);// 导入时记录，奖励类型(1:体验金，2：现金红包，3：返现券)
		String message = "";
		try {
			message = userRedpacketsService.cancelBackVoucher(rewardRecord, userRedpacketsForm);
		} catch (Exception e) {
			message = "作废失败";
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", message);
		return jsonObject;
	}

}
