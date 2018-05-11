package com.fenlibao.p2p.service.xinwang.common;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;

/**
 * Created by Administrator on 2017/5/12.
 */
public interface XWRequestService {
    void saveRequestMessage(XWResponseMessage message) throws Exception;
    void saveResponseMessage(XWResponseMessage responseMessage) throws Exception;
    void updateRequest(XWRequest request) throws Exception;
    void submit(String requestNo);
    void success(String requestNo);
    void fail(String requestNo);
}
