package com.fenlibao.p2p.alidayu.sms.service;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.aliday.sms.persistence.AlidayuMapper;
import com.fenlibao.p2p.alidayu.sms.config.AlidayuConfig;
import com.fenlibao.p2p.alidayu.sms.domain.Alidayu;
import com.fenlibao.p2p.alidayu.sms.domain.AlidayuItem;
import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;
import com.fenlibao.p2p.sms.domain.MessageData;
import com.fenlibao.p2p.sms.service.AbsTemplateSmsService;
import com.fenlibao.p2p.sms.service.ConfigService;
import com.fenlibao.p2p.sms.service.Constants;
import com.fenlibao.p2p.sms.service.ISmsServer;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注意：目前只能处理注册验证码
 * Created by Bogle on 2016/2/24.
 */
@Service(Constants.ALIDAYU)
public class AlidayuService extends AbsTemplateSmsService implements ISmsServer {

    private static final Logger log = LoggerFactory.getLogger(AlidayuService.class);

    // key:为业务名称，值为数据格式和正则表达式内容，模板id等信息，
    private Map<String, Alidayu> alidayus;

    @Autowired
    private AlidayuMapper alidayuMapper;

    @Autowired
    private ConfigService configService;

    private AlidayuConfig alidayuConfig;

    private TaobaoClient client;

    @PostConstruct
    public void init() {
        log.info("初始化阿里大鱼配置信息...");
        alidayus = new HashMap<>();
        List<Alidayu> list = alidayuMapper.find();
        for (Alidayu alidayu : list) {
            alidayus.put(alidayu.getBizCode(), alidayu);
        }
        this.alidayuConfig = configService.selectByType(AlidayuConfig.TYPE, AlidayuConfig.class);
        Assert.notEmpty(this.alidayus, "短信格式信息不能为空");
        this.client = new DefaultTaobaoClient(this.alidayuConfig.getUrl(), this.alidayuConfig.getAppkey(), this.alidayuConfig.getSecret());
    }

    @Override
    protected ISmsServer getSmsServer(MTPack pack) {
        return this;
    }


    @Override
    public GsmsResponse sendMsg(MTPack pack) throws Exception {
        pack.setServerType(MTPack.ServerType.ALIDAYU);
        pack.setTunnel(MTPack.ServerType.ALIDAYU.getName());
        List<MessageData> messageDatas = pack.getMsgs();
        GsmsResponse gsmsResponse = new GsmsResponse();
        for (int i = 0; i < messageDatas.size(); i++) {
            MessageData messageData = messageDatas.get(i);
            Alidayu alidayu = alidayus.get(pack.getBizCode());
            if (alidayu == null) {
                break;
            }
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            String signName = null == alidayu.getSignName() ? this.alidayuConfig.getSignName() : alidayu.getSignName();
            req.setExtend(pack.getCustomNum());
            req.setSmsFreeSignName(signName);
            req.setSmsType("normal");
            String content = messageData.getContent();
            Map<String, Object> params = new HashMap<>();
            for (AlidayuItem alidayuItem : alidayu.getAlidayuItems()) {
                Pattern pattern = Pattern.compile(alidayuItem.getPattern());
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String value = matcher.group();
                    value = value.substring(alidayuItem.getStartIndex(), value.length() - alidayuItem.getEndIndex());
                    params.put(alidayuItem.getKeyName(), value);
                } else {
                    throw new IllegalArgumentException("\n" + this.getServerType() + "\n短信内容：" + messageData.getContent() + "\n业务：" + pack.getBizCode() + "\n正则表达式：" + alidayuItem.getPattern() + "\nalidayuFormat:" + JSON.toJSONString(alidayuItem) + "\n正则表达式不匹配");
                }
            }
            if (!params.isEmpty())
                content = JSON.toJSONString(params);
            else
                content = null;
            req.setSmsParamString(content);
            req.setRecNum(messageData.getPhone());
            req.setSmsTemplateCode(alidayu.getTemplateCode());
            AlibabaAliqinFcSmsNumSendResponse rsp = null;
            try {
                rsp = client.execute(req);
            } catch (ApiException e) {
                gsmsResponse.setAttributes(e.getErrMsg());
            } finally {
                gsmsResponse.setCreateTime(System.currentTimeMillis());
                gsmsResponse.setMessage(rsp.getMsg());
                gsmsResponse.setAttributes(gsmsResponse.getAttributes() == null ? rsp.getBody() : gsmsResponse.getAttributes());
                gsmsResponse.setServerType(this.getSmsServerName());
                gsmsResponse.setResult(rsp.isSuccess() ? 0 : -1);
            }
            break;
        }
        return gsmsResponse;

    }

    @Override
    public String getSmsServerName() {
        return "阿里大鱼";
    }

    @Override
    public MTPack.ServerType getServerType() {
        return MTPack.ServerType.ALIDAYU;
    }
}
