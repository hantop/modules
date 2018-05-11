package com.fenlibao.p2p.service.lottery;

import com.fenlibao.p2p.model.lottery.entity.LotteryActivityInfo;

/**
 * @title  抽奖活动service
 * @author laubrence
 * @date   2016-4-26 20:54:08
 */
public interface LotteryActivityService {

	/**
	 * 获取抽奖活动信息
	 * @param activityCode
	 * @return
     */
	LotteryActivityInfo getLotteryActivityInfo(String activityCode) throws Exception;


	LotteryActivityInfo getLotteryActivityInfoNoState(String activityCode) throws Exception;

	/**
	 * 获取抽奖活动信息(在flb.t_activity中检查活动生效时间)
	 * @param activityCode
	 * @return
	 */
	LotteryActivityInfo getLotteryActivityInfoWithCheckTimeFromTActivity(String activityCode) throws Exception;
}

