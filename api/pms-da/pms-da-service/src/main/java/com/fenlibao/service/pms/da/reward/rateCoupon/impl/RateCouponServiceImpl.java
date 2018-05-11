package com.fenlibao.service.pms.da.reward.rateCoupon.impl;

import com.fenlibao.dao.pms.da.marketing.activity.ActivityMapper;
import com.fenlibao.dao.pms.da.reward.common.RewardMapper;
import com.fenlibao.dao.pms.da.reward.rateCoupon.RateCouponMapper;
import com.fenlibao.dao.pms.da.reward.rateCoupon.UserRateCouponMapper;
import com.fenlibao.model.pms.da.reward.RateCoupon;
import com.fenlibao.model.pms.da.reward.form.RateCouponEditForm;
import com.fenlibao.model.pms.da.reward.form.RateCouponForm;
import com.fenlibao.service.pms.da.reward.rateCoupon.RateCouponService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by chenzhixuan on 2016/8/25.
 */
@Service
public class RateCouponServiceImpl implements RateCouponService {
    @Autowired
    private RateCouponMapper rateCouponMapper;
    @Autowired
    private UserRateCouponMapper userRateCouponMapper;
    @Autowired
    private RewardMapper rewardMapper;
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<RateCoupon> findRateCoupons(RateCouponForm rateCouponForm, RowBounds bounds) {
        return rateCouponMapper.findRateCoupons(rateCouponForm, bounds);
    }

    @Transactional
    @Override
    public int rateCouponRemove(List<Integer> ids) {
        rateCouponMapper.deleteRateCouponBidTypes(ids);
        return rateCouponMapper.deleteRateCoupon(ids);
    }

    @Override
    public RateCoupon getRateCouponById(int id) {
        return rateCouponMapper.getRateCouponById(id);
    }

    @Transactional
    @Override
    public int saveOrUpdateRateCoupon(RateCouponEditForm rateCouponEditForm) {
        RateCoupon rateCoupon = rateCouponMapper.getRateCouponByCode(rateCouponEditForm.getCouponCode());
        int couponId = rateCouponEditForm.getId();
        if (couponId > 0) {// 修改
            if (rateCoupon != null && couponId != rateCoupon.getId()) {
                return -1;
            } else {
                // 根据加息券ID删除加息券对应的标类型
                rateCouponMapper.deleteBidTypes(couponId);
                // 新增加息券对应的标类型
                rateCouponMapper.insertBidTypes(rateCouponEditForm.getId(), rateCouponEditForm.getBidTypeIds());
                return rateCouponMapper.updateRateCoupon(rateCouponEditForm);
            }
        } else {// 新增
            if (rateCoupon != null) {
                return -1;
            } else {
                // 新增加息券对应的标类型
                int result = rateCouponMapper.insertRateCoupon(rateCouponEditForm);
                rateCouponMapper.insertBidTypes(rateCouponEditForm.getId(), rateCouponEditForm.getBidTypeIds());
                return result;
            }
        }
    }

    @Override
    public boolean isCanOperate(int id) {
        // 根据加息券ID获取用户加息券数量
        int userCouponCount = userRateCouponMapper.getUserCouponCountByCouponId(id);
        if(userCouponCount == 0) {
            // 根据加息券ID获取活动加息券数量
            return activityMapper.getActivityRateCouponCount(id) == 0;
        } else {
            return false;
        }
    }


}

