package com.fenlibao.p2p.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.TFlowSendRecordDao;
import com.fenlibao.p2p.model.entity.TFlowSendRecord;

@Repository
public class TFlowSendRecordDaoImpl implements TFlowSendRecordDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "TFlowSendRecord.";
	
	@Override
	public List<TFlowSendRecord> getWaitFlowSendRecord(Map<String, Object> map) {
		return sqlSession.selectList(MAPPER+"getFlowSendRecord", map);
	}

	@Override
	public void addFlowSendRecord(TFlowSendRecord record) {
		this.sqlSession.insert(MAPPER+"addFlowSendRecord", record);
		
	}

	/** 
	 * @Title: updateSendFlow 
	 * @Description: TODO
	 * @param paramMap
	 * @return: void
	 */
	public void updateSendFlow(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		this.sqlSession.update(MAPPER+"updateSendFlow", paramMap);

	}

}
