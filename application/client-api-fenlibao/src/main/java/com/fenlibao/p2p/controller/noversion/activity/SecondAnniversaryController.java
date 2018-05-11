package com.fenlibao.p2p.controller.noversion.activity;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.lottery.entity.LotteryActivityInfo;
import com.fenlibao.p2p.model.lottery.entity.LotteryPrizeInfo;
import com.fenlibao.p2p.model.lottery.vo.LotteryPrizeInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.activity.SecondAnniversaryService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.lottery.LotteryActivityService;
import com.fenlibao.p2p.service.lottery.LotteryPrizeService;
import com.fenlibao.p2p.service.mp.MemberPointsService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("secondAnniversary")
public class SecondAnniversaryController {
	/**
	 * 两周年活动
	 */
	private static final Logger logger= LogManager.getLogger(SecondAnniversaryController.class);

	@Resource
	LotteryActivityService lotteryActivityService;

	@Resource
	SecondAnniversaryService secondAnniversaryService;

	@Resource
	LotteryPrizeService lotteryPrizeService;

	@Resource
	UserInfoService userInfoService;

	@Resource
	MemberPointsService memberPointsService;

	@Resource
	BidInfoService bidInfoService;

	/**
	 * 大转盘
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/draw/wheelPrize", method = RequestMethod.POST)
	HttpResponse wheel2017Prize(@RequestParam(required = false, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();

		if(StringUtils.isEmpty(userId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		String activityCode = Config.get("activity.secondAnniversary.luckywheel");

		//抽奖时效
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfoWithCheckTimeFromTActivity(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();

		//检查是否当天已抽奖
		//List<UserLotteryDrawPrizeRecord> userLotteryDrawPrizeRecords = lotteryPrizeService.getUserThisDayDrawPrizes(Integer.valueOf(userId),activityId,DateUtil.nowDate());
		//if(userLotteryDrawPrizeRecords != null && userLotteryDrawPrizeRecords.size() > 0){
		//	response.setCodeMessage(ResponseCode.ACTIVITY_USER_LOTTERY_TIMES_LACK);
		//	return response;
		//}

		//获取中奖信息（调用抽奖系统服务）
		LotteryPrizeInfo drawPrizeInfo = null;
		try {
			UserInfo userInfo = userInfoService.getUser(null, null, userId);
			String cellNumber = userInfo==null?"":userInfo.getPhone();
			String cellTailNumber = "".equals(cellNumber)?"":cellNumber.substring(cellNumber.length()-4,cellNumber.length());
			drawPrizeInfo = secondAnniversaryService.lotteryAndUpdatePrizeQuantity(activityId, Integer.valueOf(userId), cellTailNumber);
		}catch (Exception e){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_DRAW_ERROR);
		}

		LotteryPrizeInfoVO lotteryPrizeInfoVO = new LotteryPrizeInfoVO();
		lotteryPrizeInfoVO.setPrizeId(drawPrizeInfo.getPrizeId());
		lotteryPrizeInfoVO.setPrizeName(drawPrizeInfo.getPrizeName());
		lotteryPrizeInfoVO.setPrizeLogo(drawPrizeInfo.getPrizeLogo());
		lotteryPrizeInfoVO.setPrizeType(drawPrizeInfo.getPrizeType());
		lotteryPrizeInfoVO.setListOrder(drawPrizeInfo.getListOrder());

		response.setData(CommonTool.toMap(lotteryPrizeInfoVO));
		return response;
	}

}
