package com.fenlibao.p2p.dao.user;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.user.RiskTestOption;
import com.fenlibao.p2p.model.entity.user.RiskTestQuestion;
import com.fenlibao.p2p.model.entity.user.RiskTestResult;


/** 
 * @ClassName: RiskTestDao 
 * @date: 2016-06-23
 */
public interface RiskTestDao {

	/** 
	 * @Description: 获取问题列表
	 * @param userId
	 */
	List<RiskTestQuestion> getQuestionList();
	/** 
	 * @Description: 获取问题选项列表
	 * @param qid
	 */
	List<RiskTestOption> getOptionByQid(int qid);
	/** 
	 * @Description: 工具分数获取测试结果
	 * @param score
	 */
	RiskTestResult getResultByScore(int score);
	
	/**
	 * @Description: 添加用户测试结果
	 * @param resultId
	 * @param userId
	 * @param score
	 * @return
	 */
	int addTestResult(int resultId,int userId,int score);
	/**
	 * @Description: 获取用户测试结果
	 * @param resultId
	 * @return
	 */
	Map getTestResultByUid(int userId);

	/**
	 * 获取用户测试记录
	 * @param userId
	 * @return
	 */
    int queryHadTestedByUid(int userId);
}

