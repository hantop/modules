package com.fenlibao.service.weixin.defines;

/**
 * Created by Bogle on 2016/3/4.
 */
public enum Env {

    prod("生产环节"), test("测试环境");
    private String evn;

    Env(String env) {
        this.evn = env;
    }

    public String getEvn() {
        return evn;
    }
}
