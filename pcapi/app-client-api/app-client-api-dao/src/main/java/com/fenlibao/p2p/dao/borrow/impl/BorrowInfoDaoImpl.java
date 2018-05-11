package com.fenlibao.p2p.dao.borrow.impl;

import com.fenlibao.p2p.dao.borrow.BorrowInfoDao;
import com.fenlibao.p2p.model.entity.borrow.*;
import com.fenlibao.p2p.model.entity.finacing.RepaymentBidInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 借款相关
 * Created by xiao on 2016/12/26.
 */
@Repository
public class BorrowInfoDaoImpl implements BorrowInfoDao {

    private static final String MAPPER = "BorrowInfoMapper.";
    @Resource
    private SqlSession sqlSession;

    /**
     * 获取借款列表 还款中和逾期
     *
     * @return
     */
    public List<BorrowInfo> getBorrowInfoList(Map<String, Object> map, PageBounds pageBounds) {
        return sqlSession.selectList(MAPPER + "getXwBorrowInfoList", map, pageBounds);
    }

    /**
     * 获取借款列表 已还清
     *
     * @return
     */
    public List<BorrowInfo> getYHQBorrowInfoList(Map<String, Object> map, PageBounds pageBounds) {
        return sqlSession.selectList(MAPPER + "getYHQBorrowInfoList", map, pageBounds);
    }

    /**
     * 获取借款统计
     *
     * @return
     */
    public BorrowStaticsInfo getStayRepayStatics(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "getStayRepayStatics", map);
    }

    /**
     * 获取当前期还款信息
     *
     * @return
     */
    public RepayInfo getCurrentRepayStatics(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "getCurrentRepayStatics", map);
    }

    /**
     * 获取提前还款信息
     * @return
     */
    public List<ForwardRepayInfo> getPreRepayStatics(Map<String, Object> map){
        return sqlSession.selectList(MAPPER + "getPreRepayStatics", map);
    }

    @Override
    public List<RepaymentInfo> repaymentList(Map<String, Object> map, PageBounds pageBounds) {
        return sqlSession.selectList(MAPPER + "getRepaymentList", map, pageBounds);
    }

    @Override
    public RepaymentBidInfo getRepaymentBidInfo(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "getRepaymentBidInfo", map);
    }

    @Override
    public BigDecimal getRefundAmount(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "getRefundAmount", map);
    }

    @Override
    public BigDecimal getBorrowerBalance(Integer userId) {
        return sqlSession.selectOne(MAPPER + "getBorrowerBalance", userId);
    }

    @Override
    public BigDecimal getBorrowerFrozen(Integer userId) {
        return sqlSession.selectOne(MAPPER + "getBorrowerFrozen", userId);
    }

    @Override
    public BigDecimal getBorrowTotal(Integer userId) {
        return sqlSession.selectOne(MAPPER + "getBorrowTotal", userId);
    }

    @Override
    public List<RepaymentInfo> repaymentByTypeList(Map<String, Object> map, PageBounds pageBounds) {
        return sqlSession.selectList(MAPPER + "getRepaymentByTypeList", map, pageBounds);
    }

    @Override
    public BorrowerDetail getBorrowerDetail(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "getBorrowerDetail", map);
    }
}
