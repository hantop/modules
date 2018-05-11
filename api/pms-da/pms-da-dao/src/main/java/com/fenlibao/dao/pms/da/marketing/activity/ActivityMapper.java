package com.fenlibao.dao.pms.da.marketing.activity;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.marketing.activity.Activity;
import com.fenlibao.model.pms.da.marketing.activity.ActivityRateCoupon;

/**
 * 活动配置
 * @author Administrator
 *
 */
public interface ActivityMapper {
	/**
	 * 根据加息券ID获取活动加息券数量
	 *
	 * @param rateCouponId
	 * @return
	 */
	int getActivityRateCouponCount(int rateCouponId);

	/**
	 * 分页查询活动列表
	 * @param bounds
	 * @return
	 */
    List<Activity> getActivityList(RowBounds bounds);
    
    /**
     * 根据code查询活动
     * @param activity
     * @return
     */
    int selectActivityCount(Activity activity);

    /**
     * 根据code查询活动ID
     * @param activityCode
     * @return
     */
    Integer getActivityIdByCode(String activityCode);
    
    /**
     * 编辑
     * @param activity
     * @return
     */
    int updateByPrimaryKeySelective(Activity activity);
    
    /**
     * 新增活动
     * @param activity
     * @return
     */
    Integer insertSelective(Activity activity);

    /**
     * 根据id查询活动
     * @param id
     * @return
     */
	Activity getActivityById(int id);

	/**
	 * 活动添加加息券
	 * @param rateCouponId
	 */
	void addRateCoupon(@Param(value = "activityId")int activityId, 
			@Param(value = "rateCouponId")Integer rateCouponId, 
			@Param(value = "startTime")Date startTime, 
			@Param(value = "endTime")Date endTime);


	/**
	 * 
	 * @param id
	 * @return
	 */
	List<Integer> getCouponIdsByActivityId(int activityId);

	/**
	 * 删除原有的数据
	 * @param id
	 * @param oldType
	 */
	void deleteActivityCoupon(@Param(value = "activityId")int activityId, @Param(value = "oldType")Integer oldType);

	/**
	 * 活动对应优惠券
	 * @param id
	 * @return
	 */
	List<ActivityRateCoupon> getCouponListByActivityId(int id);

	
	List<ActivityRateCoupon> findAllRateCoupons(RowBounds bounds);

	/**
	 * 更新中间表
	 * @param activityId
	 * @param newIds
	 * @param timeStart
	 * @param timeEnd
	 */
	void updateRatrCoupon(@Param(value = "activityId")int activityId, 
			@Param(value = "newId")Integer newId, 
			@Param(value = "timeStart")Date timeStart, 
			@Param(value = "timeEnd")Date timeEnd);
}
