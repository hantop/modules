package com.fenlibao.p2p.model.entity.activity;

/**
 * Created by xiao on 2016/12/23.
 */
public class AnnualMettingParticipant {
    private int id;
    private String name; //用户名字
    private String phone;//电话

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
