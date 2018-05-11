package com.fenlibao.model.pms.idmt.user;

import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.role.Role;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PmsUser implements Serializable {

    private int id;

    @Length(min = 2, max = 20, message = "渠道名称字符长度在 {min} 到 {max} 之间")
    private String username;

    @Length(min = 6, max = 12, message = "渠道名称字符长度在 {min} 到 {max} 之间")
    private String password;

    private String salt;

    @Length(min = 2, max = 10, message = "真实姓名字符长度在 {min} 到 {max} 之间")
    private String realname;

    private String phone;

    private String email;

    private Integer status;

    private Date createTime;

    private Boolean lock;

    private String department;//部门

    private boolean dimission; //是否离职

    private int errorNumber;//密码错误次数
    List<Role> roles;

    List<PmsGroup> groups;

    public List<PmsGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<PmsGroup> groups) {
        this.groups = groups;
    }

    private Date lastChangepwdTime;

    public Date getLastChangepwdTime() {
		return lastChangepwdTime;
	}

	public void setLastChangepwdTime(Date lastChangepwdTime) {
		this.lastChangepwdTime = lastChangepwdTime;
	}

	public PmsUser() {
    }

    public PmsUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    public Boolean isLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getCredentialsSalt() {
        return username + salt;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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

    public int getErrorNumber() {return errorNumber;}

    public void setErrorNumber(int errorNumber) {this.errorNumber = errorNumber;}
}
