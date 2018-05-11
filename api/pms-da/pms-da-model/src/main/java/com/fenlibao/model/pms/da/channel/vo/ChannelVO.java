package com.fenlibao.model.pms.da.channel.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 渠道VO
 * Created by chenzhixuan on 2015/9/24.
 */
public class ChannelVO {
    private int id;

    /**
     * 自身关联对象
     */
    private ChannelVO parent;

    @Min(value = 1, message = "请选择渠道分类")
    private int parentId;// 父渠道ID

    @Length(min = 2, max = 10, message = "渠道编码字符长度在 {min} 到 {max} 之间")
    @Pattern(regexp = "[0-9a-zA-Z]{2,10}", message = "编码只能是字母或数字的组合")
    private String code;// 渠道编码

    @Length(min = 2, max = 30, message = "渠道名称字符长度在 {min} 到 {max} 之间")
    private String name;// 渠道名称

    @Length(min = 0, max = 200, message = "备注字符长度在 {min} 到 {max} 之间")
    private String description;// 备注
    private int registerCount;// 注册总计

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

    public ChannelVO getParent() {
        return parent;
    }

    public void setParent(ChannelVO parent) {
        this.parent = parent;
    }
}
