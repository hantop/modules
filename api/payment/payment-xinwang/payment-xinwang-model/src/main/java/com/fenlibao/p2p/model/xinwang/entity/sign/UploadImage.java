package com.fenlibao.p2p.model.xinwang.entity.sign;

/**
 * 上上签上传用户图片
 * Created by Administrator on 2017/12/13.
 */
public class UploadImage {
    private int id;
    private int userId;
    private String enterprisePhoneNum;
    private String enterpriseOfficialSealCode;
    private int enterpriseStatus;
    private String enterpriseUsername;
    private String enterpriseUserAccount;
    private String imageType;
    private String userType;
    private String liabilityOfficialSealCode;
    private String liabilityUserAccount;
    private int liabilityStatus;
    private String liabilityPhone;
    private String liabilityName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnterpriseOfficialSealCode() {
        return enterpriseOfficialSealCode;
    }

    public void setEnterpriseOfficialSealCode(String enterpriseOfficialSealCode) {
        this.enterpriseOfficialSealCode = enterpriseOfficialSealCode;
    }

    public int getEnterpriseStatus() {
        return enterpriseStatus;
    }

    public void setEnterpriseStatus(int enterpriseStatus) {
        this.enterpriseStatus = enterpriseStatus;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLiabilityOfficialSealCode() {
        return liabilityOfficialSealCode;
    }

    public void setLiabilityOfficialSealCode(String liabilityOfficialSealCode) {
        this.liabilityOfficialSealCode = liabilityOfficialSealCode;
    }

    public int getLiabilityStatus() {
        return liabilityStatus;
    }

    public void setLiabilityStatus(int liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }

    public String getEnterprisePhoneNum() {
        return enterprisePhoneNum;
    }

    public void setEnterprisePhoneNum(String enterprisePhoneNum) {
        this.enterprisePhoneNum = enterprisePhoneNum;
    }

    public String getLiabilityPhone() {
        return liabilityPhone;
    }

    public void setLiabilityPhone(String liabilityPhone) {
        this.liabilityPhone = liabilityPhone;
    }

    public String getEnterpriseUsername() {
        return enterpriseUsername;
    }

    public void setEnterpriseUsername(String enterpriseUsername) {
        this.enterpriseUsername = enterpriseUsername;
    }

    public String getLiabilityName() {
        return liabilityName;
    }

    public void setLiabilityName(String liabilityName) {
        this.liabilityName = liabilityName;
    }

    public String getEnterpriseUserAccount() {
        return enterpriseUserAccount;
    }

    public void setEnterpriseUserAccount(String enterpriseUserAccount) {
        this.enterpriseUserAccount = enterpriseUserAccount;
    }

    public String getLiabilityUserAccount() {
        return liabilityUserAccount;
    }

    public void setLiabilityUserAccount(String liabilityUserAccount) {
        this.liabilityUserAccount = liabilityUserAccount;
    }
}
