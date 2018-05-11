package com.fenlibao.p2p.controller.v_4.v_4_0_0.user;

import com.fenlibao.p2p.model.consts.user.AutoBidConst;
import com.fenlibao.p2p.model.consts.user.BidTime;
import com.fenlibao.p2p.model.entity.user.UserAutobidSetting;
import com.fenlibao.p2p.model.form.user.AutobidSettingForm;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestFormExtend;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.user.AutobidSettingDetailVO;
import com.fenlibao.p2p.model.vo.user.AutobidSettingVO;
import com.fenlibao.p2p.service.user.AutoBidService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fenlibao.p2p.model.consts.user.AutoBidConst.RESERVE_LIMITED;

@RestController("v_4_0_0/UserAutoBidController")
@RequestMapping(value = "autobid", headers = APIVersion.v_4_0_0)
public class UserAutoBidController {

	private static final Logger logger=LogManager.getLogger(UserAutoBidController.class);

	@Resource
	private AutoBidService autoBidService;


	/**
	 *  获取自动投标-参数信息
	 * @return
	 */
	@RequestMapping(value = "settingInfo", method = RequestMethod.GET)
	public HttpResponse getAutobidSettingInfo(@ModelAttribute BaseRequestFormExtend formExtend) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()) {
			logger.debug("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		}
		Map<String, Object> data = new HashMap<>();
		List<BigDecimal> interestRates = autoBidService.getInterestRates();
		List<BidTime> bidTimes = autoBidService.getBidTime();

		data.put("interestRate",interestRates);
		data.put("timeMin", bidTimes);
		data.put("timeMax", bidTimes);
		response.setData(data);

		return response;
	}

	/**
	 *	自动投标-保存设置
	 * @param startTime
	 * @param endTime
	 * @param bidType
	 * @param repaymentMode
	 * @return
	 */
	@RequestMapping(value = "saveSettingInfo", method = RequestMethod.POST)
	public HttpResponse saveAutobidSettingInfo(@ModelAttribute BaseRequestFormExtend formExtend,
											   @RequestParam(required = false, value="startTime") String startTime,
											   @RequestParam(required = false, value="endTime") String endTime,
											   @RequestParam(required = false, value="bidType") String bidType,
											   @RequestParam(required = false, value="repaymentMode") String repaymentMode,
											   @RequestParam(required = false, value="reserve") String reserve,
											   @RequestParam(required = false, value="settingId") String settingId,
											   AutobidSettingForm autobidSettingForm) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()) {
			logger.debug("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		}
		if (autobidSettingForm != null) {
			if (!autoBidService.checkBidTimeCorrect(autobidSettingForm.getTimeMin(), autobidSettingForm.getMinMark(), autobidSettingForm.getTimeMax(), autobidSettingForm.getMaxMark())) {
				response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
				return response;
			}

			//封装UserAutobidSetting
			UserAutobidSetting userAutobidSetting = new UserAutobidSetting();
			userAutobidSetting.setUserId(Integer.valueOf(formExtend.getUserId()));
			//启用和结束期限
			Date start = null;
			Date end = null;
			if(StringUtils.isNotBlank(startTime)){

				start = new Date( Long.valueOf(startTime));
			}else {
				start = new Date();
			}
			userAutobidSetting.setStartTime(start);

			if(StringUtils.isNotBlank(endTime)){
				if (Long.valueOf(startTime) > Long.valueOf(endTime) ||  Long.valueOf(endTime)-Long.valueOf(startTime) > 31536000000L) {
					response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
					return response;
				}
				end = DateUtil.handleTime(Long.valueOf(startTime), Long.valueOf(endTime));
				userAutobidSetting.setEndTime(end);
			}

			if(AutoBidConst.verifyInterestRate(new BigDecimal(autobidSettingForm.getInterestRate()))){//校验是否合法的年率

				userAutobidSetting.setInterestRate(new BigDecimal(autobidSettingForm.getInterestRate()));
			}else {
				response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
				return response;
			}

			/**
			 * 前台参数为：不限记录为0天  10天 20天  3个月
			 */
			userAutobidSetting.setTimeMin(Integer.valueOf(autobidSettingForm.getTimeMin()));
			userAutobidSetting.setMinMark(autobidSettingForm.getMinMark());
			userAutobidSetting.setTimeMax(Integer.valueOf(autobidSettingForm.getTimeMax()));
			userAutobidSetting.setMaxMark(autobidSettingForm.getMaxMark());
			userAutobidSetting.setValidityMod(autobidSettingForm.getValidityMod());

			if (StringUtils.isNotBlank(reserve)) {
				BigDecimal reserveNum = new BigDecimal(reserve);
				if (1 == reserveNum.compareTo(RESERVE_LIMITED)) {
					response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
					return response;
				}
				userAutobidSetting.setReserve(reserveNum);
			}

			if(StringUtils.isBlank(bidType)){
				userAutobidSetting.setBidType(AutoBidConst.DEFAULT_BID_TYPE);
			}else{
				userAutobidSetting.setBidType(bidType);
			}
			if(StringUtils.isBlank(repaymentMode)){
				userAutobidSetting.setRepaymentMode(AutoBidConst.DEFAULT_REPAYMENT_MODE);
			}else{
				userAutobidSetting.setRepaymentMode(repaymentMode);
			}

			if (StringUtils.isNotBlank(settingId)) {

				autoBidService.updateAutobidSetting(userAutobidSetting,Integer.valueOf(settingId));
			}else{
				boolean havingOtherSet = autoBidService.selectSettingNumByUserId(userAutobidSetting.getUserId(),AutoBidConst.NO_DELETE_FLAG);
				if (havingOtherSet) {
					response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				}else{
					autoBidService.insertAutoBidSetting(userAutobidSetting);
				}
			}

		}else {
				response.setCodeMessage(ResponseCode.FAILURE);
			}

		return response;
	}

	/**
	 * 自动投标-开关
	 * @param settingId
	 * @return
	 */
	@RequestMapping(value = "activeSetting", method = RequestMethod.GET)
	public HttpResponse activeSetting(@ModelAttribute BaseRequestFormExtend formExtend,
									  @RequestParam(required = false, value = "settingId") String settingId) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()||StringUtils.isBlank(settingId)) {
			logger.debug("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		}

		autoBidService.updateAutobidSetting(formExtend.getUserId().toString(), settingId);



		return response;
	}

	/**
	 * 自动投标-用户设置列表
	 * @return
	 */
	@RequestMapping(value = "settingInfo/list", method = RequestMethod.GET)
	public HttpResponse getAutobidSettingList(@ModelAttribute BaseRequestFormExtend formExtend) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()) {
			logger.debug("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		}
		Map<String, Object> data = new HashMap<>();

		List<AutobidSettingVO> settingList = autoBidService.getAutobidSettingList(formExtend.getUserId().toString());
		data.put("items", settingList);
		response.setData(data);

		return response;
	}

	/**
	 * 自动投标-用户设置详情
	 * @param settingId
	 * @return
	 */
	@RequestMapping(value = "getSettingInfo", method = RequestMethod.GET)
	public HttpResponse getAutobidSettingInfo(@ModelAttribute BaseRequestFormExtend formExtend,
											  @RequestParam(required = true, value = "settingId") String settingId) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()) {
			logger.debug("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		}
			Map<String, Object> data = new HashMap<>();

			AutobidSettingDetailVO autobidSettingVO = autoBidService.getAutobidSettingDetail(formExtend.getUserId().toString(),settingId);
			data.put("items", autobidSettingVO);
			response.setData(data);

		return response;
	}

	/**
	 *  自动投标-用户设置删除
	 * @param settingId
	 * @return
	 */
	@RequestMapping(value = "deleteSetting", method = RequestMethod.GET)
	public HttpResponse deleteAutobidSettingInfo(@ModelAttribute BaseRequestFormExtend formExtend,
												 @RequestParam(required = true, value = "settingId") String settingId) {
		HttpResponse response = new HttpResponse();

		if (!formExtend.validate()) {
			logger.debug("request param is empty");
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
		}
		//settingId判断非空
		autoBidService.deleteAutobidSetting(formExtend.getUserId().toString(), settingId);

		return response;
	}
}

