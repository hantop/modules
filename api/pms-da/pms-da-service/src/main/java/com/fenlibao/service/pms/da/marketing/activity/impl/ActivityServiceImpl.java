package com.fenlibao.service.pms.da.marketing.activity.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.fenlibao.dao.pms.da.marketing.activity.ActivityMapper;
import com.fenlibao.model.pms.da.marketing.activity.Activity;
import com.fenlibao.model.pms.da.marketing.activity.ActivityRateCoupon;
import com.fenlibao.service.pms.da.marketing.activity.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    ActivityMapper activityMapper;

    @Override
    public List<Activity> getActivityList(RowBounds bounds) {

        List<Activity> activityList = new ArrayList<>();
        activityList = activityMapper.getActivityList(bounds);
        return activityList;
    }

	@Override
	public int saveOrUpdateActivity(Activity activity) {
		if (activity.getId() > 0) {
			// 在还没有发送之前还是允许修改的
			this.activityMapper.updateByPrimaryKeySelective(activity);
			return 1;
		} else {
			int count = activityMapper.selectActivityCount(activity);
			if (count > 0) {
				return -1;
			} else {
				this.activityMapper.insertSelective(activity);
				return 1;
			}
		}
	}

	@Override
	public Activity getActivityById(int id) {
		return activityMapper.getActivityById(id);
	}

	/**
	 * 活动添加加息券
	 */
	@Override
	public int addRateCoupons(int activityId, List<Integer> ids) {
		Activity activity = activityMapper.getActivityById(activityId);
		activity.setCouponIds(ids);
		if (activity.getId() > 0) {
			//查询原来的中间表里面的信息
			List<Integer> oldRateCouponIds = this.activityMapper.getCouponIdsByActivityId(activity.getId());
			//遍历原来的
			for (Integer oldType : oldRateCouponIds) {
				//如果刚传进去的id不包括原来的
				if (!activity.getCouponIds().contains(oldType)) {
					//删除原来的
					this.activityMapper.deleteActivityCoupon(activity.getId(),oldType);
				}
			}
			//遍历新传进来的
			for (Integer couponTypeId : activity.getCouponIds()) {
				//如果旧的不包括新的,新增新的
				if (!oldRateCouponIds.contains(couponTypeId)) {
					activityMapper.addRateCoupon(activityId, couponTypeId, activity.getTimeStart(), activity.getTimeEnd());
				}
			}
			List<Integer> newRateCouponIds = this.activityMapper.getCouponIdsByActivityId(activity.getId());
			for (Integer newIds : newRateCouponIds) {
				activityMapper.updateRatrCoupon(activityId, newIds, activity.getTimeStart(), activity.getTimeEnd());
			}
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public List<ActivityRateCoupon> getCouponListByActivityId(int id) {
		return activityMapper.getCouponListByActivityId(id);
	}

	@Override
	public List<ActivityRateCoupon> findAllRateCoupons(RowBounds bounds) {
		return activityMapper.findAllRateCoupons(bounds);
	}

}
