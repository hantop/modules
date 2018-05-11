package com.fenlibao.p2p.service.xinwang.coupon.impl;

import com.fenlibao.p2p.dao.xinwang.coupon.SysCouponManageDao;
import com.fenlibao.p2p.dao.xinwang.project.SysBidManageDao;
import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.user.XWUserRedpacket;
import com.fenlibao.p2p.model.xinwang.enums.coupon.CouponState;
import com.fenlibao.p2p.service.xinwang.coupon.SysCouponManageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date 2017/6/1 11:16
 */
@Service
public class SysCouponManageServiceImpl implements SysCouponManageService {
    private static final Logger logger = LogManager.getLogger(SysCouponManageService.class);

    @Resource
    private SysCouponManageDao couponManageDao;
    @Resource
    private SysBidManageDao bidManageDao;


    @Transactional
    @Override
    public void updateRedpacket(CouponState state, Integer tenderId, String... ids) throws Exception {
        for (int i = 0; i < ids.length; i++) {
            couponManageDao.updateRedpacket(Integer.parseInt(ids[i]), state, tenderId);
        }
    }

    @Override
    public void updateUserCoupon(Integer jxqId, CouponState lock, int tenderId) {
        couponManageDao.updateUserCoupon(jxqId, lock, tenderId);
    }

    @Override
    public List<XWUserRedpacket> getUserRedpacket(int userId, CouponState state, Integer tenderId) {
        return couponManageDao.getUserRedpacket(userId, state, tenderId);
    }

    @Override
    public XWUserCoupon getUserCouponByTenderId(int tenderId) {
        return couponManageDao.getUserCouponByTenderId(tenderId);
    }

    @Override
    public List<XWUserCoupon> getUserCoupon(int userId, CouponState state, int tenderId) {
        return couponManageDao.getUserCounpon(userId, state, tenderId);
    }
}
