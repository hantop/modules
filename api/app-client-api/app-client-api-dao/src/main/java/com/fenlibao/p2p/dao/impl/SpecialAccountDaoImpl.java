package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.AccessoryInfoDao;
import com.fenlibao.p2p.dao.SpecialAccountDao;
import com.fenlibao.p2p.model.entity.AccessoryInfo;
import com.fenlibao.p2p.model.entity.SmsValidcode;
import com.fenlibao.p2p.model.entity.SpecialAccount;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SpecialAccountDaoImpl implements SpecialAccountDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "SpecialAccountMapper.";

	@Override
	public List<SpecialAccount> getSpecialPhone(String type) {
		return this.sqlSession.selectList(MAPPER+"getSpecialPhone", type);
	}

	@Override
	public void updateSpecialAccountSendCount(SpecialAccount specialAccount) {
		Map<String,Object> map = new HashMap<>();
		map.put("xwBalance",specialAccount.getBalance());
		map.put("num",specialAccount.getSmsCount());
		map.put("phone",specialAccount.getPhoneNum());
		this.sqlSession.update(MAPPER+"updateSpecialAccountSendCount",map);
	}
}
