package com.fenlibao.p2p.service.xinwang.entrust;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface XWEntrustImportUserService {
    void entrustImportUser(Integer userId, String realName, String idCardType, String idCardNo, String mobile, String bankcardNo, String userRole) throws Exception;
}
