package com.fenlibao.p2p.dao.xinwang.credit.impl;

import com.fenlibao.p2p.dao.xinwang.credit.SysTransferDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2017/6/1 15:39
 */
@Repository
public class SysTransferDaoImpl implements SysTransferDao {
    private final String MAPPER = "SysTransferMapper.";
    @Resource
    private SqlSession sqlSession;

    @Override
    public void saveRequestNo(int applyId, String requestNo) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("applyId", applyId);
        params.put("requestNo", requestNo);

        sqlSession.update(MAPPER + "saveRequestNo", params);
    }
}
