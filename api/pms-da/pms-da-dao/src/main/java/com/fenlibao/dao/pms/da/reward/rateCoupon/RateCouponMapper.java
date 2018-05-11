package com.fenlibao.dao.pms.da.reward.rateCoupon;

import com.fenlibao.model.pms.da.reward.RateCoupon;
import com.fenlibao.model.pms.da.reward.form.RateCouponEditForm;
import com.fenlibao.model.pms.da.reward.form.RateCouponForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 加息券
 * <p>
 * Created by chenzhixuan on 2016/8/25.
 */
public interface RateCouponMapper {


    /**
     * 加息券列表
     *
     * @param rateCouponForm
     * @param bounds
     * @return
     */
    List<RateCoupon> findRateCoupons(RateCouponForm rateCouponForm, RowBounds bounds);

    /**
     * 删除加息券对应的标类型
     *
     * @param ids
     */
    void deleteRateCouponBidTypes(List<Integer> ids);

    /**
     * 删除加息券
     *
     * @param ids
     * @return
     */
    int deleteRateCoupon(List<Integer> ids);

    /**
     * 获取加息券
     *
     * @param id
     * @return
     */
    RateCoupon getRateCouponById(int id);

    /**
     * 根据编码获取加息券
     *
     * @param code
     * @return
     */
    RateCoupon getRateCouponByCode(String code);

    /**
     * 修改加息券
     *
     * @param rateCouponEditForm
     * @return
     */
    int updateRateCoupon(RateCouponEditForm rateCouponEditForm);

    /**
     * 新增加息券
     *
     * @param rateCouponEditForm
     * @return
     */
    int insertRateCoupon(RateCouponEditForm rateCouponEditForm);

    /**
     * 根据加息券ID获取加息券对应的标类型
     *
     * @param couponId
     * @return
     */
    List<Integer> getBidTypeIdsByRateCouponId(int couponId);

    /**
     * 根据加息券ID删除加息券对应的标类型
     *
     * @param couponId
     */
    void deleteBidTypes(int couponId);

    /**
     * 根据加息券ID新增加息券对应的标类型
     *
     * @param couponId
     * @param bidTypeIds
     */
    void insertBidTypes(@Param("couponId") int couponId, @Param("bidTypeIds") List<Integer> bidTypeIds);

}
