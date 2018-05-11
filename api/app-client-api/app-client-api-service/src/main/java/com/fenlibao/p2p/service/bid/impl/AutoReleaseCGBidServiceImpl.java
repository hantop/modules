package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.user.AutoReleaseCGBidDao;
import com.fenlibao.p2p.model.entity.bid.AutoReleaseCGBidInfo;
import com.fenlibao.p2p.service.bid.AutoReleaseCGBidService;
import com.fenlibao.p2p.service.xinwang.project.XWEstablishProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 将录入的存管消费信贷标调用存管接口进行发布
 * Created by xiao on 2016/11/13.
 */
@Service
public class AutoReleaseCGBidServiceImpl implements AutoReleaseCGBidService {
    private static final Logger logger = LogManager.getLogger(AutoReleaseCGBidServiceImpl.class);

    @Resource
    private AutoReleaseCGBidDao autoReleaseCGBidDao;
    @Resource
    private XWEstablishProjectService xwEstablishProjectService;
    
    /**
     * 自动发布
     */
    @Override
    public void startAutoReleaseCGBid() throws Throwable {
        AutoReleaseCGBidInfo autoReleaseCGBidInfoLast = null;
        while (true) {
            AutoReleaseCGBidInfo autoReleaseCGBidInfoTemp = null;
            try {
                autoReleaseCGBidInfoTemp = releaseCGBid();
            } catch (Throwable e) {
                logger.error(e, e);
            }
            if ((autoReleaseCGBidInfoLast == autoReleaseCGBidInfoTemp) || (autoReleaseCGBidInfoLast != null && autoReleaseCGBidInfoLast.equals(autoReleaseCGBidInfoTemp))) {
                break;
            } else {
                autoReleaseCGBidInfoLast = autoReleaseCGBidInfoTemp;
            }
        }
    }

    @Transactional
    public AutoReleaseCGBidInfo releaseCGBid() throws Throwable {
        AutoReleaseCGBidInfo autoReleaseCGBidInfo = getAndLockYCLBidInfo();
        if (autoReleaseCGBidInfo != null) {
            //获取待处理的标，加锁
            autoReleaseCGBidDao.lockYCLBidInfo(autoReleaseCGBidInfo.getId());
        } else {
            logger.info("未发现需要处理的标");
            return autoReleaseCGBidInfo;
        }
        //调用新网接口，发布
        String res = establishProject(autoReleaseCGBidInfo.getBidId());
        if ("TBZ".equals(res)) {
            //成功，修改t_consume_bidinfo 发布状态
            updateConsumeBidInfoStatus(autoReleaseCGBidInfo.getId(), "CLCG");
            //加入债权库
            addToProductLib(autoReleaseCGBidInfo.getBidId());
        } else {
            //失败，修改t_consume_bidinfo 存管发布失败次数
            updateConsumeBidInfoStatus(autoReleaseCGBidInfo.getId(), null);
        }
        return autoReleaseCGBidInfo;
    }

    public AutoReleaseCGBidInfo getAndLockYCLBidInfo() {
        AutoReleaseCGBidInfo autoReleaseCGBidInfo = autoReleaseCGBidDao.getYCLBidInfo();
        return autoReleaseCGBidInfo;
    }

    /**
     * 调用新网接口发布标
     *
     * @param loanId
     * @return
     */
    public String establishProject(Integer loanId) {
        String status = null;
        try {
            xwEstablishProjectService.establishProject(loanId);
            status = autoReleaseCGBidDao.getBidStatus(loanId);
            return status;
        } catch (Throwable e) {
            logger.error("[调用存管发布产生异常:]" + e.getMessage(), e);
        }
        return status;
    }

    /**
     * 加入产品库
     *
     * @param bid
     * @throws Throwable
     */
    public void addToProductLib(int bid) throws Throwable {
        autoReleaseCGBidDao.addToProductLib(bid);
    }

    /**
     * 修改 t_consume_bidinfo 发布状态
     *
     * @param consumeBidInfoid
     * @param status
     * @throws Throwable
     */
    public void updateConsumeBidInfoStatus(int consumeBidInfoid, String status) throws Throwable {
        Map map = new HashMap();
        map.put("id", consumeBidInfoid);
        map.put("status", status);
        autoReleaseCGBidDao.updateConsumeBidInfoStatus(map);
    }
}
