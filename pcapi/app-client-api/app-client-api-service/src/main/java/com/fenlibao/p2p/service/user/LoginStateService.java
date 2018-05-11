package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.vo.UserAccountInfoVO;

/**
 * 登录态相关服务类
 * Created by Lullaby on 2015-10-22 17:02
 */
public interface LoginStateService {

    String saveLoginState(String clientType, UserAccountInfoVO userVO);

    String saveLoginToken(String clientType, String userId) throws Exception;
}
