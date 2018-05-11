package com.fenlibao.p2p.model.vo.channel;

/**
 * 注册渠道VO
 * Created by zcai on 2016/6/20.
 */
public class RegisterChannelVO {

    private String code;
    private Integer parentId;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
