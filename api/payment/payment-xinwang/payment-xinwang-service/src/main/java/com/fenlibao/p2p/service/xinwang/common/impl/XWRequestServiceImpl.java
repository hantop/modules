package com.fenlibao.p2p.service.xinwang.common.impl;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.service.xinwang.common.XWRequestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/5/12.
 */
@Service
public class XWRequestServiceImpl implements XWRequestService{
    @Resource
    XWRequestDao requestDao;

    @Override
    public void saveRequestMessage(XWResponseMessage message) throws Exception {
        requestDao.saveRequestMessage(message);
    }

    @Override
    public void saveResponseMessage(XWResponseMessage responseMessage) throws Exception {
        requestDao.saveResponseMessage(responseMessage);
    }

    @Override
    public void updateRequest(XWRequest request) throws Exception {
        requestDao.updateRequest(request);
    }

    @Override
    public void submit(String requestNo) {
        XWRequest requestParams = new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.DQR);
        requestDao.updateRequest(requestParams);
    }

    @Override
    public void success(String requestNo) {
        XWRequest requestParams = new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);
    }

    @Override
    public void fail(String requestNo) {
        XWRequest requestParams = new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.SB);
        requestDao.updateRequest(requestParams);
    }
}
