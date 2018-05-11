package com.fenlibao.p2p.util.dm;

import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.message.RequestDocument;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.message.body.RequestBody;
import com.fenlibao.p2p.model.dm.message.header.RequestHeader;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.util.dm.security.DES3EncryptAndDecrypt;
import com.fenlibao.p2p.util.dm.security.rsa.MyRSA;
import org.aeonbits.owner.ConfigCache;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

/**
 * 请求/响应报文工具
 * Created by zcai on 2016/8/26.
 */
public class MessageUtil {

    private static final String MSG_HEADER = "001X12          ";
    private static final String SECURITY_LENGTH = "00000256";
    private static final String XML_STATEMENT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"; //自定义，不通过marshaller产生

    /**
     * 组装报文
     * @param businessParams
     * @param tradeCode
     * @return
     * @throws Exception
     */
    public static String getMessageByBusi(ReqBusinessParams businessParams, String tradeCode, String sn, int clientType) throws Exception {
        String dateTime = HXUtil.getSimpleDateTime(new Date());
        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        RequestHeader header = new RequestHeader(config.channelCode(), sn, dateTime.substring(0, 8), dateTime.substring(8), "");
        businessParams.setMERCHANTID(config.merchantId());
        businessParams.setMERCHANTNAME(config.merchantName());
        businessParams.setAPPID(APPType.getType(clientType));
        String busiData = MessageUtil.removeXmlLabel(XMLHelper.toXML(businessParams));
        String data = DES3EncryptAndDecrypt.encrypt(busiData);
        RequestBody body = new RequestBody(tradeCode, data);
        RequestDocument requestDocument = new RequestDocument(body, header);
        return getRequestMessage(requestDocument);
    }

    /**
     * 组装报文
     * @param businessParams
     * @param tradeCode
     * @return
     * @throws Exception
     */
    public static String getMessageForQuery(ReqBusinessParams businessParams, String tradeCode) throws Exception {
        String dateTime = HXUtil.getSimpleDateTime(new Date());
        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        RequestHeader header = new RequestHeader(config.channelCode(),
                HXUtil.getChannelFlowForQuery(tradeCode), dateTime.substring(0, 8), dateTime.substring(8), "");
        businessParams.setMERCHANTID(config.merchantId());
        businessParams.setMERCHANTNAME(config.merchantName());
        businessParams.setAPPID(APPType.PC.getType());//查询默认用PC，如果华兴校验原订单APPID需获取原订单的APPID
        String busiData = MessageUtil.removeXmlLabel(XMLHelper.toXML(businessParams));
        String data = DES3EncryptAndDecrypt.encrypt(busiData);
        RequestBody body = new RequestBody(tradeCode, data);
        RequestDocument requestDocument = new RequestDocument(body, header);
        return getRequestMessage(requestDocument);
    }

    /**
     * 获取封装好的请求报文
     * @param requestDocument
     * @return
     * @throws Exception
     */
    public static String getRequestMessage(RequestDocument requestDocument) throws Exception {
        String xml = XML_STATEMENT.concat(XMLHelper.toXML(requestDocument));
        String sign = MyRSA.getSign(xml);
        return MSG_HEADER.concat(SECURITY_LENGTH).concat(sign).concat(xml);
    }

    /**
     * 去掉xml root用于DES3加密
     * @param targetXml
     * @return
     */
    public static String removeXmlLabel(String targetXml) {
        if (StringUtils.isNotBlank(targetXml)
                && targetXml.contains("<xml>")) {
            return targetXml.replace("<xml>", "").replace("</xml>", "");
        }
        return null;
    }

    /**
     * 验签
     * @param respMsg 响应报文
     * @return
     */
    public static boolean checkSign(String respMsg) throws Exception {
        String privateCiphertext = getPrivateCiphertext(respMsg);
        if (StringUtils.isNotBlank(privateCiphertext)) {
            String myMd5 = MyRSA.MD5(getXmlDocument(respMsg));
            String targetMd5 = MyRSA.getMD5ByPublicKey(privateCiphertext);
            if (myMd5.equals(targetMd5)) {
                System.out.println("验签通过^0^");
                return true;
            }
        }
        return false;
    }

    /**
     * 获取响应报文里的签名
     * @param respMsg
     * @return
     */
    private static String getPrivateCiphertext(String respMsg) {
        int beginIndex = respMsg.indexOf(SECURITY_LENGTH);
        int endIndex = respMsg.indexOf("<?xml");
        if (beginIndex > 0 && 0 < endIndex) {
            return respMsg.substring(beginIndex + SECURITY_LENGTH.length(), endIndex);
        }
        return null;
    }

    /**
     * 获取响应报文里的xml文档
     * @param respMsg
     * @return
     */
    public static String getXmlDocument(String respMsg) {
        int beginIndex = respMsg.indexOf("<?xml");
        if (beginIndex > 0) {
            return respMsg.substring(beginIndex);
        }
        return null;
    }

    /**
     * 获取响应报文
     * @param respMsg
     * @return
     */
    public static <T> T getXMLDocument(Class<T> clazz, String respMsg) {
        String xmlDoc = getXmlDocument(respMsg);
        return XMLHelper.toBean(xmlDoc, clazz);
    }

    /**
     * 将xml的业务数据封装成相应的bean
     * @param enXmlParam
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getXmlParam(String enXmlParam, Class<T> clazz) throws Exception {
        String XmlParam = "<xml>" + DES3EncryptAndDecrypt.decrypt(enXmlParam) + "</xml>";
        return XMLHelper.toBean(XmlParam, clazz);
    }

    /**
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getMsgFromRequest(HttpServletRequest request) throws IOException {
        StringBuffer result = new StringBuffer();
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String str = null;
            while((str=buffer.readLine())!=null){
                result.append(str);
            }
        }
        return result.toString();
    }

    /**
     * 从结果获取业务参数
     * @param result
     * @return
     * @throws Exception
     */
    public static RespBusinessParams getRespBusiParams(String result) throws Exception {
        ResponseDocument respdecument = MessageUtil.getXMLDocument(ResponseDocument.class, result);
        String enXmlParam = respdecument.getBody().getXMLPARA();
        return MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
    }

    public static String getHtml(String formAction, String tradeCode, String message) {
        String formId = UUID.randomUUID().toString().replaceAll("-", "");
        StringBuffer html = new StringBuffer("<html lang='zh-cn'><head><title></title></head><body><form id='");
        html.append(formId).append("' name='").append(formId).append("' action='").append(formAction)
                .append("' accept-charset='utf-8' method='post'><input type=\"hidden\" name=\"RequestData\" value='")
                .append(message).append("'/><input type=\"hidden\" name=\"transCode\" value='")
                .append(tradeCode).append("'/></form><script type='text/javascript'>document.getElementById('")
                .append(formId).append("').submit();</script></body></html>");
        return html.toString();
    }
}
