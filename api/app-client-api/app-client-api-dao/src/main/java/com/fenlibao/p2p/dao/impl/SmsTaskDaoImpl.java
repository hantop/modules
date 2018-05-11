package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.SmsTaskDao;
import com.fenlibao.p2p.model.vo.sms.SmsTaskVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送短信定时任务
 * @author Mingway.Xu
 * @date 2016/12/6 14:34
 */
@Repository
public class SmsTaskDaoImpl implements SmsTaskDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "SmsTaskMapper.";

    public List<SmsTaskVO> getTaskByType(int maxCount, int messageType){
        Map<String, Object> map = new HashMap<>();
        map.put("maxCount", maxCount);
        map.put("messageType", messageType);
        return sqlSession.selectList(MAPPER+"getTaskListByType",map);
    }

    @Override
    public int updateTaskStatusById(String status, long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("id", id);

        return sqlSession.update(MAPPER+"updateTaskStatusById",map);
    }

    @Override
    public int insertSmsRecord(long id, boolean success, String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        if (success) {
            map.put("status", "YES");
        }else{
            map.put("status", "NO");
        }

        map.put("code", code);

        return sqlSession.insert(MAPPER+"insertSmsRecord",map);
    }

}
