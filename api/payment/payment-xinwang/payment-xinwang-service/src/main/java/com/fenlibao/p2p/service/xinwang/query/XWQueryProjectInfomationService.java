package com.fenlibao.p2p.service.xinwang.query;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface XWQueryProjectInfomationService {
    /**
     * 查询不保存信息
     * @param projectNo
     * @return
     * @throws Exception
     */
    Map<String, Object> queryProjectInformation(Integer projectNo) throws Exception;
}
