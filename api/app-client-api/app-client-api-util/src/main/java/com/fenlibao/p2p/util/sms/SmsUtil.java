package com.fenlibao.p2p.util.sms;

import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 发送短信
 */
public class SmsUtil {

    private static final Logger logger = LogManager.getLogger(SmsUtil.class);

    /**
     * 发送短信
     *
     * @param phoneNum
     * @param content
     * @return
     */
    public static int sendSms(String phoneNum, String content) {
        int result = sendXWSms(new String[]{phoneNum}, content);
        return result;
    }

    /**
     * 易美短信
     *
     * @param mobiles
     * @param content
     * @param priority (短信发送优先级1-5  5最高)
     * @return
     */
    private static int sendYMSms(String[] mobiles, String content, int priority) {
        try {
            String httpUrl = Sender.get("sms.ym.url");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/json");
            headers.add("Content-Type", "application/json;charset=utf-8");

            SmsVo vo = new SmsVo();
            vo.setMobiles(mobiles);
            vo.setContent(content);
            vo.setPriority(priority);
            vo.setUsername(Sender.get("sms.ym.username"));
            vo.setPassword(Sender.get("sms.ym.password"));
            vo.setCharset("UTF-8");
            String requestJson = Jackson.getBaseJsonData(vo);

            HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<SmsVo> result = restTemplate.postForEntity(httpUrl, entity, SmsVo.class);

            return result.getBody().getCode();
        } catch (Exception ex) {
            logger.error("调用易美短信接口异常", ex);
            return -1;
        }
    }

    /**
     * 玄武短信
     *
     * @param mobiles
     * @param content
     * @return
     */
    private static int sendXWSms(String[] mobiles, String content) {
        try {
            String httpUrl = Sender.get("sms.xw.url");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/json");
            headers.add("Content-Type", "application/json;charset=utf-8");

            ArrayList<MessageData> msgs = new ArrayList<MessageData>();
            for (String mobile : mobiles) {
                MessageData vo = new MessageData(mobile, content);
                msgs.add(vo);
            }

            MTPack pack = new MTPack();
            pack.setServerType(MTPack.ServerType.EMAY);
            pack.setBatchID(UUID.randomUUID());
            pack.setMsgType(MTPack.MsgType.SMS);
            pack.setSendType(MTPack.SendType.GROUP);
            pack.setUsername(Sender.get("sms.xw.username"));
            pack.setPassword(Sender.get("sms.xw.password"));
            pack.setMsgs(msgs);

            HttpEntity<MTPack> entity = new HttpEntity<MTPack>(pack, headers);
            RestTemplate restTemplate = new RestTemplate();
            GsmsResponse result = restTemplate.postForObject(httpUrl, entity, GsmsResponse.class);

            return result.getResult();
        } catch (Exception ex) {
            logger.error("调用玄武短信接口异常", ex);
            return -1;
        }
    }

}
