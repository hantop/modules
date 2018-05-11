package com.fenlibao.ud.pojo;

import java.io.Serializable;

/**
 * 风控调用请求POJO.
 *
 * @author hush@bsfit.com.cn
 * @version 1.0
 * @date 10/29/15
 */
public class AntifraudRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String partnerCode;
    private String strategyCode;
    private String scenarioCode;
    private String packageStr;
    private String nonceStr;
    private String signature;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }


    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStrategyCode() {
        return strategyCode;
    }

    public void setStrategyCode(String strategyCode) {
        this.strategyCode = strategyCode;
    }

    public String getScenarioCode() {
        return scenarioCode;
    }

    public void setScenarioCode(String scenarioCode) {
        this.scenarioCode = scenarioCode;
    }

    public AntifraudRequest() {}


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"partnerCode\":\"").append(partnerCode).append('\"');
        sb.append(", \"strategyCode\":\"").append(strategyCode).append('\"');
        sb.append(", \"scenarioCode\":\"").append(scenarioCode).append('\"');
        sb.append(", \"packageStr\":\"").append(packageStr).append('\"');
        sb.append(", \"nonceStr\":\"").append(nonceStr).append('\"');
        sb.append(", \"signature\":\"").append(signature).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
