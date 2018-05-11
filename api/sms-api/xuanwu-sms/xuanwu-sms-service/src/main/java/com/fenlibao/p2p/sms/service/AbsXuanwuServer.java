package com.fenlibao.p2p.sms.service;

import com.fenlibao.p2p.sms.config.XuanWuConfig;
import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;
import com.fenlibao.p2p.sms.domain.MessageData;
import com.fenlibao.p2p.xuanwu.wsdl.ArrayOfMessageData;
import com.fenlibao.p2p.xuanwu.wsdl.MTPacks;
import com.fenlibao.p2p.xuanwu.wsdl.Post;
import com.fenlibao.p2p.xuanwu.wsdl.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.annotation.PostConstruct;

/**
 * Created by Administrator on 2015/8/28.
 */
public abstract class AbsXuanwuServer extends AbsTemplateSmsService implements ISmsServer {

    private static final Logger log = LoggerFactory.getLogger(AbsXuanwuServer.class);

    private static final String WSDL_CONTEXT_PATH = "com.fenlibao.p2p.xuanwu.wsdl";
    private static final String WSDL_MOS = "http://211.147.239.62/Service/WebService.asmx?wsdl";
    private static final String SOAP_ACTION_POST = "http://www.139130.net/Post";

    private WebServiceTemplate webServiceTemplate;

    @Autowired
    private ConfigService configService;

    private XuanWuConfig xuanWuConfig;


    /**
     * 获取发送短信的通道
     *
     * @return
     */
    public abstract String getTunnel();

    @PostConstruct
    public void init() {
        log.info("初始化{}配置信息",this.getSmsServerName());
        this.webServiceTemplate = new WebServiceTemplate(){};
        webServiceTemplate.setDefaultUri(defaultUri());
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        xuanWuConfig = this.configService.selectByType(this.getTunnel(), XuanWuConfig.class);
    }

    private String defaultUri() {
        return WSDL_MOS;
    }

    @Override
    public GsmsResponse sendMsg(MTPack pack) {
        /***************************************************************************************/
        //短信信息
        MTPacks mtPacks = new MTPacks();
        String uuid = pack.getBatchID().toString();
        mtPacks.setBatchID(uuid);//信息批次唯一序号，不可重复
        mtPacks.setUuid(uuid);//与batchID一致
        mtPacks.setBatchName(pack.getBatchName());
        mtPacks.setSendType(pack.getSendType().getIndex());// 发送类型： 群发(0默认)  组发(1)  单发是以上两者的特例
        mtPacks.setMsgType(pack.getMsgType().getIndex());//消息类型：
        mtPacks.setBizType(pack.getBizType());//信息业务类型
        mtPacks.setDistinctFlag(pack.isDistinctFlag());//是否过滤重复号码，默认false
        mtPacks.setScheduleTime(pack.getScheduleTime());//计划发送时间
        mtPacks.setRemark(pack.getRemark());//备注
        mtPacks.setCustomNum(pack.getCustomNum());//用户扩展码
        mtPacks.setDeadline(pack.getDeadline());//下发截至时间
        //设置短信手机号信息
        ArrayOfMessageData arrayOfMessageData = new ArrayOfMessageData();
        for (MessageData messageData : pack.getMsgs()) {
            com.fenlibao.p2p.xuanwu.wsdl.MessageData msg = new com.fenlibao.p2p.xuanwu.wsdl.MessageData();
            BeanUtils.copyProperties(messageData, msg);
            arrayOfMessageData.getMessageData().add(msg);
        }
        mtPacks.setMsgs(arrayOfMessageData);

        //post
        Post post = new Post();
        post.setAccount(xuanWuConfig.getUsername());//用户名
        post.setPassword(xuanWuConfig.getPassword());//密码
        post.setMtpack(mtPacks);

        PostResponse postResponse = (PostResponse) webServiceTemplate.marshalSendAndReceive(post, new SoapActionCallback(SOAP_ACTION_POST));
        GsmsResponse response = new GsmsResponse();
        BeanUtils.copyProperties(postResponse.getPostResult(), response);
        return response;
        /***************************************************************************************/
    }

    @Override
    protected ISmsServer getSmsServer(MTPack pack) {
        return this;
    }

    private Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(WSDL_CONTEXT_PATH);
        return marshaller;
    }
}
