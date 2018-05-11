package com.fenlibao.p2p.sms.service;

import com.fenlibao.p2p.sms.config.defines.SmsConfig;
import com.fenlibao.p2p.sms.defines.ResultCode;
import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2015/9/6.
 */
@Service
public class SmsLogicService {
    private static final Logger log = LoggerFactory.getLogger(SmsLogicService.class);
    private static ThreadLocal<AtomicInteger> atomicInteger = new ThreadLocal<>();
    @Value("${alidayu.enable}")
    private Boolean alidayuEnable = false;
    @Autowired
    private Map<String, AbsTemplateSmsService> smsServiceMap;

    @Autowired
    private ConfigService configService;

    private SmsConfig smsConfig;

    @PostConstruct
    public void init() {
        this.smsConfig = this.configService.selectByType(SmsConfig.TYPE, SmsConfig.class);
        log.info("默认短信通道.....{}", smsConfig.getDefaultService());
        if (!this.alidayuEnable) {
            this.smsServiceMap.remove(Constants.ALIDAYU);
        }
    }

    /**
     * 发送短信，在发送短信前进行默认配置检测，如果在用没有指定发送的短信通道时，采用数据库的默认配置完成
     *
     * @param pack
     * @return
     */
    public GsmsResponse send(MTPack pack) {
        if (pack.getMsgs() == null || pack.getMsgs().isEmpty()) {
            GsmsResponse gsmsResponse = new GsmsResponse();
            gsmsResponse.setCreateTime(System.currentTimeMillis());
            gsmsResponse.setResult(ResultCode.ERROR_CODE_$9020.getErrorcode());
            gsmsResponse.setAttributes(ResultCode.ERROR_CODE_$9020.getSource());
            gsmsResponse.setMessage(ResultCode.ERROR_CODE_$9020.getErrmsg());
            return gsmsResponse;
        }
        if (pack.getMsgs().size() > 1) {
            GsmsResponse gsmsResponse = new GsmsResponse();
            gsmsResponse.setCreateTime(System.currentTimeMillis());
            gsmsResponse.setResult(ResultCode.ERROR_CODE_$9021.getErrorcode());
            gsmsResponse.setAttributes(ResultCode.ERROR_CODE_$9021.getSource());
            gsmsResponse.setMessage(ResultCode.ERROR_CODE_$9021.getErrmsg());
            return gsmsResponse;
        }
        String service = (pack.getBizCode() != null && pack.getServerType() == null) ? Constants.ALIDAYU : pack.getServerType() != null ? pack.getServerType().toString() : null;
        if (!this.alidayuEnable) pack.setBizCode(null);
        if (service != null && pack.getBizCode() == null && service.equals(Constants.ALIDAYU)) service = null;
        if (service == null) {
            service = smsConfig.getDefaultService();
            MTPack.ServerType serverType = MTPack.ServerType.valueOf(service);
            if (serverType == null) {
                serverType = MTPack.ServerType.EMAY;
                service = Constants.EMAY;
            }
            pack.setServerType(serverType);
            pack.setTunnel(service);
        }
        log.info("短信通道.....{}", service);
        List<GsmsResponse> results = new ArrayList<>();
        atomicInteger.set(new AtomicInteger(0));
        GsmsResponse response = this.send(pack, service, results);
        atomicInteger.remove();
        results.remove(response);
        response.setErrors(results);
        return response;
    }

    /**
     * 轮询的发送短信，
     *
     * @param pack
     * @param service
     * @return
     */
    private GsmsResponse send(MTPack pack, String service, List<GsmsResponse> results) {
        AbsTemplateSmsService smsService = smsServiceMap.get(service);
        int polls = this.smsConfig.getCount();
        GsmsResponse response = null;
        try {
            pack.setCount(pack.getCount() + 1);
            response = smsService.post(pack);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pack.getCount() < polls - 1 && (response == null || response.getResult() != 0)) {
                service = getService(pack.getBizCode());
                response = this.send(pack, service, results);
            }
        }
        if (response == null) {
            response = new GsmsResponse();
            response.setCreateTime(System.currentTimeMillis());
            response.setUuid(UUID.randomUUID());
            response.setMessage("服务器未知错误");
            response.setAttributes("轮询了" + this.smsConfig.getCount() + "次，无法发送短信");
            response.setResult(-100001);
        }
        return response;
    }

    private String getService(String bizCode) {
        int size = smsServiceMap.keySet().size();
        int index = atomicInteger.get().getAndIncrement() % size;

        String[] arrs = new String[size];
        arrs = smsServiceMap.keySet().toArray(arrs);
        String service = arrs[index];
        if (bizCode == null && service.equals(Constants.ALIDAYU)) {
            return this.getService(bizCode);
        }
        return service;
    }
}
