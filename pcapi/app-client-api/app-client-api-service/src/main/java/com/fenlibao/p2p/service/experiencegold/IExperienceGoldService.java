package com.fenlibao.p2p.service.experiencegold;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.ExperienceGoldInfo;
import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.experiencegold.ExperienceGoldEarningsEntity;
import com.fenlibao.p2p.model.form.experiencegold.ExperienceGoldEarningsForm;
import com.fenlibao.p2p.model.form.experiencegold.UserExperienceGoldForm;

public interface IExperienceGoldService {

	/**
	 * 获取体验金详情
	 * @param userId
	 * @param status
	 * @return
	 */
	Map<String, Object> getDetail(int userId, int status);
	
	/**
	 * 获取用户累计收益
	 * @param userId
	 * @return
	 */
	BigDecimal getTotalEarnings(int userId);
	
	/**
	 * 获取用户收益列表
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<ExperienceGoldEarningsForm> getEarningsList(int userId, String timestamp);
	
	/**
	 * 获取昨天收益
	 * @param userId
	 * @return
	 */
	BigDecimal getEarningsYesterday(int userId);
	
	/**
	 * 初始化体验金收益
	 * @throws Exception
	 */
	void initExperienceGoldEarnings(int validDate, Date earningsDate,
			ExperienceGoldEarningsEntity goldEarnings, UserExperienceGoldForm g) throws Exception;

	/**
	 * 获取体验金设置信息
	 * @param expType 体验金类型
	 * @return
	 */
	List<ExperienceGoldInfo> getActivityByType(int expType);

	/**
	 * 给用户发放体验金
	 * @param experienceGoldInfos
	 * @param userId
	 * @param phoneNum
	 */
	void addUserExperienceGold(List<ExperienceGoldInfo> experienceGoldInfos, String userId, String phoneNum);

	/**
	 * 获取用户对应的体验金
	 * @param paramMap
	 * @return
	 */
	List getExperienceGolds(Map<String, Object> paramMap);
	
	/**
	 * 结束体验金体验，将收益打给用户
	 * <p>现把事务控制在单个
	 * @param feeType
	 * @param accountType
	 * @param platformAccount
	 * @param g
	 * @param totalEarnings
	 * @param content
	 * @throws Exception
	 */
	void endExperience(FeeType feeType, String accountType, String platformAccount, UserExperienceGoldForm g,
			BigDecimal totalEarnings, String content) throws Exception;
	
	/**
	 * 获取到期的用户体验金
	 * @return
	 */
	List<UserExperienceGoldForm> getExpireUserExperienceGold();
	
	/**
	 * 获取“未计息”的用户体验金
	 * @return
	 */
	List<UserExperienceGoldForm> getUserExperienceGoldList();
	
}
