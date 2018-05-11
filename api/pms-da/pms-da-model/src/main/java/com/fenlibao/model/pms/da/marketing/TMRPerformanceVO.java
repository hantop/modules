package com.fenlibao.model.pms.da.marketing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Louis Wang on 2016/3/9.
 */

public class TMRPerformanceVO implements Serializable{

    private Integer id;

    private String tmrNumber;

    private String tmrName;

    private String fileName;

    private Timestamp createtime;// 导入时间

    private Timestamp endTime;// 呼入结束时间

    private String numbers; //人数

    private boolean visible;    //是否可见

    private boolean dispose;    //是否处理

    private BigDecimal investTotal = new BigDecimal(0);     //投资的总金额

    private BigDecimal activateTotal = new BigDecimal(0);     //投资的总金额

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTmrNumber() {
        return tmrNumber;
    }

    public void setTmrNumber(String tmrNumber) {
        this.tmrNumber = tmrNumber;
    }

    public String getTmrName() {
        return tmrName;
    }

    public void setTmrName(String tmrName) {
        this.tmrName = tmrName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BigDecimal getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(BigDecimal investTotal) {
        this.investTotal = investTotal;
    }

    public BigDecimal getActivateTotal() {
        return activateTotal;
    }

    public void setActivateTotal(BigDecimal activateTotal) {
        this.activateTotal = activateTotal;
    }

    public boolean isDispose() {
        return dispose;
    }

    public void setDispose(boolean dispose) {
        this.dispose = dispose;
    }
}
