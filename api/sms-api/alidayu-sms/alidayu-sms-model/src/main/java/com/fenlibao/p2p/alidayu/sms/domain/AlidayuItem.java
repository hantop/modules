package com.fenlibao.p2p.alidayu.sms.domain;

import java.io.Serializable;

/**
 * Created by Bogle on 2016/2/25.
 */
public class AlidayuItem implements Serializable{

    private static final long serialVersionUID = 3453556157968859928L;

    private Integer id;

    private int startIndex;
    private int endIndex;
    private String pattern;// 正则表达式获取验证码
    private String keyName;// 模板中的对应key

    public AlidayuItem() {
    }

    public AlidayuItem(int startIndex, int endIndex, String pattern, String keyName) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.pattern = pattern;
        this.keyName = keyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
