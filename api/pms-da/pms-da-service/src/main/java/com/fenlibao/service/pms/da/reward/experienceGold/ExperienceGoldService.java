package com.fenlibao.service.pms.da.reward.experienceGold;

import java.util.List;
import java.util.Map;

import com.fenlibao.model.pms.da.reward.ExperienceGoldStatistics;
import com.fenlibao.model.pms.da.reward.ExperienceGold;
import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserExperience;

public interface ExperienceGoldService {
    
    /**
     * 插入体验金发放记录
     * @param record
     * @return
     * @throws Exception 
     */
	List<String> insertExperienceGoldRecords(RewardRecord record,List<String[]> detailList) throws Exception;
    
	/**
	 * 发放体验金
	 */
    String grantExperienceGold(RewardRecord rewardRecord) throws Exception;
	
    /**
     * 查询体验金记录详情
     * @param userExperience
     * @param bounds
     * @return
     */
    List<UserExperience> getExperienceGoldRecordDetail(UserExperience userExperience,RowBounds bounds);

    List<ExperienceGold> findExperienceGoldPager(ExperienceGold experienceGold, RowBounds bounds);

    /**
     * save or update
     * 编辑体验金信息，如果字段为null不修改
     * 新体验金信息，如果新增的字段为null将不进行添加
     *
     * @param experienceGold
     * @return
     */
    int experienceGoldEdit(ExperienceGold experienceGold);

    /**
     * 根据id获取体验金信息
     *
     * @param id
     * @return
     */
    ExperienceGold getExperienceGoldById(int id);

    /**
     * 删除
     *
     * @param experienceGolds
     * @return
     */
    int experienceGoldRemove(List<Integer> experienceGolds);
    /**
     * 体验金发放记录作废
     * @param rewardRecord
     * @return
     */
    String cancelExperienceGold(RewardRecord rewardRecord) throws Exception;

    List<UserExperience> findAllActivityCode(UserExperience userExperience);
    
    List<ExperienceGoldStatistics> experienceGoldStatistics(Integer grantId);
}
