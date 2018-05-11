package com.fenlibao.lianpay.share.config;

/**
 * @name   SignTyoe
 * @author Eric
 * @email  mataojs@lianlian.com
 * @date   2015年2月13日 下午2:27:07
 * @update #update time#
 * @modify #modify user#

 */
public enum SignType {
    MD5("MD5","MD5加签模式"),
    RSA("RSA","RSA加签模式"),
    DES("DES","DES加签模式");

    public String signTypeCode;
    public String signTypeName;

    public String getSignTypeCode(){
        return signTypeCode;
    }

    public String getSignTypeName(){
        return signTypeName;
    }


    SignType(String signTypeCode,String signTypeName){
        this.signTypeCode = signTypeCode;
        this.signTypeName = signTypeName;
    }
}
