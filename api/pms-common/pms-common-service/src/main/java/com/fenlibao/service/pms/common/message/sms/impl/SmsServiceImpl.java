package com.fenlibao.service.pms.common.message.sms.impl;

import com.fenlibao.dao.pms.common.message.sms.SendSmsRecordExtMapper;
import com.fenlibao.dao.pms.common.message.sms.SendSmsRecordMapper;
import com.fenlibao.model.pms.common.message.sms.SendSmsRecord;
import com.fenlibao.model.pms.common.message.sms.SendSmsRecordExt;
import com.fenlibao.service.pms.common.message.sms.SmsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 短信
 * Created by chenzhixuan on 2015/12/15.
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    private SendSmsRecordMapper sendSmsRecordMapper;
    @Resource
    private SendSmsRecordExtMapper sendSmsRecordExtMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void sendMsg(String phoneNum, String content) {
        // 过期时间
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);
        SendSmsRecord record = new SendSmsRecord();
        record.setContent(content);
        record.setCreateTime(new Date());
        record.setStatus("W");
        record.setType(0);
        record.setOutTime(outTime);
        // 待发送短信表
        sendSmsRecordMapper.insertSendSmsRecord(record);
        // 短信息与手机号关系表
        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phoneNum);
        sendSmsRecordExtMapper.insertSendSmsRecordExt(recordExt);
    }
}
