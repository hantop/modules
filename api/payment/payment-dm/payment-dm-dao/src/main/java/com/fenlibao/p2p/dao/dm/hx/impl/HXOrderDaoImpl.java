package com.fenlibao.p2p.dao.dm.hx.impl;

import com.fenlibao.p2p.dao.dm.hx.HXOrderDao;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2016/12/24.
 */
@Repository
public class HXOrderDaoImpl implements HXOrderDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "HXOrderMapper.";

    @Override
    public List<HXOrder> get(OrderStatus state, HXTradeType tradeType) {
        Map<String, Integer> params = new HashMap<>(2);
        params.put("state", state.getCode());
        if (tradeType != null) {
            params.put("tradeType", tradeType.getBusiCode());
        }
        return sqlSession.selectList(MAPPER + "get", params);
    }
}
