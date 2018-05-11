package com.fenlibao.p2p.dao.experiencegold;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.ExperienceGoldInfo;
import com.fenlibao.p2p.model.entity.experiencegold.ExperienceGoldEarningsEntity;
import com.fenlibao.p2p.model.form.experiencegold.ExperienceGoldEarningsForm;
import com.fenlibao.p2p.model.form.experiencegold.UserExperienceGoldForm;

public interface IExperienceGoldDao {

    /**
     * 获取体验金设置信息
     * @param paramMap
     * @return
     */
    List<ExperienceGoldInfo> getActivityByType(Map<String, Object> paramMap);


    /**
     * 发放体验金
     * @param paramMap
     * @return
     */
    void addUserExperienceGold(Map<String, Object> paramMap);
	
	/**
	 * 获取用户累计收益
	 * @param userId
	 * @return
	 */
	BigDecimal getTotalEarnings(int userId);
	
	/**
	 * 获取用户收益列表
	 * @param userId
	 * @param begin
	 * @param end
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
	 * 保存体验金流水
	 * @param paramMap
	 * @return
	 */
	void addExpGoldFunding(Map<String, Object> paramMap);

	/**
	 * 获取用户对应的体验金
	 * @param paramMap
	 * @return
	 */
	List<ExperienceGoldInfo> getExperienceGolds(Map<String, Object> paramMap);
	/**
	 * 插入体验金日收益
	 * @param params
	 * @return
	 * @throws Exception
	 */
	int insertDayEarnings(ExperienceGoldEarningsEntity entity) throws Exception;
	
	/**
	 * 获取“未计息”的用户体验金
	 * @return
	 */
	List<UserExperienceGoldForm> getUserExperienceGoldList();
	
	/**
	 * 更新用户体验金计息状态
	 * @param expId
	 * @param status
	 * @param yieldStatus
	 * @return
	 * @throws Exception
	 */
	int updateUserExperience(int expId, Integer status, String yieldStatus) throws Exception;
	
	/**
	 * 更新体验金收益状态为已到账
	 * @param expId 用户体验金ID
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	int updateExperienceEarningsStatus(int expId, int userId) throws Exception;
	
	/**
	 * 更新用户体验金计息状态
	 * @param id 用户体验金ID
	 * @param yieldStatus 计息状态
	 * @return
	 * @throws Exception
	 */
	int updateUserExperienceYieldStatus(int id, String yieldStatus) throws Exception;
	
	/**
	 * 获取到期的用户体验金
	 * @return
	 */
	List<UserExperienceGoldForm> getExpireUserExperienceGold();
}
