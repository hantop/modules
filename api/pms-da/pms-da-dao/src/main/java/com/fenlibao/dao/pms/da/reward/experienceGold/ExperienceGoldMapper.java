package com.fenlibao.dao.pms.da.reward.experienceGold;

import com.fenlibao.model.pms.da.reward.ExperienceGoldStatistics;
import com.fenlibao.model.pms.da.reward.ExperienceFunding;
import com.fenlibao.model.pms.da.reward.ExperienceGold;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserExperience;

import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ExperienceGoldMapper {
	
	int insertExperienceGoldRecord(RewardRecord record);
	
	List<UserExperience> getExperienceGoldRecordDetail(UserExperience userExperience,RowBounds bounds);
	
    int deleteByPrimaryKey(Integer id);

    int insert(ExperienceGold record);

    int insertSelective(ExperienceGold record);

    ExperienceGold selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExperienceGold record);

    int updateByPrimaryKey(ExperienceGold record);

    List<ExperienceGold> findExperienceGoldPager(ExperienceGold experienceGold, RowBounds bounds);

    int getExperienceGoldCountByCode(String code,int id);

    int experienceGoldRemove(List<Integer> experienceGolds);
    
    ExperienceGold getExperienceGoldbyCode(String activityCode);
    
    int insertExperienceGoldRecordDetail(List<UserExperience> list);
    
    int updateExperienceGoldRecordDetail(List<UserExperience> userExperienceList);
    
    int insertExperienceFunding(List<ExperienceFunding> experienceFundingList);

    List<UserExperience> findAllActivityCode(UserExperience userExperience);
    
    List<ExperienceGoldStatistics> experienceGoldStatistics(Integer grantId);
}