package com.fenlibao.p2p.weixin.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Bogle on 2015/12/11.
 */
@Entity(name = "wexin_template_msg_data")
public class TemplateMsgData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_msg_id")
    private Long templateMsg;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "color")
    private String color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateMsg() {
        return templateMsg;
    }

    public void setTemplateMsg(Long templateMsg) {
        this.templateMsg = templateMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
