package com.fenlibao.p2p.weixin.message.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fenlibao.p2p.weixin.component.JSONUtil;
import com.fenlibao.p2p.weixin.message.WxMsg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板消息
 * Created by Administrator on 2015/7/9.
 */
public class TemplateMsg extends WxMsg implements Serializable {

    private static final long serialVersionUID = -5437748572811636020L;

    /**
     * 接收消息的用户openid
     */
    @JsonProperty(value = "touser")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String touser;

    /**
     * 模板ID
     */
    @JsonProperty(value = "template_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String templateId;

    /**
     * 点击模板消息的链接
     */
    @JsonProperty(value = "url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;

    @JsonProperty(value = "topcolor")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String topcolor;
    
    /**
     * 消息id
     */
    @JsonProperty(value = "msgid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msgid;

    /**
     * 消息内容map
     */
    @JsonProperty(value = "data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, TemplateMsgData> data;

    public TemplateMsg() {
    }

    public TemplateMsg(String touser, String templateId, String url, String topcolor) {
        this.touser = touser;
        this.templateId = templateId;
        this.url = url;
        this.topcolor = topcolor;
    }

    public TemplateMsg(String templateId, String url, String topcolor) {
        this.templateId = templateId;
        this.url = url;
        this.topcolor = topcolor;
    }

    public static void main(String[] args) throws JsonProcessingException {
        TemplateMsg templateMsg = new TemplateMsg("OPENID", "ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY", "http://weixin.qq.com/download", "#FF0000");
        Map<String, TemplateMsgData> data = new HashMap<>();
        data.put("first", new TemplateMsgData("恭喜你购买成功！", "#173177"));
        data.put("keynote1", new TemplateMsgData("巧克力", "#173177"));
        data.put("keynote2", new TemplateMsgData("39.8元", "#173177"));
        data.put("keynote3", new TemplateMsgData("2014年9月16日", "#173177"));
        data.put("remark", new TemplateMsgData("欢迎再次购买！", "#173177"));
        templateMsg.setData(data);
        String json = JSONUtil.writeValueAsString(templateMsg);
        System.out.println(json);
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public Map<String, TemplateMsgData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateMsgData> data) {
        this.data = data;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}
