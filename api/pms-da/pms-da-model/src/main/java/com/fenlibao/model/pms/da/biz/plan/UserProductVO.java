package com.fenlibao.model.pms.da.biz.plan;

/**
 * @author Mingway.Xu
 * @date 2017/3/24 10:10
 */
public class UserProductVO {
    private Integer productType;

    private Integer productId;

    private Integer tenderId;

    private double amount;

    private double returnAmount;

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTenderId() {
        return tenderId;
    }

    public void setTenderId(int tenderId) {
        this.tenderId = tenderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }
}
