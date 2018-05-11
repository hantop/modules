package com.fenlibao.p2p.model.entity.bid;

/**
 * Created by Administrator on 2017/12/15.
 */
public class BidExtendRelatedGuaranteeInfo extends BidExtendInfo {
    Integer liabilityUserType; //连带担保用户类型: 1:个人; 0:企业
    String liabilityUserName; //连带担保用户名称
    String liabilityPhone;  //连带担保用户联系电话
    String liabilityIdCardNo;  //连带担保用户身份证,加密存储
    String liabilityUnifiedCode;  //连带担保注册号/统一信用代码

    public Integer getLiabilityUserType() {
        return liabilityUserType;
    }

    public void setLiabilityUserType(Integer liabilityUserType) {
        this.liabilityUserType = liabilityUserType;
    }

    public String getLiabilityUserName() {
        return liabilityUserName;
    }

    public void setLiabilityUserName(String liabilityUserName) {
        this.liabilityUserName = liabilityUserName;
    }

    public String getLiabilityPhone() {
        return liabilityPhone;
    }

    public void setLiabilityPhone(String liabilityPhone) {
        this.liabilityPhone = liabilityPhone;
    }

    public String getLiabilityIdCardNo() {
        return liabilityIdCardNo;
    }

    public void setLiabilityIdCardNo(String liabilityIdCardNo) {
        this.liabilityIdCardNo = liabilityIdCardNo;
    }

    public String getLiabilityUnifiedCode() {
        return liabilityUnifiedCode;
    }

    public void setLiabilityUnifiedCode(String liabilityUnifiedCode) {
        this.liabilityUnifiedCode = liabilityUnifiedCode;
    }

}
