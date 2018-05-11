package com.fenlibao.p2p.weixin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fenlibao.p2p.weixin.component.ConnectionSettings;
import com.fenlibao.p2p.weixin.component.HttpComponent;
import com.fenlibao.p2p.weixin.defines.MsgType;
import com.fenlibao.p2p.weixin.defines.WeixinStatus;
import com.fenlibao.p2p.weixin.domain.RobotMsg;
import com.fenlibao.p2p.weixin.domain.Subscribe;
import com.fenlibao.p2p.weixin.message.AutoLogin;
import com.fenlibao.p2p.weixin.message.FenlibaoApi;
import com.fenlibao.p2p.weixin.message.Message;
import com.fenlibao.p2p.weixin.message.Type;
import com.fenlibao.p2p.weixin.message.template.TemplateMsg;
import com.fenlibao.p2p.weixin.message.template.TemplateMsgData;
import com.fenlibao.p2p.weixin.repository.RobotMsgRepository;
import com.fenlibao.p2p.weixin.repository.TemplateMsgRepository;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

/**
 * Created by Administrator on 2015/6/18.
 */
@Component
public class WeixinMessageHandler implements IMessageHandler, Constants {

    private final static Logger log = LoggerFactory.getLogger(WeixinMessageHandler.class);

    @Autowired
    private Environment env;
    private String activeProfiles;

    private static final String PREFIX_QR_SUBSCRIBE = "subscribe_qrscene_";

    public static final String DEFAULT_EVENT_KEY = "3000";

    @Autowired
    private ConnectionSettings connectionSettings;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private RobotMsgRepository robotMsgRepository;

    @Autowired
    private TemplateMsgRepository templateMsgRepository;

    @Autowired
    private HttpComponent httpComponent;

    @Value("${fenlibao.version}")
    private String version;

    @Value("${fenlibao.authorization}")
    private String authorization;
    
    @Value("${fenlibao.openAutoCs}")
    private String openAutoCs;

    @PostConstruct
    public void init() {
        String[] activeProfiles = env.getActiveProfiles();
        if (activeProfiles != null && activeProfiles.length > 0) {
            this.activeProfiles = activeProfiles[0];
        } else {
            this.activeProfiles = "test";
        }
    }

    @Override
    public Message clickEvent(Message message, IWxApi wxApi) {
        String opendid = message.getFromUserName();
        Message result = new Message();
        result.setFromUserName(message.getToUserName());
        result.setToUserName(message.getFromUserName());
        result.setCreateTime(System.currentTimeMillis());
        result.setMsgType(MsgType.text);
        String content;
        if (CLICK_LOGIN_BIND.equals(message.getEventKey())) {  // 免登陆功能按钮key
            //免登陆功能按钮
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("openId", opendid);
            FenlibaoApi<AutoLogin> fenlibaoApi = this.httpsPost(this.connectionSettings.getServerBind(), paramsMap, new TypeReference<FenlibaoApi<AutoLogin>>() {
            });

            if (fenlibaoApi == null || fenlibaoApi.getData() != null && fenlibaoApi.getData().getWeixinStatus() != WeixinStatus.BINDED) {
                //绑定提示
                content = this.robotMsgByKeyword("开启免登录", Type.CLICK, message.getFromUserName());
            } else {
                content = this.robotMsgByKeyword("取消免登录", Type.CLICK, message.getFromUserName());
            }

        } else if (CLICK_WEIXIN_CUSTOMER.equals(message.getEventKey())) {//微信客服
            int dayOfWeek = this.getDayOfWeek();
            if (dayOfWeek > 1 && dayOfWeek < 7) {
                content = this.robotMsgByKeyword("工作日客服消息", Type.CLICK, message.getFromUserName());
            } else {
                content = this.robotMsgByKeyword("休息日客服消息", Type.CLICK, message.getFromUserName());
            }
        } else {
            content = this.robotMsgByKeyword(message.getEventKey(), Type.CLICK, message.getFromUserName());
        }

        if (content != null) {
            result.setContent(content);
            return result;
        } else {
            return null;
        }
    }


    public FenlibaoApi<AutoLogin> httpsPost(String url, Map<String, String> params, TypeReference<FenlibaoApi<AutoLogin>> typeReference) {
        FenlibaoApi fenlibaoApis = httpComponent.post(url, params, typeReference, new BasicHeader[]{
                //new BasicHeader("Version", version),
                new BasicHeader("Authorization", authorization)
        });
        return fenlibaoApis;
    }


    /**
     * 获取当前时间是星期几
     * 注意：
     * 1：星期日
     * 2：星期一
     * 3：星期二
     * 4：星期三
     * 5：星期四
     * 6：星期五
     * 7：星期六
     *
     * @return
     */
    private int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public Message subscribe(Message message) {
        String eventKey = serializeSubscribe(message);//序列化渠道
        Message result = new Message();
        result.setFromUserName(message.getToUserName());
        result.setToUserName(message.getFromUserName());
        result.setCreateTime(System.currentTimeMillis());
        result.setMsgType(MsgType.text);
        String content = this.robotMsgByEventKey(PREFIX_QR_SUBSCRIBE, eventKey, Type.CONTENT);
        result.setContent(content);
        return result;
    }


