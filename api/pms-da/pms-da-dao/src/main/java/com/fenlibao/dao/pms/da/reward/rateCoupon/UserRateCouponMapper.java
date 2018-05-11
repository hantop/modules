package com.fenlibao.dao.pms.da.reward.rateCoupon;

import com.fenlibao.model.pms.da.reward.RateCouponGrantStatistics;
import com.fenlibao.model.pms.da.reward.UserRateCoupon;
import com.fenlibao.model.pms.da.reward.form.RateCouponDetailForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Set;

/**
 * 加息券
 * <p>
 * Created by chenzhixuan on 2016/8/25.
 */
public interface UserRateCouponMapper {

    /**
     * 根据加息券ID获取用户加息券数量
     *
     * @param couponId
     * @return
     */
    int getUserCouponCountByCouponId(int couponId);

    /**
     * 批量插入用户加息券
     *
     * @param insertList
     */
    void batchInsert(Set<UserRateCoupon> insertList);

    List<UserRateCoupon> findPager(RateCouponDetailForm rateCouponDetailForm, RowBounds bounds);

    List<RateCouponGrantStatistics> getRateCouponGrantStatistics(Integer grantId);

    List<UserRateCoupon> findAll(RateCouponDetailForm rateCouponDetailForm);

    /**
     * 根据发放ID和手机号获取用户加息券
     *
     * @param item
     * @return
     */
    List<UserRateCoupon> getUserRateCouponByGrantIdAndPhone(UserRateCoupon item);

    void batchUpdateUserRateCoupon(List<UserRateCoupon> userRateCouponList);
}
