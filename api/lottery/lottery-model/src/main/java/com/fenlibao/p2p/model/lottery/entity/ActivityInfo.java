package com.fenlibao.p2p.model.lottery.entity;

import java.util.Date;

/**
 * 对应 flb.t_activity 表
 * Created by xiao on 2017/1/13.
 */
public class ActivityInfo {
    private int id;
    private String name;//  活动名称
    private String code;//  活动编号
    private Date startDateTime;//  活动开始时间
    private Date endDateTime;//  活动结束时间
    private String remark;//   备注

    public ActivityInfo() {
    }

    public ActivityInfo(int id, String name, String code, Date startDateTime, Date endDateTime, String remark) {

        this.id = id;
        this.name = name;
        this.code = code;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
