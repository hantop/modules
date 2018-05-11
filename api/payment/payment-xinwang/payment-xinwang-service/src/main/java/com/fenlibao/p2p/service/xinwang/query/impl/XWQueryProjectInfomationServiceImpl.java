package com.fenlibao.p2p.service.xinwang.query.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.query.XWQueryProjectInfomationService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/18.
 */
@Service
public class XWQueryProjectInfomationServiceImpl implements XWQueryProjectInfomationService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Override
    public Map<String, Object> queryProjectInformation(Integer projectNo) throws Exception {
        //组装请求
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("projectNo", projectNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));

        String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.QUERY_PROJECT_INFORMATION.getCode(),reqData);
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        return respMap;
    }

}
