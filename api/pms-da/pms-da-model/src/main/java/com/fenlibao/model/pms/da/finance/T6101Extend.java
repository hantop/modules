package com.fenlibao.model.pms.da.finance;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/7/5.
 */
public class T6101Extend {
    /**
     * 账户ID,自增
     */
    private Integer F01;

    /**
     * 用户ID,参考T6110.F01
     */
    private Integer F02;

    /**
     * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;SDZH:锁定账户;
     */
    private String F03;

    /**
     * 资金账号
     */
    private String F04;

    /**
     * 账户名称
     */
    private String F05;

    /**
     * 余额
     */
    private BigDecimal F06 = BigDecimal.ZERO;

    /**
     * 最后更新时间
     */
    private Timestamp F07;

    /**
     * 是否已绑定银行卡： 1:是、0:否
     */
    private Integer isBindBank;

    /**
     * 是否允许解绑银行卡 0：否、1：是
     */
    private Integer allowUnbind;

    /**
     * 用户角色
     */

    private String userRole;

    public Integer getF01() {
        return F01;
    }

    public void setF01(Integer f01) {
        F01 = f01;
    }

    public Integer getF02() {
        return F02;
    }

    public void setF02(Integer f02) {
        F02 = f02;
    }

    public String getF03() {
        return F03;
    }

    public void setF03(String f03) {
        F03 = f03;
    }

    public String getF04() {
        return F04;
    }

    public void setF04(String f04) {
        F04 = f04;
    }

    public String getF05() {
        return F05;
    }

    public void setF05(String f05) {
        F05 = f05;
    }

    public BigDecimal getF06() {
        return F06;
    }

    public void setF06(BigDecimal f06) {
        F06 = f06;
    }

    public Timestamp getF07() {
        return F07;
    }

    public void setF07(Timestamp f07) {
        F07 = f07;
    }

    public Integer getIsBindBank() {
        return isBindBank;
    }

    public void setIsBindBank(Integer isBindBank) {
        this.isBindBank = isBindBank;
    }

    public Integer getAllowUnbind() {
        return allowUnbind;
    }

    public void setAllowUnbind(Integer allowUnbind) {
        this.allowUnbind = allowUnbind;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
