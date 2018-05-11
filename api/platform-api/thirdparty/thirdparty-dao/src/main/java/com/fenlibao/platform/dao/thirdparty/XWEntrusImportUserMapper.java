package com.fenlibao.platform.dao.thirdparty;

import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 */
public interface XWEntrusImportUserMapper {
    void createRequest(XWRequest request);
    void saveRequestMessage(XWResponseMessage message);
    void saveResponseMessage(XWResponseMessage responseMessage);
    void updateRequest(XWRequest request);
    void createXWAccount(XinwangAccount account) ;
    void createPTAssetAccount(Map<String,Object> params) ;
    Boolean getIdentityAuthState(Integer userId) ;
    void updatePTAccountIdentityState(Integer userId) ;
    void updatePTAccountInfo(Map<String,Object> params) ;

}
