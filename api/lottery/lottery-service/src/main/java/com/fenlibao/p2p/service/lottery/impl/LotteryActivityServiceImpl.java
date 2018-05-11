package com.fenlibao.p2p.service.lottery.impl;

import com.fenlibao.p2p.dao.lottery.LotteryActivityDao;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.lottery.entity.ActivityInfo;
import com.fenlibao.p2p.model.lottery.entity.LotteryActivityInfo;
import com.fenlibao.p2p.model.lottery.entity.UserInvestBoard;
import com.fenlibao.p2p.service.lottery.LotteryActivityService;
import com.fenlibao.p2p.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {

	@Resource
	private LotteryActivityDao lotteryActivityDao;

	/**
	 * 获取抽奖活动信息
	 * @param activityCode
	 * @return
     */
	@Override
	public LotteryActivityInfo getLotteryActivityInfo(String activityCode) throws Exception{
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityDao.getLotteryActivityInfo(0,activityCode);
		if(lotteryActivityInfo==null){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST.getCode(),ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST.getMessage());
		}
		if(DateUtil.dateToTimestampToSec(DateUtil.nowDate()) < DateUtil.dateToTimestampToSec(lotteryActivityInfo.getStartDateTime())){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_NOT_STARTED.getCode(),ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_NOT_STARTED.getMessage());
		}
		if(DateUtil.dateToTimestampToSec(DateUtil.nowDate()) > DateUtil.dateToTimestampToSec(lotteryActivityInfo.getEndDateTime())){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_EXPIRE.getCode(),ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_EXPIRE.getMessage());
		}
		return lotteryActivityInfo;
	}

	/**
	 * 获取抽奖活动信息(在flb.t_activity中检查活动生效时间)
	 * @param activityCode
	 * @return
	 */
	@Override
	public LotteryActivityInfo getLotteryActivityInfoWithCheckTimeFromTActivity(String activityCode) throws Exception{
		ActivityInfo activityInfo = lotteryActivityDao.getActivityInfo(0,activityCode);
		LotteryActivityInfo lotteryActivityInfo = lotteryActivityDao.getLotteryActivityInfo(0,activityCode);

		if(activityInfo==null && lotteryActivityInfo == null){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST.getCode(),ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST.getMessage());
		}
		if(DateUtil.dateToTimestampToSec(DateUtil.nowDate()) < DateUtil.dateToTimestampToSec(activityInfo.getStartDateTime())){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_NOT_STARTED.getCode(),ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_NOT_STARTED.getMessage());
		}
		if(DateUtil.dateToTimestampToSec(DateUtil.nowDate()) > DateUtil.dateToTimestampToSec(activityInfo.getEndDateTime())){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_EXPIRE.getCode(),ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_HAVE_EXPIRE.getMessage());
		}
		return lotteryActivityInfo;
	}

	/**
	 * 获取无状态抽奖活动信息
	 * @param activityCode
	 * @return
	 */
	@Override
	public LotteryActivityInfo getLotteryActivityInfoNoState(String activityCode) throws Exception {
		LotteryActivityInfo lotteryActivityInfo =  lotteryActivityDao.getLotteryActivityInfo(0, activityCode);
		if(lotteryActivityInfo==null){
			throw new BusinessException(ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST.getCode(),ResponseCode.ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST.getMessage());
		}
		return lotteryActivityInfo;
	}

}
