package com.fenlibao.p2p.service.xinwang.project.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.project.XWEstablishProjectTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/6.
 */
@Service
public class XWEstablishProjectTransactionServiceImpl implements XWEstablishProjectTransactionService{

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    PTCommonDao ptCommonDao;

    @Override
    @Transactional
    public void ptEstablishProject(String requestNo,Integer loanId,XWProjectInfo projectInfo) throws Exception{
        //平台发布标
        Date now=ptCommonDao.getCurrentDate();
        Calendar raiseDeadline = Calendar.getInstance();
        raiseDeadline.setTime(now);
        raiseDeadline.add(Calendar.DAY_OF_YEAR, projectInfo.getFundRaisingDays());
        raiseDeadline.set(Calendar.HOUR_OF_DAY, 24);
        raiseDeadline.set(Calendar.MINUTE, 0);
        raiseDeadline.set(Calendar.SECOND, 0);

        Map<String,Object> updateProjectParams=new HashMap<>();
        updateProjectParams.put("state", PTProjectState.TBZ.name());
        if(projectInfo.getEstablishTime()==null){
            updateProjectParams.put("establishTime", now);
        }
        updateProjectParams.put("fundRaisingDeadline", raiseDeadline.getTime());
        updateProjectParams.put("projectNo",loanId);
        projectDao.establishProject(updateProjectParams);
        //存管发标后添加利息管理费字段
        BigDecimal percent = projectDao.getInterestPercent(1);
        projectDao.updateInterestPercent(loanId, percent);

        //如果标设置了要加入计划债权库，就把标插入债权库
        XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(loanId);
        if(projectExtraInfo.getJoinPlan()){
            projectDao.projectJoinPlan(loanId);
        }
        //结束订单
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.CG);
        requestDao.updateRequest(param);
    }
}
