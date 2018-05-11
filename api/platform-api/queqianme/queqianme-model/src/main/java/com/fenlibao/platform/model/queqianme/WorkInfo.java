package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.common.json.Jackson;
import com.fenlibao.platform.model.Response;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 风控模型（工作情况）
 * Created by xiao on 2016/10/23.
 */
public class WorkInfo {

    private int id;

    private String companyName;//单位名称

    private String companyPhone;//单位电话

    private String companyAddress;//单位地址

    private String position;//职员

    private String monthlyIncome;//月收入


    public WorkInfo() {

    }

    public WorkInfo(String workInfo) {
        Map<String,Object> workMap  = Jackson.getMapFormString(workInfo);
        if(workMap!=null){
            this.companyAddress = (String) workMap.get("companyAddress");
            this.companyName = (String)workMap.get("companyName");
            this.companyPhone = (String)workMap.get("companyPhone");
            this.position = (String)workMap.get("position");
            this.monthlyIncome = (String)workMap.get("monthlyIncome");
        }
    }




    public Response verifyWorkInfo(String key) throws Exception {
        if (StringUtils.isBlank(companyName)
                || StringUtils.isBlank(companyPhone)
                || StringUtils.isBlank(companyAddress)
                || StringUtils.isBlank(position)
                ||StringUtils.isBlank(monthlyIncome)) {
            return Response.SYSTEM_EMPTY_PARAMETERS;
        }

        try{
            new BigDecimal(monthlyIncome);
        }catch (Exception e){
            return Response.SYSTEM_ERROR_PARAMETERS;
        }
        if( new BigDecimal(monthlyIncome).compareTo(BigDecimal.ZERO)<0){
            return Response.SYSTEM_ERROR_PARAMETERS;
        }
        return Response.RESPONSE_SUCCESS;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
