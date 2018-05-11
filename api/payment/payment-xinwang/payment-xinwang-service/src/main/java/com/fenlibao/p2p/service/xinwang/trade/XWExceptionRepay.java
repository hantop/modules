package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.service.xinwang.XWBatchNotifyService;

/**
 * Created by Administrator on 2017/9/18.
 * 资金异常还款（用户还平台）
 */
public interface XWExceptionRepay extends XWBatchNotifyService {
    public void exceptionRepay() throws Exception;
}
