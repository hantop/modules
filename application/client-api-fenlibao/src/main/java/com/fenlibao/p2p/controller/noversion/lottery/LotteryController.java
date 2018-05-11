package com.fenlibao.p2p.controller.noversion.lottery;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.lottery.entity.*;
import com.fenlibao.p2p.model.lottery.vo.LotteryPrizeInfoVO;
import com.fenlibao.p2p.model.lottery.vo.UserDrawPrizeInfoVO;
import com.fenlibao.p2p.model.lottery.vo.UserInvestBoardVO;
import com.fenlibao.p2p.model.lottery.vo.UserLotteryRecordVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidInfoService;
import com.fenlibao.p2p.service.lottery.LotteryActivityService;
import com.fenlibao.p2p.service.lottery.LotteryPrizeService;
import com.fenlibao.p2p.service.mp.MemberPointsService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("lottery")
public class LotteryController {

	private static final Logger logger= LogManager.getLogger(LotteryController.class);

	@Resource
	LotteryActivityService lotteryActivityService;

	@Resource
	LotteryPrizeService lotteryPrizeService;

	@Resource
	UserInfoService userInfoService;

	@Resource
	MemberPointsService memberPointsService;

	@Resource
	BidInfoService bidInfoService;

	@Resource
	private RedpacketService redpacketService;

