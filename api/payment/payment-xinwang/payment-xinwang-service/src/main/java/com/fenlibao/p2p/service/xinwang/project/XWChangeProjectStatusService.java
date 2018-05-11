package com.fenlibao.p2p.service.xinwang.project;

import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;

/**
 * Created by Administrator on 2017/6/8.
 */
public interface XWChangeProjectStatusService {
    void changeProjectStatus(Integer bidId, Integer orderId ,XWProjectStatus status)throws Exception;
}
