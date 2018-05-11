package com.fenlibao.p2p.dao.trade.bid.impl;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.model.trade.entity.*;
import com.fenlibao.p2p.model.trade.entity.bid.T6504;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BidManageDaoImpl implements BidManageDao{
	
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "BidManageMapper.";

	@Override
	public T6230 getBidById(Integer loanId) throws Exception{
		T6230 t6230=sqlSession.selectOne(MAPPER + "getBidById", loanId);
		return t6230;
	}

	@Override
	public void releaseBid(Map<String, Object> params) throws Exception{
		sqlSession.update(MAPPER + "releaseBid", params);
	}

	@Override
	public T6231 getBidExInfoById(Integer loanId) throws Exception{
		T6231 t6231=sqlSession.selectOne(MAPPER + "getBidExInfoById", loanId);
		return t6231;
	}

	@Override
	public List<T6250> getTenderRecord(Map<String,Object> params) throws Exception {
		List<T6250> list=sqlSession.selectList(MAPPER + "getTenderRecord", params);
		return list;
	}

	@Override
	public T6238 getBidRateById(Integer loanId) throws Exception {
		T6238 t6238=sqlSession.selectOne(MAPPER + "getBidRateById", loanId);
		return t6238;
	}

	@Override
	public T6250 getTenderRecordById(Integer id) throws Exception {
		return sqlSession.selectOne(MAPPER + "getTenderRecordById", id);
	}

	@Override
	public void updateTenderRecord(Map<String,Object> params) throws Exception {
		sqlSession.update(MAPPER+"updateTenderRecord", params);
	}

	@Override
	public void updateBid(Map<String, Object> params) {
		sqlSession.update(MAPPER+"updateBid", params);
	}

	@Override
	public void updateBidExInfo(Map<String, Object> params) {
		sqlSession.update(MAPPER+"updateBidExInfo", params);
	}

	@Override
	public void insertDebt(List<T6251> list) {
		sqlSession.insert(MAPPER+"insertDebt", list);
	}

	@Override
	public List<T6251> getDebts(Map<String, Object> params) {
		List<T6251> list=sqlSession.selectList(MAPPER + "getDebts", params);
		return list;
	}

	@Override
	public void insertRepayPlan(List<T6252> list) {
		sqlSession.insert(MAPPER+"insertRepayPlan", list);
	}

	@Override
	public List<T6252> getRepayPlan(Map<String, Object> params) {
		List<T6252> list=sqlSession.selectList(MAPPER + "getRepayPlan", params);
		return list;
	}

	@Override
	public T6252 getAndLockRepayPlan(Map<String, Object> params) {
		T6252 e=sqlSession.selectOne(MAPPER + "getAndLockRepayPlan", params);
		return e;
	}

	@Override
	public void updateRepayPlan(Map<String, Object> params) {
		sqlSession.update(MAPPER+"updateRepayPlan", params);
	}

	@Override
	public T6252 getRepayPlanById(int id) {
		return sqlSession.selectOne(MAPPER + "getRepayPlanById", id);
	}

	@Override
	public T6251 getDebtById(int id) {
		return sqlSession.selectOne(MAPPER + "getDebtById", id);
	}

	@Override
	public void updateDebt(Map<String, Object> params) {
		sqlSession.update(MAPPER+"updateDebt", params);
	}

	@Override
	public List<T6252> getSybjOfDebt(Map<String, Object> params) {
		List<T6252> list=sqlSession.selectList(MAPPER + "getSybjOfDebt", params);
		return list;
	}

	@Override
	public void updateT6252TQH(Map<String, Object> params) {
		sqlSession.update(MAPPER+"updateT6252TQH", params);
	}

	@Override
	public T6252 getSybjByDebt(Map<String, Object> params) {
		T6252 e=sqlSession.selectOne(MAPPER + "getSybjByDebt", params);
		return e;
	}

	@Override
	public T6260 getDebtTransferApply(Map<String, Object> params) {
		T6260 e=sqlSession.selectOne(MAPPER + "getDebtTransferApply", params);
		return e;
	}

	@Override
	public void updateDebtTransferApply(Map<String, Object> params) {
		sqlSession.update(MAPPER+"updateDebtTransferApply", params);
	}

	@Override
	public int getTotalTerms(int F02) {
		int totalTerms=sqlSession.selectOne(MAPPER + "getTotalTerms", F02);
		return totalTerms;
	}

	@Override
	public List<Integer> getSealBidAccounts() {
		List<Integer> list=sqlSession.selectList(MAPPER + "getSealBidAccounts");
		return list;
	}

@Override
	public T6504 getTenderOrder(int orderId) {
		return sqlSession.selectOne(MAPPER + "getTenderOrder", orderId);
	}

	@Override
	public void addTenderRecord(T6250 tenderRecord) throws Exception {
		sqlSession.insert(MAPPER + "addTenderRecord", tenderRecord);
	}

	@Override
	public void updateTenderOrder(int orderId, int recordId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("orderId", orderId);
		params.put("recordId", recordId);
		sqlSession.update(MAPPER + "updateTenderOrder", params);
	}

	@Override
	public int countTenderOrderOfDQR(int bidId, int tenderId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("bidId", bidId);
		params.put("tenderId", tenderId);
		return sqlSession.selectOne(MAPPER + "countTenderOrderOfDQR", params);
	}
	
	@Override
	public T6230 getBidByCode(String loanCode) throws Exception {
		T6230 t6230=sqlSession.selectOne(MAPPER + "getBidByCode", loanCode);
		return t6230;
	}
}
