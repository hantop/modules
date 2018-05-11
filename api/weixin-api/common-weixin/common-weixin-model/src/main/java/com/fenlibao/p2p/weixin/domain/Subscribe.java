package com.fenlibao.p2p.weixin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name = "weixin_subscribe")
public class Subscribe implements Serializable {

    private static final long serialVersionUID = 5154416609542056971L;

    @Id
    @Column(name = "opend_id", unique = true)
    private String opendId;

    @Column(name = "event_key")
    private String eventKey;

    @Column(name = "subscribe_time")
    private Timestamp subscribeTime;

    public Subscribe() {
    }

    public Subscribe(String opendId, String eventKey) {
        this.opendId = opendId;
        this.eventKey = eventKey;
    }

    public String getOpendId() {
        return opendId;
    }

    public void setOpendId(String opendId) {
        this.opendId = opendId == null ? null : opendId.trim();
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey == null ? null : eventKey.trim();
    }

    public Timestamp getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Timestamp subscribeTime) {
        this.subscribeTime = subscribeTime;
    }
}