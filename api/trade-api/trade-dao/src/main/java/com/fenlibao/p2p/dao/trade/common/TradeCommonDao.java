package com.fenlibao.p2p.dao.trade.common;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fenlibao.p2p.model.trade.entity.CapitalFlow;

public interface TradeCommonDao {
	Timestamp getCurrentTimestamp()throws Exception;
	Date getCurrentDate() throws Exception;
	long insertT6123(Integer userId, String title, String state) throws Exception;
	void insertT6124(long letterId, String content) throws Exception;
	String getSystemVariable(String id) throws Exception;
	Integer insertT1040(Integer type, String content) throws Exception;
	void insertT1041(Integer msgId, String mobile) throws Exception;
	void insertT6102s(List<CapitalFlow> list)throws Exception;
}
