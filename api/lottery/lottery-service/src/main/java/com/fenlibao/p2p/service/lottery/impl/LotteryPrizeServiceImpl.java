package com.fenlibao.p2p.service.lottery.impl;

import com.fenlibao.p2p.dao.lottery.LotteryPrizeDao;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.lottery.entity.*;
import com.fenlibao.p2p.service.lottery.LotteryPrizeService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.lottery.LotteryUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
public class LotteryPrizeServiceImpl implements LotteryPrizeService {

	@Resource
	LotteryPrizeDao lotteryPrizeDao;

	@Override
	public List<LotteryPrizeInfo> getLotteryPrizeInfos(int activityId) throws Exception {
		return lotteryPrizeDao.getLotteryPrizeInfos(activityId);
	}

	@Override
	public List<LotteryPrizeInfo> getEffectivePrizeInfos(int activityId) throws Exception {
		return lotteryPrizeDao.getEffectivePrizeInfos(activityId);
	}

	@Transactional
	public LotteryPrizeInfo lotteryTemp(int activityId, int userId, String cellTailNumber) throws Exception {
		//查询用户抽奖次数信息
		UserLotteryTimes userLotteryTimes =  lotteryPrizeDao.getUserLotteryTimes(activityId, userId);
		if(userLotteryTimes == null || userLotteryTimes.getAvailTimes() <= 0 ){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_TIMES_LACK);
		}
		//锁定用户抽奖次数记录
		UserLotteryTimes lockUserLotteryTimes =  lotteryPrizeDao.lockUserLotteryTimes(userLotteryTimes.getRecordId());
		if(lockUserLotteryTimes == null || lockUserLotteryTimes.getAvailTimes() == 0 ){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_TIMES_LACK);
		}
		//获取中奖奖品信息
		LotteryPrizeInfo drawPrizeInfo = getLotteryDrawPrize(activityId);
		if(drawPrizeInfo==null){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_DRAW_ERROR);
		}
		//更新用户可抽奖次数减1
		int userTimesUpdateFlag = lotteryPrizeDao.updateUserLotteryAvailTimes(lockUserLotteryTimes.getRecordId());
		int drawRecordFlag = lotteryPrizeDao.addLotteryDrawRecord(activityId ,userId, cellTailNumber, drawPrizeInfo.getPrizeId());
		if(userTimesUpdateFlag == 0 || drawRecordFlag == 0){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_DRAW_ERROR);
		}
		return drawPrizeInfo;
	}

	public LotteryPrizeInfo getLotteryDrawPrize(int activityId){
		List<LotteryPrizeInfo> effectivePrizeInfos = lotteryPrizeDao.getEffectivePrizeInfos(activityId);
		if(effectivePrizeInfos ==null || effectivePrizeInfos.size() == 0){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES.getCode(),ResponseCode.ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES.getMessage());
		}
		List<Double> orignalRates = new ArrayList<Double>(effectivePrizeInfos.size());
		for (LotteryPrizeInfo prizeInfo : effectivePrizeInfos) {
			double probability = prizeInfo.getProbability().doubleValue();
			if (probability < 0) {
				probability = 0;
			}
			orignalRates.add(probability);
		}
		int drawIndex = LotteryUtil.lottery(orignalRates);
		LotteryPrizeInfo drawPrizeInfo = effectivePrizeInfos.get(drawIndex);
		return drawPrizeInfo;
	}

	@Override
	public UserLotteryTimes getUserLotteryTimes(int activityId, int userId) throws Exception {
		return lotteryPrizeDao.getUserLotteryTimes(activityId, userId);
	}

	@Override
	public UserLotteryRecord getUserLotteryOneRecord(int activityId, int recordId) throws Exception {
		return lotteryPrizeDao.getUserLotteryOneRecord(activityId,recordId);
	}
	/**
	 * 获取一条用户在某活动中的抽奖次数
	 * @param activityId
	 * @return
	 * @throws Exception
	 */
	public int getUserLotteryCount(int activityId, int userId) throws Exception{
		return lotteryPrizeDao.getUserLotteryCount(activityId,userId);
	}

	@Override
	public List<UserInvestBoard> getBoardDayRichestList(Date currentDate, int limit, int userId) {
		return lotteryPrizeDao.getBoardDayRichestList(currentDate,limit,userId);
	}

    @Override
	public LotteryPrizeInfo lottery(int activityId, int userId, String cellTailNumber) throws Exception {
		//获取中奖奖品信息
		LotteryPrizeInfo drawPrizeInfo = getLotteryDrawPrize(activityId);
		if(drawPrizeInfo==null){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_DRAW_ERROR);
		}
		int drawRecordFlag = lotteryPrizeDao.addLotteryDrawRecord(activityId ,userId, cellTailNumber, drawPrizeInfo.getPrizeId());
		return drawPrizeInfo;
	}

	@Transactional
	@Override
	public LotteryPrizeInfo lotteryAndUpdatePrizeQuantity(int activityId, int userId, String cellTailNumber) throws Exception {
		//获取中奖奖品信息
		LotteryPrizeInfo drawPrizeInfo = null;
		boolean enoughPrizeFlag = false;
		try{
			drawPrizeInfo = getLotteryDrawPrize(activityId);
			if(drawPrizeInfo==null){
				throw new Exception();
			}
			enoughPrizeFlag = true;
		}catch (Exception e){
			//奖品不够或者抽奖异常，给予指定奖品
			String benchPrizeId = Config.get("activity.springFestival.benchPrizeId");
			List<LotteryPrizeInfo>  LotteryPrizeInfos = getLotteryPrizeInfos(activityId);
			for(LotteryPrizeInfo lpf: LotteryPrizeInfos){
				if(lpf.getPrizeId() == Integer.parseInt(benchPrizeId)){
					drawPrizeInfo = lpf;
					break;
				}
			}
		}

		if(drawPrizeInfo==null){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_DRAW_ERROR);
		}

		if(enoughPrizeFlag){
			//减少奖品数量
			updatePrizeQuantity(activityId, drawPrizeInfo.getPrizeId(), 1);
			//更新奖品概率
			updatePrizeProbability(activityId, drawPrizeInfo.getPrizeId(),1);
		}

		//插入中奖纪录
		int drawRecordFlag = lotteryPrizeDao.addLotteryDrawRecord(activityId ,userId, cellTailNumber, drawPrizeInfo.getPrizeId());
		if(drawRecordFlag <= 0){
			throw new BusinessException(ResponseCode.ACTIVITY_USER_LOTTERY_DRAW_ERROR);
		}

		return drawPrizeInfo;
	}

	public int updatePrizeQuantity(int activityId, int prizeId, int quantity) {
		return lotteryPrizeDao.updatePrizeQuantity(activityId ,prizeId, quantity);
	}

	public int updatePrizeProbability(int activityId, int prizeId, int quantity) {
		return lotteryPrizeDao.updatePrizeProbability(activityId ,prizeId, quantity);
	}

	@Override
	public int addLotteryDrawRecord(int activityId, int userId, String cellTailNumber, int prizeId) throws Exception {
		return lotteryPrizeDao.addLotteryDrawRecord(activityId ,userId, cellTailNumber, prizeId);
	}

    @Override
    public List<UserLotteryDrawPrizeRecord>  getUserDrawPrizes(int userId, int activityId, Date drawDate) {
        return lotteryPrizeDao.getUserDrawPrizes(userId,activityId,drawDate);
    }

	/**
	 * 获取当天中奖奖品列表
	 * @param userId
	 * @param activityId
	 */
	@Override
	public List<UserLotteryDrawPrizeRecord> getUserThisDayDrawPrizes(int userId, int activityId, Date drawDate) {
        return lotteryPrizeDao.getUserThisDayDrawPrizes(userId, activityId, drawDate);
    }

	@Override
    public List<UserLotteryDrawPrizeRecord> getUserLatestDrawPrizes(int activityId, int size){
        return lotteryPrizeDao.getUserLatestDrawPrizes(activityId,size);
    }

	/**
	 * 抽奖(只获取数据，不插入数据库，用于周年庆开宝箱中奖虚拟数据)
	 * @param activityId
	 * @param num
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<UserLotteryDrawPrizeRecord> simulateLottery(int activityId, int num) throws Exception {
		List<LotteryPrizeInfo> effectivePrizeInfos = lotteryPrizeDao.getEffectivePrizeInfos(activityId);
		if(effectivePrizeInfos ==null || effectivePrizeInfos.size() == 0){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES.getCode(),ResponseCode.ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES.getMessage());
		}
		List<Double> orignalRates = new ArrayList<Double>(effectivePrizeInfos.size());
		for (LotteryPrizeInfo prizeInfo : effectivePrizeInfos) {
			double probability = prizeInfo.getProbability().doubleValue();
			if (probability < 0) {
				probability = 0;
			}
			orignalRates.add(probability);
		}

		List<UserLotteryDrawPrizeRecord> lps = new ArrayList<UserLotteryDrawPrizeRecord>();
		for (int i = 0 ; i < num ; i++){
			int drawIndex = LotteryUtil.lottery(orignalRates);
			LotteryPrizeInfo drawPrizeInfo = effectivePrizeInfos.get(drawIndex);

			UserLotteryDrawPrizeRecord udpr = new UserLotteryDrawPrizeRecord();
			udpr.setPrizeName(drawPrizeInfo.getPrizeName());
			udpr.setCellTailNumber(createPhoneNum());
			udpr.setRecordId(drawPrizeInfo.getPrizeType());
			lps.add(udpr);
		}

		return lps;
	}

	private String createPhoneNum(){
		String[] preNums = {"137","138","150","158","189"};
		Random random = new Random();
		int x = random.nextInt(8999);
		x = x+1000;
		String res = preNums[random.nextInt(4)]+"****"+x;
		return res;
	}
}