    /**
     * 消息处理
     *
     * @param message
     * @return
     */
    @Override
    public Message multiService(Message message) {
        String content = this.robotMsgByKeyword(message.getContent(), Type.CONTENT, message.getFromUserName());
        if (null == content) {
        	if(Boolean.parseBoolean(openAutoCs)){
        		content = this.robotMsgByKeyword("非关键字自动回复", Type.CONTENT, message.getFromUserName());
        	}
        	else{
                String toUserName = message.getFromUserName();
                message.setFromUserName(message.getToUserName());
                message.setToUserName(toUserName);
                message.setMsgType(MsgType.transfer_customer_service);
                return message;
        	}
        }

        Message result = new Message();
        result.setFromUserName(message.getToUserName());
        result.setToUserName(message.getFromUserName());
        result.setCreateTime(System.currentTimeMillis());
        result.setMsgType(MsgType.text);
        result.setContent(content);
        return result;
    }

    /**
     * 序列化关注渠道
     *
     * @param message
     */
    private String serializeSubscribe(Message message) {
        String eventKey = message.getEventKey();
        String openId = message.getFromUserName();
        if (eventKey != null && eventKey.startsWith("qrscene_")) {
            eventKey = eventKey.substring("qrscene_".length());
        } else if (eventKey == null || eventKey.equals("")) {
            eventKey = DEFAULT_EVENT_KEY;
        }
        this.subscribeService.save(new Subscribe(openId, eventKey));
        return eventKey;
    }


    /**
     * 获取关注渠道编码，换行和渠道码生成
     *
     * @param content
     * @return
     */
    private String parseTxt(String content, String openId) {
        String eventKey = this.subscribeService.findEventKeyByOpendId(openId);
        return fillEventKey(content, eventKey);
    }

    /**
     * 填充eventkey和换行贴换
     *
     * @param content
     * @param eventKey
     * @return
     */
    private String fillEventKey(String content, String eventKey) {
        if (eventKey == null || "".equals(eventKey)) eventKey = DEFAULT_EVENT_KEY;
        return content.replaceAll("\\\\n", "\n").replaceAll("#eventKey", eventKey);
    }

    @Autowired
    DataSource dataSource;

    /**
     * 判断是否包含关键字，并返回关键字对应的回复内容
     *
     * @param keyword
     * @return
     */
    private String robotMsgByKeyword(String keyword, Type type, String openId) {
        if (keyword == null) return null;
        List<RobotMsg> robotMsgs = this.robotMsgRepository.findActiveProfilesAndType(activeProfiles, type.toString());
        for (RobotMsg robotMsg : robotMsgs) {
            String keywords = robotMsg.getKeyword();
            if (Arrays.asList(keywords.split(";")).contains(keyword)) {
                return this.parseTxt(robotMsg.getContent(), openId);
            }
        }
        return null;
    }

    private String robotMsgByEventKey(String prefix, String eventKey, Type type) {
        List<RobotMsg> robotMsgs = this.robotMsgRepository.findActiveProfilesAndType(activeProfiles, type.toString());
        // 根据不同的渠道回复该渠道对应的信息
        for (RobotMsg robotMsg : robotMsgs) {
            String keywords = robotMsg.getKeyword();
            if (Arrays.asList(keywords.split(";")).contains(prefix + eventKey)) {
                return this.fillEventKey(robotMsg.getContent(), eventKey);
            }
        }
        // 如果渠道没有找到回复信息，统一回复关注信息
        String keyword = "关注";
        for (RobotMsg robotMsg : robotMsgs) {
            String keywords = robotMsg.getKeyword();
            if (Arrays.asList(keywords.split(";")).contains(keyword)) {
                return this.fillEventKey(robotMsg.getContent(), eventKey);
            }
        }
        return null;
    }

    @Override
    public Message kfCloseSessionEvent(Message message) {
        return null;
    }

    /**
     * 保存到数据库
     *
     * @param result
     * @return
     */
    @Async
    @Override
    public void completeTemplateMsg(TemplateMsg result) {
        com.fenlibao.p2p.weixin.domain.TemplateMsg serializeTemplateMsg = new com.fenlibao.p2p.weixin.domain.TemplateMsg();
        BeanUtils.copyProperties(result, serializeTemplateMsg);
        serializeTemplateMsg.setCreateTime(System.currentTimeMillis() / 1000);
        List<com.fenlibao.p2p.weixin.domain.TemplateMsgData> serializeTemplateMsgDatas = new ArrayList<>();
        for (Map.Entry<String, TemplateMsgData> entry : result.getData().entrySet()) {
            com.fenlibao.p2p.weixin.domain.TemplateMsgData serializeTemplateMsgData = new com.fenlibao.p2p.weixin.domain.TemplateMsgData();
            BeanUtils.copyProperties(entry.getValue(), serializeTemplateMsgData);
            serializeTemplateMsgData.setName(entry.getKey());
            serializeTemplateMsgDatas.add(serializeTemplateMsgData);
        }
        serializeTemplateMsg.setTemplateMsgDatas(serializeTemplateMsgDatas);
        this.templateMsgRepository.save(serializeTemplateMsg);
        System.out.println(serializeTemplateMsg);
    }

    @Async
    @Override
    public void completeStatusTemplateMsg(Message message) {
        this.templateMsgRepository.setStatusFor(message.getMsgID(), message.getStatus());
    }
}
