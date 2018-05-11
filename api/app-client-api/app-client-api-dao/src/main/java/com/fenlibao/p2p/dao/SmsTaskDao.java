package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.vo.sms.SmsTaskVO;

import java.util.List;

/**
 * @author Mingway.Xu
 * @date 2016/12/6 14:34
 */
public interface SmsTaskDao {

    List<SmsTaskVO> getTaskByType(int maxCount, int messageType);

    int updateTaskStatusById(String z, long id);

    int insertSmsRecord(long id, boolean b, String code);

}
