package com.fenlibao.p2p.service.xinwang.account;

import com.fenlibao.p2p.model.xinwang.param.account.ResetPasswordRequestParams;
import com.fenlibao.p2p.service.xinwang.XWNotifyService;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
public interface XWResetPasswordService extends XWNotifyService {

    /**
     * 获取修改密码接收数据
     * @param params
     * @return
     */
    Map<String,Object> getResetPasswordRequestData(ResetPasswordRequestParams params) throws Exception;
}
