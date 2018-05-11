package com.fenlibao.p2p.dao.user.impl;

import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.user.entity.T5020;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.entity.T6118;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/10/10.
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "UserMapper.";

    @Override
    public UserInfoEntity get(Integer userId, String phoneNum) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("phoneNum", phoneNum);
        return sqlSession.selectOne(MAPPER + "get", params);
    }

    @Override
    public void addBankCard(UserBankCardVO bankCardVO) throws Exception {
        sqlSession.insert(MAPPER + "addBankCard", bankCardVO);
    }
    
	@Override
	public void updateBankCard(UserBankCardVO bankCardVO) throws Exception {
		sqlSession.update(MAPPER + "updateBankCard", bankCardVO);
	}

    @Override
    public UserBankCardVO getBankCard(int userId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        return sqlSession.selectOne(MAPPER + "getBankCard", params);
    }

	@Override
	public void updateAccount(AssetAccount t6101) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", t6101.F01);
        params.put("amount", t6101.F06);
        sqlSession.update(MAPPER+"updateAccount", params);
	}

    @Override
    public void initFundAccount(List<AssetAccount> list) {
        sqlSession.insert(MAPPER + "initFundAccount", list);
    }

    @Override
    public AssetAccount getFundAccount(int userId, T6101_F03 type) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("type", type);
        return sqlSession.selectOne(MAPPER + "getFundAccount", params);
    }

    @Override
    public AssetAccount getFundAccountByF04(String account) {
        return sqlSession.selectOne(MAPPER + "getFundAccountByF04", account);
    }

    @Override
    public int countOverdue(int userId) {
        return sqlSession.selectOne(MAPPER + "countOverdue", userId);
    }

	@Override
	public T6118 getAuthInfo(int userId) {
		return sqlSession.selectOne(MAPPER + "getAuthInfo", userId);
	}

	@Override
	public T5020 getBank(T5020 t5020) {
        return sqlSession.selectOne(MAPPER + "getBank", t5020);
	}

	@Override
	public AssetAccount getPlatformFundAccount(T6101_F03 type) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("type", type);
        return sqlSession.selectOne(MAPPER + "getPlatformFundAccount", params);
	}

	@Override
	public int getTradePwdWrongCount(int userId) {
		return sqlSession.selectOne(MAPPER + "getTradePwdWrongCount", userId);
	}
	
	@Override
	public void updateTradePwdWrongCount(int userId, boolean isReset) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("isReset", isReset);
		sqlSession.update(MAPPER + "updateTradePwdWrongCount", params);
	}
}
