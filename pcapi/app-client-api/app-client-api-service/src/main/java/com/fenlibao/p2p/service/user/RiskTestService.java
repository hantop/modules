/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: InviteService.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.invite 
 * @author: laubrence
 * @date: 2015-11-13 下午2:15:32 
 * @version: V1.1   
 */
package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.entity.user.RiskTestResult;
import com.fenlibao.p2p.model.vo.user.RiskTestQuestionVO;

import java.util.List;


public interface RiskTestService {

	/** 
	 * @Description: 获取问题列表
	 * @param userId
	 */
	List<RiskTestQuestionVO> getQuestionList();
	/** 
	 * @Description: 获取问题选项列表
	 * @param qid
	 */
//	List<RiskTestOption> getOptionByQid(int qid);
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
	int addTestResult(int resultId, int userId, int score);
	
}
