package com.fenlibao.service.pms.da.reward.rateCoupon;

import com.fenlibao.model.pms.da.reward.RateCoupon;
import com.fenlibao.model.pms.da.reward.form.RateCouponEditForm;
import com.fenlibao.model.pms.da.reward.form.RateCouponForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 加息券
 * <p>
 * Created by chenzhixuan on 2016/8/25.
 */
public interface RateCouponService {

    /**
     * 加息券列表
     *
     * @param rateCouponForm
     * @param bounds
     * @return
     */
    List<RateCoupon> findRateCoupons(RateCouponForm rateCouponForm, RowBounds bounds);

    /**
     * 删除加息券
     *
     * @param ids
     * @return
     */
    int rateCouponRemove(List<Integer> ids);

    /**
     * 获取加息券
     *
     * @param id
     * @return
     */
    RateCoupon getRateCouponById(int id);

    /**
     * 新增或更新加息券
     *
     * @param rateCouponEditForm
     * @return
     */
    int saveOrUpdateRateCoupon(RateCouponEditForm rateCouponEditForm);

    /**
     * 加息券是否可操作(修改、删除)
     *
     * @param id
     * @return
     */
    boolean isCanOperate(int id);
}
