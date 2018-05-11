package com.fenlibao.p2p.sms.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/8/28.
 */
public class GsmsResponse implements Serializable {

    @JSONField(serialize=false)
    private Long id;
    private Long createTime;//创建时间

    @JSONField(serialize=false)
    private UUID uuid;
    private int result = -100;
    private String message;
    private String attributes;

    private String serverType;
    private List<GsmsResponse> errors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime == null ? System.currentTimeMillis() : createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public UUID getUuid() {
        return uuid == null ? UUID.randomUUID() : uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<GsmsResponse> getErrors() {
        return errors;
    }

    public void setErrors(List<GsmsResponse> errors) {
        if (this.errors != null) this.errors.addAll(errors);
        else
            this.errors = errors;
    }

    public void addErrors(GsmsResponse error) {
        if (this.errors == null)
            this.errors = new ArrayList<>();
        this.errors.add(error);
    }



    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }
}
