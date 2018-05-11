package com.fenlibao.model.pms.idmt.user.form;

import com.fenlibao.model.pms.idmt.user.PmsUser;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Bogle on 2016/1/26.
 */
public class UserForm extends PmsUser {

    @Length(min = 6, max = 12, message = "渠道名称字符长度在 {min} 到 {max} 之间")
    private String repassword;

    private String organizationId;

    private String organizationName;

    public String getGroupId() {
        return organizationId;
    }

    public void setGroupId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
