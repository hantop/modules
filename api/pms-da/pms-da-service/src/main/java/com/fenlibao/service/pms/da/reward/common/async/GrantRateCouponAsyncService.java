package com.fenlibao.service.pms.da.reward.common.async;

import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.form.RateCouponDetailForm;

/**
 * Created by Administrator on 2016/9/12.
 */
public interface GrantRateCouponAsyncService {
    /**
     * 多线程发放加息券
     *
     * @param rewardRecord
     * @param rateCouponDetailForm
     */
    void multiThreadGrantRateCoupon(RewardRecord rewardRecord, RateCouponDetailForm rateCouponDetailForm);
}
