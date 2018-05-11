package com.fenlibao.platform.service;

import com.fenlibao.platform.model.BusinessAgreement;
import com.fenlibao.platform.model.BusinessInfo;

import java.util.Map;

/**
 * Created by Lullaby on 2016/2/25.
 */
public interface CommonService {

    boolean isIPAuthorized(String ip, String appid);

    boolean verifySign(Map<String, String> params);

    /**
     * 更新秘钥
     * @param appid
     * @param secret
     * @param status 使用状态（1、启用；0、停用）
     */
    void updateSecret(String appid, String secret, Integer status)  throws Exception;

    /**
     * 更新ip
     * <p>exist ? delete : add
     * @param appid
     * @param ip
     * @throws Exception
     */
    boolean updateIp(String appid, String ip) throws Exception;

    public boolean existsKey(String key);

    public void removeKey(String key);

    /**
     * 获取商户ID
     * @param appid
     * @return
     */
    String getMerchantId(String appid) throws Exception;

    /**
     * 获取商户的AES密钥
     * @param appid
     * @return
     */
    String getAesSecret(String appid) throws Exception;

    /**
     * 判断商户是否委托开户
     * @param map
     * @return
     */
    int getBusinessUser(Map<String,Object> map);

    /**
     * 插入开户请求记录数据
     *
     * @param map
     */
    int addBusinessRequest(Map<String,Object> map);

    /**
     * 更新开户请求记录数据状态
     * @param map
     * @throws Exception
     */
    int updateBusinessRequest(Map<String,Object> map)  throws Exception;

    /**
     * 插入开户信息数据
     * @param businessInfo
     */
    int addBusinessUserInfo(BusinessInfo businessInfo);

    /**
     * 判断商户是否有请求记录
     * @param map
     * @return
     */
    String getBusinessRequest(Map<String,Object> map);

    /**
     * 插入协议签订数据
     * @param businessAgreement
     */
    void addBusinessAgreement(BusinessAgreement businessAgreement);


    void registerAndAgreement(BusinessInfo businessInfo, BusinessAgreement businessAgreement);

    void updateAgreement(Integer userId,Integer businessId);

    int getAgreement(Integer userId);

    /**
     * 插入委托开户异常响应信息表
     *
     */
    void addUserExceprionResopnse(Map<String,Object> map);

}
