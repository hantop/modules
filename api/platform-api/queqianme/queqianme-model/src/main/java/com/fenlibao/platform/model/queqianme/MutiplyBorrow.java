package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.common.json.Jackson;

import java.util.Map;

/**
 * 风控模型（多头借贷基本信息）
 * Created by Administrator on 2017/12/25.
 */
public class MutiplyBorrow {

    private int id;
    private int mutiIdOneDay;//1天内设备使用过多的身份证或手机号进行申请
    private int mutiIdSevenDay;//7天内设备使用过多的身份证或手机号进行申请
    private int mutiDeviceOneDay;//1天内身份证使用过多设备进行申请
    private int mutiDeviceSevenDay;//7天内身份证使用过多设备进行申请
    private int mutiDeviceOneMonth;//1个月内身份证使用过多设备进行申请
    private int mutiPlatformSevenDay;//7天内申请人在多个平台 申请借款
    private int mutiPlatformOneMonth;//1个月内申请人在多个平台 申请借款
    private int mutiPlatformTreeMonth;//3个月内申请人在多个平台 申请借款
    private int mutiPlatformSixMonth;//6个月内申请人在多个平台 申请借款
    private int mutiPlatformTwelveMonth;//12个月内申请人在多个平台 申请借款
    private int mutiPlatformEightteenMonth;//18个月内申请人在多个平台 申请借款
    private int mutiPlatformTwentyFourMonth;//24个月内申请人在多个平台 申请借款
    private int mutiPlatformSixtyMonth;//60个月内申请人在多个平台 申请借款
    private int excludeThreeMonth;//3个月内申请人在多个平台被放款_ 不包含本合作方
    private int includeThreeMonth;//3个月内申请人被本合作方放款
    private int loanPlatformSixMonth;//6个月内申请人在多个平台被放款
    private int loanPlatformTwelveMonth;//12个月内申请人在多个平台被放款
    private int loanPlatformEightteenMonth;//18个月内申请人在多个平台被放款
    private int loanPlatformTwentyFourMonth;//24个月内申请人在多个平台被放款
    private int loanPlatformSixtyMonth;//60个月内申请人在多个平台被放款

    public MutiplyBorrow() {
    }

    public MutiplyBorrow(String mutiplyBorrow) {
        Map<String,Object> mutiplyBorrowMap  = Jackson.getMapFormString(mutiplyBorrow);
        if(mutiplyBorrowMap!=null){
            this.mutiIdOneDay = (Integer) mutiplyBorrowMap.get("mutiIdOneDay");
            this.mutiIdSevenDay = (Integer) mutiplyBorrowMap.get("mutiIdSevenDay");
            this.mutiDeviceOneDay = (Integer) mutiplyBorrowMap.get("mutiDeviceOneDay");
            this.mutiDeviceSevenDay = (Integer) mutiplyBorrowMap.get("mutiDeviceSevenDay");
            this.mutiDeviceOneMonth = (Integer) mutiplyBorrowMap.get("mutiDeviceOneMonth");
            this.mutiPlatformSevenDay = (Integer) mutiplyBorrowMap.get("mutiPlatformSevenDay");
            this.mutiPlatformOneMonth = (Integer) mutiplyBorrowMap.get("mutiPlatformOneMonth");
            this.mutiPlatformTreeMonth = (Integer) mutiplyBorrowMap.get("mutiPlatformTreeMonth");
            this.mutiPlatformSixMonth = (Integer) mutiplyBorrowMap.get("mutiPlatformSixMonth");
            this.mutiPlatformTwelveMonth = (Integer) mutiplyBorrowMap.get("mutiPlatformTwelveMonth");
            this.mutiPlatformEightteenMonth = (Integer) mutiplyBorrowMap.get("mutiPlatformEightteenMonth");
            this.mutiPlatformTwentyFourMonth = (Integer) mutiplyBorrowMap.get("mutiPlatformTwentyFourMonth");
            this.mutiPlatformSixtyMonth = (Integer) mutiplyBorrowMap.get("mutiPlatformSixtyMonth");
            this.excludeThreeMonth = (Integer) mutiplyBorrowMap.get("excludeThreeMonth");
            this.includeThreeMonth = (Integer) mutiplyBorrowMap.get("includeThreeMonth");
            this.loanPlatformSixMonth = (Integer) mutiplyBorrowMap.get("loanPlatformSixMonth");
            this.loanPlatformTwelveMonth = (Integer) mutiplyBorrowMap.get("loanPlatformTwelveMonth");
            this.loanPlatformEightteenMonth = (Integer) mutiplyBorrowMap.get("loanPlatformEightteenMonth");
            this.loanPlatformTwentyFourMonth = (Integer) mutiplyBorrowMap.get("loanPlatformTwentyFourMonth");
            this.loanPlatformSixtyMonth = (Integer) mutiplyBorrowMap.get("loanPlatformSixtyMonth");
        }
    }

   /* public Response verifyMutiplyBorrow() throws Exception {
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
    }*/

    public int getExcludeThreeMonth() {
        return excludeThreeMonth;
    }

    public void setExcludeThreeMonth(int excludeThreeMonth) {
        this.excludeThreeMonth = excludeThreeMonth;
    }

