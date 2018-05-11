package com.fenlibao.p2p.dao.bank.impl;

import com.fenlibao.p2p.dao.bank.BankDao;
import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.vo.BankCardVO;
import com.fenlibao.p2p.model.vo.BankVO;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by LouisWang on 2015/8/15.
 */
@Repository
public class BankDaoImpl implements BankDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "BankCardMapper.";

    @Override
    public List<BankCard> getBankCardsByUserId(int userId) {
        return sqlSession.selectList(MAPPER + "getBankCardsByUserId", userId);
    }

    @Override
    public List<BankCardVO> getBankCars(int acount, String status) {
     //   return sqlSession.selectList(MAPPER + "getBankList", userId);
        return null;
    }

    @Override
    public Map getUserDealStatus(Integer userId) throws Exception {
        return sqlSession.selectOne(MAPPER + "getUserDealStatus",userId);
    }

    @Override
    public BankCardVO checkBankCardInfo(Integer userId) throws Exception {

        return sqlSession.selectOne(MAPPER + "checkBankCardInfo",userId);
    }

    @Override
    public Map<String,Object> getBankCardById(int id) throws Exception {
        return sqlSession.selectOne(MAPPER + "getBankCardById",id);
    }

    @Override
    public BankVO getBank(int bankId) {
        return sqlSession.selectOne(MAPPER + "getBank",bankId);
    }

    @Override
    public Map getBankCardMsg(Map query) throws Throwable {

        return sqlSession.selectOne(MAPPER + "getBankCardMsg",query);
    }

	@Override
	public int updateBankCardInfo(BankCardVO vo) throws Exception {
		return sqlSession.update(MAPPER + "updateBankCardInfo", vo);
	}

	@Override
	public int getIdByCode(String code) {
		return sqlSession.selectOne(MAPPER + "getIdByCode", code);
	}

	@Override
	public String getBankCardBindStatus(int userId) {
		return sqlSession.selectOne(MAPPER + "isBindBankCard", userId);
	}

    @Override
    public PaymentLimitVO getPaymentLimitByUserId(String userId) {
        return sqlSession.selectOne(MAPPER + "getPaymentLimitByUserId", userId);
    }

    @Override
    public List<String> getQYBankCode() {
        return sqlSession.selectList(MAPPER + "getQYBankCode");
    }
}