	@RequestMapping(value = "/activity/state", method = RequestMethod.GET)
	HttpResponse getLotteryActivityState(@ModelAttribute BaseRequestForm paramForm,
								  @RequestParam(required = false, value = "activityCode") String activityCode) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(activityCode) ){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfo(activityCode);
		return response;
	}

	@RequestMapping(value = "/prizes", method = RequestMethod.GET)
    HttpResponse getLotteryPrizes(@ModelAttribute BaseRequestForm paramForm,
							   @RequestParam(required = false, value = "activityCode") String activityCode) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(activityCode) ){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfoNoState(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();
		List<LotteryPrizeInfo> lotteryPrizeInfos = lotteryPrizeService.getLotteryPrizeInfos(activityId);
		List<LotteryPrizeInfoVO> lotteryPrizeInfoVOList = new ArrayList<>();
		for (LotteryPrizeInfo lotteryPrizeInfo: lotteryPrizeInfos){
			LotteryPrizeInfoVO lotteryPrizeInfoVO = new LotteryPrizeInfoVO();
			lotteryPrizeInfoVO.setPrizeId(lotteryPrizeInfo.getPrizeId());
			lotteryPrizeInfoVO.setPrizeName(lotteryPrizeInfo.getPrizeName());
			lotteryPrizeInfoVO.setPrizeLogo(lotteryPrizeInfo.getPrizeLogo());
			lotteryPrizeInfoVO.setPrizeType(lotteryPrizeInfo.getPrizeType());
			lotteryPrizeInfoVO.setListOrder(lotteryPrizeInfo.getListOrder());
			lotteryPrizeInfoVOList.add(lotteryPrizeInfoVO);
		}
		response.getData().put("prizesList",lotteryPrizeInfoVOList);
		return response;
	}

	@RequestMapping(value = "/user/avail/times", method = RequestMethod.GET)
    HttpResponse getLotteryUserAvailTimes(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = false, value = "token") String token,
    		@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "activityCode") String activityCode) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(activityCode)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfo(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();
		UserLotteryTimes userLotteryTimes = lotteryPrizeService.getUserLotteryTimes(activityId,Integer.valueOf(userId));
		int availTimes = userLotteryTimes==null || userLotteryTimes.getAvailTimes() <0?0:userLotteryTimes.getAvailTimes();
		response.getData().put("availTimes",availTimes);
		return response;
	}

	@RequestMapping(value = "/user/draw/prize", method = RequestMethod.POST)
    HttpResponse lotteryDrawPrize(@ModelAttribute BaseRequestForm paramForm,
								  @RequestParam(required = false, value = "token") String token,
								  @RequestParam(required = false, value = "userId") String userId,
								  @RequestParam(required = false, value = "activityCode") String activityCode) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(activityCode)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfo(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();
		int _userId = Integer.valueOf(userId);
		//1.积分账户扣减（调用积分系统服务）
		//memberPointsService.pointsCanExchangeCash(_userId,InterfaceConst.POINTS_LOTTERY_TYPE,InterfaceConst.POINTS_LOTTERY_CONSUME_NUM);
		memberPointsService.modifyUserAccountPointsNum(InterfaceConst.POINTS_LOTTERY_TYPE,_userId,InterfaceConst.POINTS_LOTTERY_CONSUME_NUM,InterfaceConst.PCT_OUTGOINGS);

		//获取中奖信息（调用抽奖系统服务）
		LotteryPrizeInfo drawPrizeInfo = null;
		try {
			UserInfo userInfo = userInfoService.getUser(null, null, userId);
			String cellNumber = userInfo==null?"":userInfo.getPhone();
			String cellTailNumber = "".equals(cellNumber)?"":cellNumber.substring(cellNumber.length()-4,cellNumber.length());

			//检查用户是否投资过
			boolean isNovice = bidInfoService.isNovice(Integer.valueOf(userId));
			//先检查用户是否抽过奖
			int num = lotteryPrizeService.getUserLotteryCount(activityId,Integer.valueOf(userId));

			if(isNovice && num == 0){
				List<LotteryPrizeInfo>  LotteryPrizeInfos = lotteryPrizeService.getLotteryPrizeInfos(activityId);
				//必中项
				for(LotteryPrizeInfo lpf: LotteryPrizeInfos){
					if("返现券5元".equals(lpf.getPrizeName()) && lpf.getProbability().compareTo(BigDecimal.ZERO) == 0){
						drawPrizeInfo = lpf;
						break;
					}
				}
				lotteryPrizeService.addLotteryDrawRecord(activityId,Integer.valueOf(userId), cellTailNumber, drawPrizeInfo.getPrizeId());
			}else{
				drawPrizeInfo = lotteryPrizeService.lottery(activityId, Integer.valueOf(userId), cellTailNumber);
			}
		}catch (Exception e){
			//积分抽奖异常积分返还
			memberPointsService.modifyUserAccountPointsNum(InterfaceConst.POINTS_LOTTERY_TYPE,_userId,InterfaceConst.POINTS_LOTTERY_CONSUME_NUM,InterfaceConst.PCT_INCOME,"积分抽奖异常积分返还");
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

	@RequestMapping(value = "/user/draw/prizes", method = RequestMethod.GET)
	HttpResponse lotteryDrawPrizeList(@ModelAttribute BaseRequestForm paramForm,
								  @RequestParam(required = false, value = "token") String token,
								  @RequestParam(required = false, value = "userId") String userId,
								  @RequestParam(required = false, value = "activityCode") String activityCode,
								  @RequestParam(required = false, value = "drawTime") String drawTime) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(activityCode)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfoNoState(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();
		int _userId = Integer.valueOf(userId);

		Date drawDate = DateUtil.nowDate();
		if (StringUtils.isNotEmpty(drawTime)) {
			drawDate = DateUtil.timestampToDateBySec(Long.valueOf(drawTime));
		}
		List<UserLotteryDrawPrizeRecord> userLotteryDrawPrizeRecords = lotteryPrizeService.getUserDrawPrizes(_userId,activityId,drawDate);
		List<UserDrawPrizeInfoVO> userDrawPrizeInfoVOs = new ArrayList<>();
		for (UserLotteryDrawPrizeRecord userLotteryDrawPrizeRecord:userLotteryDrawPrizeRecords){
			UserDrawPrizeInfoVO userDrawPrizeInfoVO = new UserDrawPrizeInfoVO();
			userDrawPrizeInfoVO.setPrizeName(userLotteryDrawPrizeRecord.getPrizeName());
			userDrawPrizeInfoVO.setDrawTime(DateUtil.dateToTimestampToSec(userLotteryDrawPrizeRecord.getDrawTime()));
			userDrawPrizeInfoVOs.add(userDrawPrizeInfoVO);
		}
		response.getData().put("drawPrizeList",userDrawPrizeInfoVOs);
		return response;
	}

	@RequestMapping(value = "/draw/winners", method = RequestMethod.GET)
	HttpResponse lotteryDrawWinners(@ModelAttribute BaseRequestForm paramForm,
								  @RequestParam(required = false, value = "recordId") String recordId,
								  @RequestParam(required = false, value = "activityCode") String activityCode) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(activityCode)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfo(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();

		int rId=0;
		if(!StringUtils.isEmpty(recordId)){
			rId = Integer.valueOf(recordId);
		}
		UserLotteryRecord userLotteryRecord = lotteryPrizeService.getUserLotteryOneRecord(activityId,rId );
		if(userLotteryRecord==null || userLotteryRecord.getRecordId() == 0 ){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_DRAW_RECORD_NOT_FOUND);
		}
		UserLotteryRecordVO userLotteryRecordVO = new UserLotteryRecordVO();
		userLotteryRecordVO.setRecordId(userLotteryRecord.getRecordId());
		userLotteryRecordVO.setCellTailNumber(InterfaceConst.CELL_TAIL_NUMBER_PREFIX+userLotteryRecord.getCellTailNumber());
		userLotteryRecordVO.setPrizeName(userLotteryRecord.getPrizeName());

		response.setData(CommonTool.toMap(userLotteryRecordVO));
		return response;
	}


	@RequestMapping(value = "/board/day/richest/list", method = RequestMethod.GET)
	HttpResponse boardDayRichestList(@ModelAttribute BaseRequestForm paramForm) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate()){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		int limit = 6;
		Date currentDate = DateUtil.nowDate();
		if (1464105600000L >= currentDate.getTime()){ //活动统计开始时间：2016/5/25 0:0:0
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_NOT_START);
		}
		if (1464623999999L <= currentDate.getTime()){//活动统计截止时间2016/5/30 23:59:59
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_EXPIRED);
		}
		List<UserInvestBoard> userInvestBoards = lotteryPrizeService.getBoardDayRichestList(currentDate,limit,0);
		List<UserInvestBoardVO> userInvestBoardVOs = new ArrayList<UserInvestBoardVO>();
		for (UserInvestBoard userInvestBoard:userInvestBoards){
			UserInvestBoardVO userInvestBoardVO = new UserInvestBoardVO();
			userInvestBoardVO.setCellTailNumber(InterfaceConst.CELL_TAIL_NUMBER_PREFIX+userInvestBoard.getCellTailNumber());
			userInvestBoardVO.setTotalInvestAmount(String.valueOf(userInvestBoard.getTotalInvestAmount()));
			userInvestBoardVOs.add(userInvestBoardVO);
		}
		response.getData().put("boardRichestList",userInvestBoardVOs);

		return response;
	}

	@RequestMapping(value = "/user/day/total/invest", method = RequestMethod.GET)
	HttpResponse userDayTotalInvest(@ModelAttribute BaseRequestForm paramForm,
									  @RequestParam(required = false, value = "token") String token,
									  @RequestParam(required = false, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		Date currentDate = DateUtil.nowDate();
		if (1464105600000L >= currentDate.getTime()){ //活动统计开始时间：2016/5/25 0:0:0
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_NOT_START);
		}
		if (1464623999999L <= currentDate.getTime()){//活动统计截止时间2016/5/30 23:59:59
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_EXPIRED);
		}

		int limit = 1;

		List<UserInvestBoard> userInvestBoards = lotteryPrizeService.getBoardDayRichestList(currentDate,limit,Integer.valueOf(userId));
		String totalInvestAmount = "0.00";
		if (userInvestBoards!=null && userInvestBoards.size() > 0){
			UserInvestBoard userInvestBoard = userInvestBoards.get(0);
			totalInvestAmount = String.valueOf(userInvestBoard.getTotalInvestAmount());
		}
		response.getData().put("totalInvestAmount",totalInvestAmount);
		return response;
	}

	@RequestMapping(value = "/board/total/invest/list", method = RequestMethod.GET)
	HttpResponse boardTotalInvestList(@ModelAttribute BaseRequestForm paramForm) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate()){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		int limit = 10;

		List<UserInvestBoard> userInvestBoards = lotteryPrizeService.getBoardDayRichestList(null,limit,0);
		List<UserInvestBoardVO> userInvestBoardVOs = new ArrayList<UserInvestBoardVO>();
		for (UserInvestBoard userInvestBoard:userInvestBoards){
			UserInvestBoardVO userInvestBoardVO = new UserInvestBoardVO();
			userInvestBoardVO.setCellTailNumber(InterfaceConst.CELL_TAIL_NUMBER_PREFIX+userInvestBoard.getCellTailNumber());
			userInvestBoardVO.setTotalInvestAmount(String.valueOf(userInvestBoard.getTotalInvestAmount()));
			userInvestBoardVOs.add(userInvestBoardVO);
		}
		response.getData().put("boardTotalInvestList",userInvestBoardVOs);

		return response;
	}


	@RequestMapping(value = "/user/total/invest", method = RequestMethod.GET)
	HttpResponse boardUserTotalInvest(@ModelAttribute BaseRequestForm paramForm,
									  @RequestParam(required = false, value = "token") String token,
									  @RequestParam(required = false, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		int limit = 1;

		List<UserInvestBoard> userInvestBoards = lotteryPrizeService.getBoardDayRichestList(null,limit,Integer.valueOf(userId));
		String totalInvestAmount = "0.00";
		if (userInvestBoards!=null && userInvestBoards.size() > 0){
			UserInvestBoard userInvestBoard = userInvestBoards.get(0);
			totalInvestAmount = String.valueOf(userInvestBoard.getTotalInvestAmount());
		}
		response.getData().put("totalInvestAmount",totalInvestAmount);
		return response;
	}


	/**
	 * 分利宝一周年开宝箱（去除/user/draw/prize中积分相关，并添加每天只抽一次限制）
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @param activityCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/draw/anniversaryPrize", method = RequestMethod.POST)
	HttpResponse lotteryAnniversaryDrawPrize(@ModelAttribute BaseRequestForm paramForm,
											 @RequestParam(required = false, value = "token") String token,
											 @RequestParam(required = false, value = "userId") String userId,
											 @RequestParam(required = false, value = "activityCode") String activityCode) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(activityCode)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		//抽奖时效
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfo(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();

		//检查是否当天已抽奖
		List<UserLotteryDrawPrizeRecord> userLotteryDrawPrizeRecords = lotteryPrizeService.getUserThisDayDrawPrizes(Integer.valueOf(userId),activityId,DateUtil.nowDate());
		if(userLotteryDrawPrizeRecords != null && userLotteryDrawPrizeRecords.size() > 0){
			response.setCodeMessage(ResponseCode.ACTIVITY_USER_LOTTERY_TIMES_LACK);
			return response;
		}

		//获取中奖信息（调用抽奖系统服务）
		LotteryPrizeInfo drawPrizeInfo = null;
		try {
			UserInfo userInfo = userInfoService.getUser(null, null, userId);
			String cellNumber = userInfo==null?"":userInfo.getPhone();
			String cellTailNumber = "".equals(cellNumber)?"":cellNumber.substring(cellNumber.length()-4,cellNumber.length());
			drawPrizeInfo = lotteryPrizeService.lottery(activityId, Integer.valueOf(userId), cellTailNumber);
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

	/**
	 * 中奖滚动列表（一周年）
	 * @param paramForm
	 * @param activityCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/draw/anniversaryPrizes", method = RequestMethod.POST)
	HttpResponse lotteryAnniversaryDrawPrizeList(@ModelAttribute BaseRequestForm paramForm,
												 @RequestParam(required = false, value = "activityCode") String activityCode) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() ||  StringUtils.isEmpty(activityCode)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		//抽奖时效
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfo(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();

		//最终中奖数据
		List<UserLotteryDrawPrizeRecord> finalRecords = new  ArrayList<UserLotteryDrawPrizeRecord>();

		//真实中奖数据
		List<UserLotteryDrawPrizeRecord> userLotteryDrawPrizeRecords = lotteryPrizeService.getUserLatestDrawPrizes(activityId, 10);

		//先放两条真数据
		for(int i=0; i< userLotteryDrawPrizeRecords.size() && i<2; i++){
			finalRecords.add(userLotteryDrawPrizeRecords.get(0));
			userLotteryDrawPrizeRecords.remove(0);
		}

		//加入混淆数据
		LotteryActivityInfo lotteryActivitySimulateInfo = lotteryActivityService.getLotteryActivityInfo("LT_ANNIVERSARY_SIMULATE_ACTIVITY");
		int simulateActivityId = lotteryActivitySimulateInfo.getActivityId();
		List<UserLotteryDrawPrizeRecord> simulateUserLotteryDrawPrizeRecords = lotteryPrizeService.simulateLottery(simulateActivityId, 8);
		for(int i = 0; i < simulateUserLotteryDrawPrizeRecords.size(); i++){
			UserLotteryDrawPrizeRecord udpr = simulateUserLotteryDrawPrizeRecords.get(i);
			if(udpr.getRecordId() == 3){//返现券
				if(userLotteryDrawPrizeRecords.size()>0){
					simulateUserLotteryDrawPrizeRecords.set(i,userLotteryDrawPrizeRecords.get(0));
					userLotteryDrawPrizeRecords.remove(0);
				}
			}
		}

		//乱序
		Collections.shuffle(simulateUserLotteryDrawPrizeRecords);
		finalRecords.addAll(simulateUserLotteryDrawPrizeRecords);

		for(UserLotteryDrawPrizeRecord udp :finalRecords){
			String pname = udp.getPrizeName();
			if(pname.contains("返现券") && pname.indexOf("（") > 0){
				udp.setPrizeName(pname.substring(0,pname.indexOf("（")));
			}
		}

		List<UserLotteryRecordVO> userDrawPrizeInfoVOs = new ArrayList<>();
		for (UserLotteryDrawPrizeRecord userLotteryDrawPrizeRecord:finalRecords){
			UserLotteryRecordVO userLotteryRecordVO = new UserLotteryRecordVO();
			userLotteryRecordVO.setCellTailNumber(userLotteryDrawPrizeRecord.getCellTailNumber());
			userLotteryRecordVO.setPrizeName(userLotteryDrawPrizeRecord.getPrizeName());
			userDrawPrizeInfoVOs.add(userLotteryRecordVO);
		}

		response.getData().put("drawPrizeList",userDrawPrizeInfoVOs);
		return response;
	}

	/**
	 * 2017春节活动
	 * @param paramForm
	 * @param token
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/draw/springFestival2017Prize", method = RequestMethod.POST)
	HttpResponse lotterySpringFestival2017Prize(@ModelAttribute BaseRequestForm paramForm,
											 @RequestParam(required = false, value = "token") String token,
											 @RequestParam(required = false, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();

		if(!paramForm.validate() || StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}

		String activityCode = Config.get("activity.springFestival.activityCode");

		//抽奖时效
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityService.getLotteryActivityInfoWithCheckTimeFromTActivity(activityCode);
		int activityId = lotteryActivityInfo.getActivityId();

		//检查是否当天已抽奖
		List<UserLotteryDrawPrizeRecord> userLotteryDrawPrizeRecords = lotteryPrizeService.getUserThisDayDrawPrizes(Integer.valueOf(userId),activityId,DateUtil.nowDate());
		if(userLotteryDrawPrizeRecords != null && userLotteryDrawPrizeRecords.size() > 0){
			response.setCodeMessage(ResponseCode.ACTIVITY_USER_LOTTERY_TIMES_LACK);
			return response;
		}

		//获取中奖信息（调用抽奖系统服务）
		LotteryPrizeInfo drawPrizeInfo = null;
		try {
			UserInfo userInfo = userInfoService.getUser(null, null, userId);
			String cellNumber = userInfo==null?"":userInfo.getPhone();
			String cellTailNumber = "".equals(cellNumber)?"":cellNumber.substring(cellNumber.length()-4,cellNumber.length());
			drawPrizeInfo = lotteryPrizeService.lotteryAndUpdatePrizeQuantity(activityId, Integer.valueOf(userId), cellTailNumber);
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

	/**
	 * @author chen
	 * @date 20171031  获取双11活动红包列表
	 * @return
	 */
	@RequestMapping(value = "activityRedpacketList", method = RequestMethod.GET)
	public HttpResponse activityRedpacketList(String userId,String token) {
		HttpResponse apiResponse = new HttpResponse();
		//参数验证
        /*if(!paramForm.validate()){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }*/

		Map<String,Object> mapData = new HashMap<String, Object>();
		try {
			//根据条件查询活动红包列表
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId",userId);
			List<UserRedPacketInfo> redpacketList = redpacketService.getActivityRedBagList(params);

			int status = 1;//可领取
			List<UserRedPacketInfo> userRedpacketList= redpacketService.getUserRedBagByActivity(params);
			if(userRedpacketList!=null&&userRedpacketList.size()>0){
				for(UserRedPacketInfo userRedPacketInfo : userRedpacketList){
					//当天存在未使用的,不能再领取红包
					if(Integer.valueOf(userRedPacketInfo.getStatus())==1){
						status = 0 ;
					}
				}
			}
			Date currentTime = new Date();// 当前时间
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime = formatter.format(currentTime);

			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			String time = "2017-11-30 23:59:59";
			try {
				now.setTime(formatter.parse(nowtime));
				c.setTime(formatter.parse(time));
			} catch (Exception e) {
				e.printStackTrace();
			}
			int result = now.compareTo(c);// 比开始时间小，未开始
			if(result>=0){
				status = 0 ;
			}
			mapData.put("status", status);
			mapData.put("redpacketList",redpacketList);
			apiResponse.setData(mapData);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			apiResponse.setCodeMessage(ResponseCode.FAILURE );
		}
		return apiResponse;
	}


	/**
	 * 领取双11活动红包
	 * @param params
	 * @param hbId
	 * @return
	 */
	@RequestMapping(value = "receiveRedpacket", method = RequestMethod.POST)
	public HttpResponse receiveRedpacket(BaseRequestForm params,
										 @ModelAttribute BaseRequestFormExtend paramForm,
										 String hbId) {
		HttpResponse response = new HttpResponse();
		if (!params.validate() || !paramForm.validate()||StringUtils.isEmpty(hbId)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("hbId",hbId);
			map.put("userId",paramForm.getUserId());
			UserRedPacketInfo userRedPacketInfo = new UserRedPacketInfo();
			List<UserRedPacketInfo> redpacketList = redpacketService.getActivityRedBagList(map);
			if(redpacketList!=null){
				userRedPacketInfo = redpacketList.get(0);
			}else {
				response.setCodeMessage(ResponseCode.BID_RED_ENVELOPE_NOT_EXIST);
				logger.warn("领取红包失败,userId=[{}],hbId=[{}],msg=[{}]", paramForm.getUserId(), hbId, ResponseCode.BID_RED_ENVELOPE_NOT_EXIST.getMessage());
				return  response;
			}

			List<UserRedPacketInfo> userRedpacketList= redpacketService.getUserRedBagByActivity(map);
			if(userRedpacketList!=null&&userRedpacketList.size()>0){
				for(UserRedPacketInfo userRedPacket : userRedpacketList){
					//当天存在未使用的,不能再领取红包
					if(Integer.valueOf(userRedPacket.getStatus())==1){
						response.setCodeMessage(ResponseCode.ACTIVITY_OLYMPIC_REDPACKET_UNUSED);
						logger.warn("领取红包失败,userId=[{}],hbId=[{}],msg=[{}]", paramForm.getUserId(), hbId, ResponseCode.BID_RED_ENVELOPE_NOT_EXIST.getMessage());
						return  response;
					}
				}
			}

			Date currentTime = new Date();// 当前时间
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime = formatter.format(currentTime);

			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			String time = "2017-11-30 23:59:59";
			try {
				now.setTime(formatter.parse(nowtime));
				c.setTime(formatter.parse(time));
			} catch (Exception e) {
				e.printStackTrace();
			}
			int result = now.compareTo(c);// 比开始时间小，未开始
			if(result>=0){
				response.setCodeMessage(ResponseCode.ACTIVITY_OLYMPIC_REDPACKET_UNUSED);
				logger.warn("活动已经结束,userId=[{}],hbId=[{}],msg=[{}]", paramForm.getUserId(), hbId, ResponseCode.BID_RED_ENVELOPE_NOT_EXIST.getMessage());
				return  response;
			}
			// 用户红包有效时间
			Calendar calendar = buildUserRedpacketValidTime(userRedPacketInfo.getEffectDay()-1);
			// 红包有效期
			Timestamp validTime = new Timestamp(calendar.getTimeInMillis());
			String dateStr = DateUtil.getDateTime(validTime);

			redpacketService.addUserRedpacket(Integer.valueOf(hbId) ,null,String.valueOf(paramForm.getUserId()),  dateStr);
			response.setCodeMessage(ResponseCode.SUCCESS);
		} catch (BusinessException busi) {
			response.setCodeMessage(busi);
			logger.warn("领取红包失败,userId=[{}],hbId=[{}],msg=[{}]", paramForm.getUserId(), hbId, busi.getMessage());
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.warn("领取红包失败,userId=[{}],hbId=[{}],msg=[{}]", paramForm.getUserId(), hbId, e.getMessage());
		}
		return response;
	}

	/**
	 * 用户红包有效时间
	 * @param effectDay
	 * @return
	 */
	private Calendar buildUserRedpacketValidTime(Integer effectDay) {
		Calendar calendar = Calendar.getInstance();
		// 用户红包有效时间为当前时间加上红包有效天数
		calendar.add(Calendar.DATE, effectDay);
		// 时间到23:59:59
		calendar.set(calendar.HOUR_OF_DAY, 23);
		calendar.set(calendar.MINUTE, 59);
		calendar.set(calendar.SECOND, 59);
		return calendar;
	}
}