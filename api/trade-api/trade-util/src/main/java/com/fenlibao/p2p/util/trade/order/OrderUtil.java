package com.fenlibao.p2p.util.trade.order;

import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.redis.RedisConst;
import com.fenlibao.p2p.util.api.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by zcai on 2017/2/12.
 */
public class OrderUtil {


    /**
     * 获取订单号
     * @param prefix
     * @return
     */
    public static String getOrderNo(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return "";
        }
        int count = RedisUtil.counter(RedisConst.COUNTER_KEY_ORDER_NO, RedisConst.COUNTER_TIMEOUT_ORDER_NO);
        String countStr = String.format("%03d", count);
        return prefix.concat(DateUtil.getYYYYMMDDHHMMSS(new Date()).concat(countStr));
    }

    /**
     * 流水号
     * @return
     */
    public static String getFlowNo() {
        int count = RedisUtil.counter(RedisConst.COUNTER_KEY_FLOW_NO, RedisConst.COUNTER_TIMEOUT_ORDER_NO);
        String countStr = String.format("%04d", count);
        return DateUtil.getYYYYMMDDHHMMSS(new Date()).concat(countStr);
    }

}
