package com.fenlibao.service.pms.da.marketing.activity;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.marketing.activity.Activity;
import com.fenlibao.model.pms.da.marketing.activity.ActivityRateCoupon;

/**
 * 活动设置
 * @author Administrator
 *
 */
public interface ActivityService {
	
	/**
	 * 分页查询活动列表
	 * @param bounds
	 * @return
	 */
    List<Activity> getActivityList(RowBounds bounds);

    /**
     * 
     * @param activity
     * @return
     */
	int saveOrUpdateActivity(Activity activity);

	/**
	 * 根据id查询活动
	 * @param id
	 * @return
	 */
	Activity getActivityById(int id);

	/**
	 * 活动添加加息券
	 * @param ids
	 * @return
	 */
	int addRateCoupons(int activityId, List<Integer> ids);

	/**
	 * 该活动对应的优惠券
	 * @param id
	 * @return
	 */
	List<ActivityRateCoupon> getCouponListByActivityId(int id);

	/**
	 * 所有活动优惠券
	 * @param object
	 * @param bounds
	 * @return
	 */
	List<ActivityRateCoupon> findAllRateCoupons(RowBounds bounds);

}
