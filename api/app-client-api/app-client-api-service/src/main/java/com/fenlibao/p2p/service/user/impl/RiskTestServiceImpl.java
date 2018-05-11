package com.fenlibao.p2p.service.user.impl;

import com.fenlibao.p2p.dao.user.RiskTestDao;
import com.fenlibao.p2p.model.entity.user.RiskTestQuestion;
import com.fenlibao.p2p.model.entity.user.RiskTestResult;
import com.fenlibao.p2p.model.vo.user.RiskTestQuestionVO;
import com.fenlibao.p2p.service.user.RiskTestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class RiskTestServiceImpl implements RiskTestService {

    @Resource 
    RiskTestDao riskTestDao;

	@Override
	public List<RiskTestQuestionVO> getQuestionList() {
		List<RiskTestQuestion> list=riskTestDao.getQuestionList();
		List<RiskTestQuestionVO> volist=new ArrayList<RiskTestQuestionVO>();
		if(list!=null && list.size()>0){
			for (RiskTestQuestion RiskTestQuestion : list) {
				RiskTestQuestionVO vo=new RiskTestQuestionVO(RiskTestQuestion);
				vo.setOptions(riskTestDao.getOptionByQid(RiskTestQuestion.getId()));
				volist.add(vo);
			}
		}
		return volist;
	}

	@Override
	public RiskTestResult getResultByScore(int score) {
		return riskTestDao.getResultByScore(score);
	}

	@Override
	public int addTestResult(int resultId, int userId, int score) {
		return  riskTestDao.addTestResult(resultId, userId, score);
	}

	@Override
	public boolean queryHadTestedByUid(int userId) {
		boolean flag = true;
		if (riskTestDao.queryHadTestedByUid(userId) >= 1) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}
}
