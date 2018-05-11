package com.fenlibao.p2p.weixin.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Bogle on 2015/12/11.
 */
@Entity(name = "wexin_template_msg")
public class TemplateMsg implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "touser")
    private String touser;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "url")
    private String url;

    @Column(name = "topcolor")
    private String topcolor;

    //错误编码
    @Column(name = "errcode")
    private int errcode;

    //错误消息
    @Column(name = "errmsg")
    private String errmsg;


    @Column(name = "msgid")
    private String msgid;

    //错误消息
    @Column(name = "status")
    private String status;

    @JoinColumn(name = "template_msg_id")
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<TemplateMsgData> templateMsgDatas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TemplateMsgData> getTemplateMsgDatas() {
        return templateMsgDatas;
    }

    public void setTemplateMsgDatas(List<TemplateMsgData> templateMsgDatas) {
        this.templateMsgDatas = templateMsgDatas;
    }
}
