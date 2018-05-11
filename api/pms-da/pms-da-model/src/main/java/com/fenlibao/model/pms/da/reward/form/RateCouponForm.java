package com.fenlibao.model.pms.da.reward.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class RateCouponForm {
    private int id;
    private Date timeStart;
    private Date timeEnd;
    @NotEmpty(message = "加息券代码不能为空")
    @NotNull(message = "加息券代码不能为空")
    @Length(min = 0, max = 20, message = "加息券代码字符长度在 {min} 到 {max} 之间")
    private String couponCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}