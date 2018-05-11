package com.fenlibao.p2p.sms.service;

import com.fenlibao.p2p.sms.service.ConfigService;
import com.fenlibao.p2p.sms.config.UserVerify;
import com.fenlibao.p2p.sms.defines.ResultCode;
import com.fenlibao.p2p.sms.defines.SmsFrontCode;
import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Created by Administrator on 2015/9/7.
 */
@Service
public class SmsService {
    private static final Logger log = LoggerFactory.getLogger(SmsService.class);
    @Autowired
    private SmsLogicService smsLogicService;

    @Autowired
    private ConfigService configService;

    private UserVerify userVerify = new UserVerify();

    @PostConstruct
    public void init() {
        this.userVerify = this.configService.selectByType(UserVerify.TYPE,UserVerify.class);
    }

    /**
     * 发送短信，在发送短信之前做用户合法验证
     *
     * @param pack
     * @return
     */
    public GsmsResponse sendMessage(MTPack pack) {
        if (this.userVerify.getUsername().equals(pack.getUsername()) && this.userVerify.getPassword().equals(pack.getPassword())) {
            if (pack.getMsgs() == null || pack.getMsgs().isEmpty()) {
                GsmsResponse gsmsResponse = new GsmsResponse();
                gsmsResponse.setCreateTime(System.currentTimeMillis());
                gsmsResponse.setResult(ResultCode.ERROR_CODE_$9020.getErrorcode());
                gsmsResponse.setAttributes(ResultCode.ERROR_CODE_$9020.getSource());
                gsmsResponse.setMessage(ResultCode.ERROR_CODE_$9020.getErrmsg());
                gsmsResponse.setUuid(UUID.randomUUID());
                log.error(ResultCode.ERROR_CODE_$9020.getErrmsg());
                return gsmsResponse;
            }
            return this.smsLogicService.send(pack);
        } else {
            GsmsResponse gsmsResponse = new GsmsResponse();
            gsmsResponse.setCreateTime(System.currentTimeMillis());
            gsmsResponse.setResult(SmsFrontCode.ERROR_CODE_$10000.getErrorcode());
            gsmsResponse.setAttributes(SmsFrontCode.ERROR_CODE_$10000.getSource());
            gsmsResponse.setMessage(SmsFrontCode.ERROR_CODE_$10000.getErrmsg());
            gsmsResponse.setUuid(UUID.randomUUID());
            log.error(SmsFrontCode.ERROR_CODE_$10000.getErrmsg());
            return gsmsResponse;
        }
    }
}
