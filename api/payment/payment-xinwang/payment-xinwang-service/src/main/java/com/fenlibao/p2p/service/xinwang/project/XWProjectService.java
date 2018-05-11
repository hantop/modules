package com.fenlibao.p2p.service.xinwang.project;

import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.query.ProjectQueryInfo;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;

/**
 * Created by Administrator on 2017/6/8.
 */
public interface XWProjectService {
    void changeProjectStatus(XWProjectInfo projectInfo, PTProjectState status)throws Exception;

    ProjectQueryInfo queryProject(Integer bidId) throws Exception;
}
