package com.fenlibao.p2p.service.xinwang;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */
public interface XWNotifyService {
    void handleNotify(Map<String, Object> respMap) throws Exception;
}
