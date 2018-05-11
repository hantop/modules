package com.fenlibao.p2p.service.xinwang;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */
public interface XWBatchNotifyService {
    void handleNotify(List<Map<String, Object>> details) throws Exception;
}
