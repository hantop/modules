package com.fenlibao.p2p.service.xinwang.pay.impl;

import com.fenlibao.p2p.dao.xinwang.pay.XWFundsTransferDao;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWFundsTransfer;
import com.fenlibao.p2p.service.xinwang.pay.XWFundsTransferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date 2017/7/1 11:25
 */
@Service
public class XWFundsTransferServiceImpl implements XWFundsTransferService {
    @Resource
    private XWFundsTransferDao fundsTransferDao;
    @Override
    public void batchInsertFlow(List<XWFundsTransfer> transferList) {
        for (XWFundsTransfer xwFundsTransfer : transferList) {
            fundsTransferDao.insertFlow(xwFundsTransfer);
        }
    }
}
