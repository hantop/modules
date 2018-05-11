package com.fenlibao.platform.model.thirdparty.vo.bid;

import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/14.
 */
public class BidInfoPreVoWDZJ implements Serializable{


    private int projectId;//项目主键,t6230.F01
    private String deadline;//实际借款期限
    private String deadlineUnit;//期限单位* 仅限  ‘月’ 或 ‘天’

    public BidInfoPreVoWDZJ(BidInfoEntity bid){
        projectId = bid.getProjectId();
        if(bid.getDeadline_m()==0) {
            deadline = String.valueOf(DateUtil.getDayBetweenDates(bid.getSuccessTime(), new Date()) -1);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            deadline =  String.valueOf(DateUtil.getMonthSpace(sdf.format(bid.getSuccessTime()),sdf.format(new Date())));
        }

        this.deadlineUnit=bid.getDeadline_d()==0?"月":"天";
    }
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadlineUnit() {
        return deadlineUnit;
    }

    public void setDeadlineUnit(String deadlineUnit) {
        this.deadlineUnit = deadlineUnit;
    }
}
