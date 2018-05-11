package com.fenlibao.p2p.model.channel.vo;

/**
 * 渠道VO
 * Created by chenzhixuan on 2015/9/24.
 */
public class ChannelVO {
    private int id;
    private int parentId;// 父渠道ID
    private String code;// 渠道编码
    private String name;// 渠道名称
    private String description;// 备注
    // 注册总计
    private int registerCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }
}
