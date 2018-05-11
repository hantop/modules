package com.fenlibao.p2p.dao;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.TFlowSendRecord;

public interface TFlowSendRecordDao {

	/**
	 * 获取等待发送流量的用户信息
	 * @param map
	 * @return
	 */
	public List<TFlowSendRecord> getWaitFlowSendRecord(Map<String,Object> map);
	
	/**
	 * 添加已发送记录
	 * @param record
	 */
	public void addFlowSendRecord(TFlowSendRecord record);
}