    public int getIncludeThreeMonth() {
        return includeThreeMonth;
    }

    public void setIncludeThreeMonth(int includeThreeMonth) {
        this.includeThreeMonth = includeThreeMonth;
    }

    public int getLoanPlatformEightteenMonth() {
        return loanPlatformEightteenMonth;
    }

    public void setLoanPlatformEightteenMonth(int loanPlatformEightteenMonth) {
        this.loanPlatformEightteenMonth = loanPlatformEightteenMonth;
    }

    public int getLoanPlatformSixMonth() {
        return loanPlatformSixMonth;
    }

    public void setLoanPlatformSixMonth(int loanPlatformSixMonth) {
        this.loanPlatformSixMonth = loanPlatformSixMonth;
    }

    public int getLoanPlatformSixtyMonth() {
        return loanPlatformSixtyMonth;
    }

    public void setLoanPlatformSixtyMonth(int loanPlatformSixtyMonth) {
        this.loanPlatformSixtyMonth = loanPlatformSixtyMonth;
    }

    public int getLoanPlatformTwelveMonth() {
        return loanPlatformTwelveMonth;
    }

    public void setLoanPlatformTwelveMonth(int loanPlatformTwelveMonth) {
        this.loanPlatformTwelveMonth = loanPlatformTwelveMonth;
    }

    public int getLoanPlatformTwentyFourMonth() {
        return loanPlatformTwentyFourMonth;
    }

    public void setLoanPlatformTwentyFourMonth(int loanPlatformTwentyFourMonth) {
        this.loanPlatformTwentyFourMonth = loanPlatformTwentyFourMonth;
    }

    public int getMutiDeviceOneDay() {
        return mutiDeviceOneDay;
    }

    public void setMutiDeviceOneDay(int mutiDeviceOneDay) {
        this.mutiDeviceOneDay = mutiDeviceOneDay;
    }

    public int getMutiDeviceOneMonth() {
        return mutiDeviceOneMonth;
    }

    public void setMutiDeviceOneMonth(int mutiDeviceOneMonth) {
        this.mutiDeviceOneMonth = mutiDeviceOneMonth;
    }

    public int getMutiDeviceSevenDay() {
        return mutiDeviceSevenDay;
    }

    public void setMutiDeviceSevenDay(int mutiDeviceSevenDay) {
        this.mutiDeviceSevenDay = mutiDeviceSevenDay;
    }

    public int getMutiIdOneDay() {
        return mutiIdOneDay;
    }

    public void setMutiIdOneDay(int mutiIdOneDay) {
        this.mutiIdOneDay = mutiIdOneDay;
    }

    public int getMutiIdSevenDay() {
        return mutiIdSevenDay;
    }

    public void setMutiIdSevenDay(int mutiIdSevenDay) {
        this.mutiIdSevenDay = mutiIdSevenDay;
    }

    public int getMutiPlatformEightteenMonth() {
        return mutiPlatformEightteenMonth;
    }

    public void setMutiPlatformEightteenMonth(int mutiPlatformEightteenMonth) {
        this.mutiPlatformEightteenMonth = mutiPlatformEightteenMonth;
    }

    public int getMutiPlatformOneMonth() {
        return mutiPlatformOneMonth;
    }

    public void setMutiPlatformOneMonth(int mutiPlatformOneMonth) {
        this.mutiPlatformOneMonth = mutiPlatformOneMonth;
    }

    public int getMutiPlatformSevenDay() {
        return mutiPlatformSevenDay;
    }

    public void setMutiPlatformSevenDay(int mutiPlatformSevenDay) {
        this.mutiPlatformSevenDay = mutiPlatformSevenDay;
    }

    public int getMutiPlatformSixMonth() {
        return mutiPlatformSixMonth;
    }

    public void setMutiPlatformSixMonth(int mutiPlatformSixMonth) {
        this.mutiPlatformSixMonth = mutiPlatformSixMonth;
    }

    public int getMutiPlatformSixtyMonth() {
        return mutiPlatformSixtyMonth;
    }

    public void setMutiPlatformSixtyMonth(int mutiPlatformSixtyMonth) {
        this.mutiPlatformSixtyMonth = mutiPlatformSixtyMonth;
    }

    public int getMutiPlatformTreeMonth() {
        return mutiPlatformTreeMonth;
    }

    public void setMutiPlatformTreeMonth(int mutiPlatformTreeMonth) {
        this.mutiPlatformTreeMonth = mutiPlatformTreeMonth;
    }

    public int getMutiPlatformTwelveMonth() {
        return mutiPlatformTwelveMonth;
    }

    public void setMutiPlatformTwelveMonth(int mutiPlatformTwelveMonth) {
        this.mutiPlatformTwelveMonth = mutiPlatformTwelveMonth;
    }

    public int getMutiPlatformTwentyFourMonth() {
        return mutiPlatformTwentyFourMonth;
    }

    public void setMutiPlatformTwentyFourMonth(int mutiPlatformTwentyFourMonth) {
        this.mutiPlatformTwentyFourMonth = mutiPlatformTwentyFourMonth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
