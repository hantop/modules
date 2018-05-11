package com.fenlibao.p2p.dao.salary;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.TJoinRecord;

public interface TJoinRecordDao {

	/**
	 * 获取加入记录
	 * @param map
	 * @return
	 */
	public List<TJoinRecord> getJoinRecord(Map<String,Object> map);
}
