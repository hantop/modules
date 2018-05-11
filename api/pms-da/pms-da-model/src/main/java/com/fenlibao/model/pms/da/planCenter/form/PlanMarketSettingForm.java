package com.fenlibao.model.pms.da.planCenter.form;

/**
 * Created by Administrator on 2017/3/23.
 */
public class PlanMarketSettingForm {
    private int id;//计划id
    private String name;//计划名称
    private String timeStart;//处理时间
    private String timeEnd;
    private String timeStartShow;//处理时间
    private String timeEndShow;
    private int productType;//1月升计划,2省心计划
    private int status;//状态
    private int isCG;//系统类型:1,普通; 2,存管

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

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeStartShow() {
        return timeStartShow;
    }

    public void setTimeStartShow(String timeStartShow) {
        this.timeStartShow = timeStartShow;
    }

    public String getTimeEndShow() {
        return timeEndShow;
    }

    public void setTimeEndShow(String timeEndShow) {
        this.timeEndShow = timeEndShow;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsCG() {
        return isCG;
    }

    public void setIsCG(int isCG) {
        this.isCG = isCG;
    }
}
