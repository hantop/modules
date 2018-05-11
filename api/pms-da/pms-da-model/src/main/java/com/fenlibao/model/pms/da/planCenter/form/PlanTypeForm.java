package com.fenlibao.model.pms.da.planCenter.form;

/**
 * Created by Administrator on 2017/3/23.
 */
public class PlanTypeForm {
    private int id;//计划id
    private String title;//计划名称
    private int productType;//1月升计划,2省心计划

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }
}
