package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.model.entity.SendSmsRecord;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SendSmsRecordDaoImpl implements SendSmsRecordDao {

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "SendSmsRecordMapper.";

    @Override
    public int insertSendSmsRecord(SendSmsRecord record) {
        return sqlSession.insert(MAPPER + "insertSendSmsRecord", record);
    }

    @Override
    public int userSendPhoneCount(Map map) {
        return sqlSession.selectOne(MAPPER + "userSendPhoneCount", map);
    }

    @Override
    public int getSmsDateDiff(String content, String phoneNum) {
        Map<String, Object> map = new HashMap();
        map.put("content", content);
        map.put("phoneNum", phoneNum);
        return sqlSession.selectOne(MAPPER + "getSmsDateDiff", map);
    }

    @Override
    public Integer insertT1040(Integer type, String content) throws Exception {
        Map<String, Object> params = new HashMap<>(2);
        params.put("type", type);
        params.put("content", content);
        sqlSession.insert(MAPPER + "insertT1040", params);
        return Integer.parseInt(String.valueOf(params.get("id")));
    }

    @Override
    public void insertT1041(Integer msgId, String mobile) throws Exception {
        Map<String, Object> params = new HashMap<>(2);
        params.put("msgId", msgId);
        params.put("mobile", mobile);
        sqlSession.insert(MAPPER + "insertT1041", params);
    }


}
