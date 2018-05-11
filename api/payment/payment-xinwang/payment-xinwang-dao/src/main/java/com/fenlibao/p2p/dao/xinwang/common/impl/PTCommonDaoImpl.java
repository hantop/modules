package com.fenlibao.p2p.dao.xinwang.common.impl;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/22.
 */
@Repository
public class PTCommonDaoImpl implements PTCommonDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "PTCommonMapper.";

    @Override
    public long insertT6123(Integer userId, String title, String state){
        Map<String, Object> params = new HashMap<>(3);
        params.put("userId", userId);
        params.put("title", title);
        params.put("state", state);
        sqlSession.insert(MAPPER+"insertT6123", params);
        return (long)(params.get("id")==null?0:params.get("id"));
    }

    @Override
    public void insertT6124(long letterId, String content) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("letterId", letterId);
        params.put("content", content);
        sqlSession.insert(MAPPER+"insertT6124", params);
    }

    @Override
    public Integer insertT1040(Integer type, String content)  {
        Map<String, Object> params = new HashMap<>(2);
        params.put("type", type);
        params.put("content", content);
        sqlSession.insert(MAPPER+"insertT1040", params);
        return Integer.parseInt(String.valueOf(params.get("id")));
    }

    @Override
    public void insertT1041(Integer msgId, String mobile) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("msgId", msgId);
        params.put("mobile", mobile);
        sqlSession.insert(MAPPER+"insertT1041", params);
    }

    @Override
    public String getSystemVariable(String id)  {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return sqlSession.selectOne(MAPPER+"getSystemVariable", params);
    }

    @Override
    public void insertT6102(XWCapitalFlow flow) {
        sqlSession.insert(MAPPER+"insertT6102s", flow);
    }

    @Override
    public Date getCurrentDate() {
        return sqlSession.selectOne(MAPPER+"getCurrentDate");
    }

    @Override
    public void batchInsertT6102(List<XWCapitalFlow> list) {
        Map<String, Object> params = new HashMap<>();
        params.put("list", list);
        sqlSession.insert(MAPPER+"batchInsertT6102",params);
    }

    @Override
    public void batchInsertTransactionExtend(List<XWCapitalFlow> list) {
        Map<String, Object> params = new HashMap<>();
        params.put("list", list);
        sqlSession.insert(MAPPER+"batchInsertTransactionExtend",params);
    }

    @Override
    public int getSpecialUserId() {
        return sqlSession.selectOne(MAPPER + "getSpecialUserId");
    }

    @Override
    public void insertErrorLog(ErrorLogParam errorLogParam) {
        sqlSession.insert(MAPPER + "insertErrorLog", errorLogParam);
    }
}
