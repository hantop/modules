package com.fenlibao.p2p.model.entity.district;

/**
 * 行政区划
 * Created by chenzhixuan on 2015/9/9.
 */
public class District {
    private int id;
    // 省级ID
    private int provinceId;
    // 市级ID
    private int cityId;
    // 县级ID
    private int countyId;
    // 名称
    private String name;
    // 省级名称
    private String provinceName;
    // 市级名称
    private String cityName;
    // 县级名称
    private String countyName;
    // 级别,SHENG:省级;SHI:市级;XIAN:县级
    private String level;
    // 简拼
    private String spell;
    // 连连支付code
    private int llCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public int getLLCode() {
        return llCode;
    }

    public void setLLCode(int llCode) {
        this.llCode = llCode;
    }
}
