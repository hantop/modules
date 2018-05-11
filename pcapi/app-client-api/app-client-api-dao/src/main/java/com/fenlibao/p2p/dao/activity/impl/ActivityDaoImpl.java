package com.fenlibao.p2p.dao.activity.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.entity.activity.AnniversaryInvestRecord;
import com.fenlibao.p2p.model.vo.redpacket.RedPacketActivityVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.activity.ActivityDao;

@Repository
public class ActivityDaoImpl implements ActivityDao{
	
	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "ActivityMapper.";
	
	@Override
    public int insertActivity(String activityCode, String phone, int isNew){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("activityCode", activityCode);
    	map.put("phone", phone);
    	map.put("joinTime", new Date());
    	if(isNew == 0){
    		map.put("registType", 1);
    	}else{
    		map.put("registType", 3);
    	}
    	return this.sqlSession.insert(MAPPER+"insertActivity", map);
    }
    
	@Override
    public int validRegistActivity(String activityCode, String phone){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("activityCode", activityCode);
    	map.put("phone", phone);
    	List list = this.sqlSession.selectList(MAPPER+"validRegistActivity", map);
    	if(list!=null && list.size() >0){
    		return (int) list.get(0);
    	}
    	return 0;
    }


	@Override
	public Map<String,Object>  isActivityTime(Map map) {
		return sqlSession.selectOne(MAPPER + "isActivityTime", map);
	}

	@Override
	public List<AnniversaryInvestRecord> anniversaryInvestRecords() {
		return sqlSession.selectList(MAPPER + "anniversaryInvestRecords");
	}

	@Override
	public Map<String, Object> myAnniversaryInvestInfo(String userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		return sqlSession.selectOne(MAPPER + "myAnniversaryInvestInfo", map);
	}

	@Override
	public List<RedPacketActivityVO> getRedPacketList(Integer userId, String activityCode) {
		Map<String, Object> param = new HashMap<>(1);
		param.put("userId", userId);
		param.put("activityCode", activityCode);
		return sqlSession.selectList(MAPPER + "getRedPacketList", param);
	}

	@Override
	public Integer getCurdateUnusedRedPacket(int userId, String activityCode) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("activityCode", activityCode);
		return sqlSession.selectOne(MAPPER + "getCurdateUnusedRedPacket", params);
	}

	@Override
	public Integer getStatus(Integer userId) {
		Map<String,Object> map = new HashMap<>();
		map.put("userId",userId);

		return sqlSession.selectOne(MAPPER + "getPhoneStatus", map);
	}

	@Override
	public void addActivityUserPhone(int userId, String phone, String activityCode) throws Exception {
		Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
		map.put("phone",phone);
		map.put("code",activityCode);
		sqlSession.insert(MAPPER + "addActivityUserPhone", map);
	}
}

