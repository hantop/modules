package com.fenlibao.p2p.model.api.vo;

/**
 * pdf 文件偏移参数
 */
public class PdfPosition {
    Integer page;
    Float signX;
    Float signY;
    Integer starType;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Float getSignX() {
        return signX;
    }

    public void setSignX(Float signX) {
        this.signX = signX;
    }

    public Float getSignY() {
        return signY;
    }

    public void setSignY(Float signY) {
        this.signY = signY;
    }

    public Integer getStarType() {
        return starType;
    }

    public void setStarType(Integer starType) {
        this.starType = starType;
    }
}
