package com.fenlibao.p2p.model.entity.pms.user;

import java.io.Serializable;
import java.util.Date;

/**
 * PMS用户
 *
 * Created by chenzhixuan on 2017/4/12.
 */
public class PmsUser implements Serializable {

    private int id;

    private String username;

    private String password;

    private String salt;

    private String realname;

    private String phone;

    private String email;

    private Integer status;

    private Date createTime;

    private Boolean lock;

    private String department;//部门

    private boolean dimission; //是否离职

    private int errorNumber;//密码错误次数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isDimission() {
        return dimission;
    }

    public void setDimission(boolean dimission) {
        this.dimission = dimission;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }
}
