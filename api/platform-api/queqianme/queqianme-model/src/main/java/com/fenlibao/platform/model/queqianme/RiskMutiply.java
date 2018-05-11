package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.common.json.Jackson;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/26.
 */
public class RiskMutiply {
    private int id;
    private int score;
    private int auditStatus;
    public RiskMutiply() {

    }

    public RiskMutiply(String riskMutiply) {
        Map<String,Object> riskMutiplyMap  = Jackson.getMapFormString(riskMutiply);
        if(riskMutiplyMap!=null){
            this.score = (Integer) riskMutiplyMap.get("creditScore");
        }
         this.auditStatus = 0;
        if(this.score>=600){
            this.auditStatus = 1;
        }else {
            this.auditStatus = 2;
        }

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }
}
