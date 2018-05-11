package com.fenlibao.p2p.dao.mq.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.mq.UserFundsTenderDao;
import com.fenlibao.p2p.model.mp.entity.UserAuthStatus;
import com.fenlibao.p2p.model.mp.entity.UserFundsAccountInfo;

@Repository
public class UserFundsTenderDaoImpl implements UserFundsTenderDao {
	
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "UserFundsTenderMapper.";

	/**
	 * @Title: getPTUserAccountId
	 * @Description: TODO
	 * @return 
	 * @see com.fenlibao.p2p.dao.mq.UserFundsTenderDao#getPTUserAccountId() 
	 */
	@Override  
	public int getPTUserAccountId() {
		Map<String,Object> map = new HashMap<String,Object>();
		UserFundsAccountInfo user = sqlSession.selectOne(MAPPER + "getPTUserAccountId",map);
		if(user !=null){
			return user.getUserId();
		}
		return 0;
	}

	@Override
	public UserFundsAccountInfo getUserFundsAccountInfo(int userId, String accountType) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("accountType", accountType);
		return sqlSession.selectOne(MAPPER + "getUserFundsAccountInfo",map);
	}

	@Override
	public int updateUserFundsAccountAmount(int fundsAccountId,
			BigDecimal accountAmount, Date nowDatetime) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fundsAccountId", fundsAccountId);
		map.put("accountAmount", accountAmount);
		map.put("nowDatetime", nowDatetime);
		return sqlSession.update(MAPPER + "updateUserFundsAccountAmount",map);
	}

	@Override
	public int addT6102Record(int zcwlzhId, int FeeCode, int zrwlzhId,
			Date nowDatetime, BigDecimal zrcashAmount, BigDecimal zccashAmount,BigDecimal balanceAmount,
			String remark) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("zcwlzhId", zcwlzhId);
		map.put("FeeCode", FeeCode);
		map.put("zrwlzhId", zrwlzhId);
		map.put("nowDatetime", nowDatetime);
		map.put("zrcashAmount", zrcashAmount);
		map.put("zccashAmount", zccashAmount);
		map.put("balanceAmount", balanceAmount);
		map.put("remark", remark);
		return sqlSession.insert(MAPPER + "addT6102Record",map);
	}

	@Override
	public UserAuthStatus getUserAuthInfo(int userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		return sqlSession.selectOne(MAPPER + "getUserAuthInfo",map);
	}

}
