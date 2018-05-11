package com.fenlibao.p2p.sms.service;

import com.fenlibao.p2p.emay.wsdl.*;
import com.fenlibao.p2p.sms.config.EmayInfo;
import com.fenlibao.p2p.sms.defines.EmayCodeMsg;
import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;
import com.fenlibao.p2p.sms.domain.MessageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/8/29.
 */
@Service(Constants.EMAY)
public class EmayService extends AbsTemplateSmsService implements ISmsServer {
    private static final Logger log = LoggerFactory.getLogger(EmayService.class);
    private static final String WSDL = "http://sdk4rptws.eucp.b2m.cn:8080/sdk/SDKService?wsdl";
    private static final String WSDL_CONTEXT_PATH = "com.fenlibao.p2p.emay.wsdl";

    private ObjectFactory objectFactory = new ObjectFactory();

    @Autowired
    private ConfigService configService;

    protected EmayInfo emayInfo;//配置信息

    private WebServiceTemplate webServiceTemplate;

    @PostConstruct
    public void init() {
        log.info("初始化{}配置信息",this.getSmsServerName());
        this.webServiceTemplate = new WebServiceTemplate(){};
        webServiceTemplate.setDefaultUri(defaultUri());
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        this.emayInfo = configService.selectByType(EmayInfo.TYPE,EmayInfo.class);
    }

    private String defaultUri() {
        return WSDL;
    }

    private Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(WSDL_CONTEXT_PATH);
        return marshaller;
    }

    @Override
    protected ISmsServer getSmsServer(MTPack pack) {
        return this;
    }

    @Override
    public GsmsResponse sendMsg(MTPack pack) throws Exception {
        pack.setServerType(MTPack.ServerType.EMAY);
        pack.setTunnel(MTPack.ServerType.EMAY.getName());
        List<MessageData> messageDatas = pack.getMsgs();
        List<String> mobiles = new ArrayList<>();
        String content = "";
        for (MessageData messageData : messageDatas) {
            mobiles.add(messageData.getPhone());
            content = messageData.getContent();
        }
        content = this.emayInfo.getSign() + content;
        int priority = pack.getPriority();

        /******************************************************************************************/
        SendSMS sendSMS = new SendSMS();

        sendSMS.setArg0(this.emayInfo.getSoftwareSerialNo());//软件序列号
        sendSMS.setArg1(this.emayInfo.getKey());//用户自定义key值
        sendSMS.getArg3().addAll(mobiles);//手机号码
        sendSMS.setArg4(content);//短信内容
        sendSMS.setArg7(priority);
//        sendSMS.setArg8(pack.getId());

        JAXBElement<SendSMS> element = objectFactory.createSendSMS(sendSMS);
        JAXBElement<SendSMSResponse> response = (JAXBElement<SendSMSResponse>) webServiceTemplate.marshalSendAndReceive(element);

        /******************************************************************************************/
        int result = response.getValue().getReturn();
        EmayCodeMsg code = EmayCodeMsg.handCode(result);
        GsmsResponse gsmsResponse = new GsmsResponse();
        gsmsResponse.setMessage(code.getErrmsg());
        gsmsResponse.setAttributes(code.getSource());
        gsmsResponse.setResult(result);
        gsmsResponse.setServerType(this.getSmsServerName());
        return gsmsResponse;
    }

    @Override
    public String getSmsServerName() {
        return "易美短信";
    }

    @Override
    public MTPack.ServerType getServerType() {
        return MTPack.ServerType.EMAY;
    }

    /**
     * 获取余额
     * @return
     */
    public double getBalance() {
        GetBalance getBalance = this.objectFactory.createGetBalance();
        getBalance.setArg0(this.emayInfo.getSoftwareSerialNo());//软件序列号
        getBalance.setArg1(this.emayInfo.getKey());//用户自定义key值
        JAXBElement<GetBalance> element = this.objectFactory.createGetBalance(getBalance);
        JAXBElement<GetBalanceResponse> response = (JAXBElement<GetBalanceResponse>) webServiceTemplate.marshalSendAndReceive(element);
        if (response != null) return response.getValue().getReturn();
        return -1;
    }
}
