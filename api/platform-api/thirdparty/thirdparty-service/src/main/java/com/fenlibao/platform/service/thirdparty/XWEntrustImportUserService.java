package com.fenlibao.platform.service.thirdparty;

/**
 * Created by Administrator on 2017/8/16.
 */
public interface XWEntrustImportUserService {
    void entrustImportUser(Integer userId, String realName, String idCardType, String idCardNo, String mobile, String bankcardNo, String userRole) throws Exception;
}
