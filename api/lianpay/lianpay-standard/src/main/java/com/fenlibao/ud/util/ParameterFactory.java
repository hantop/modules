package com.fenlibao.ud.util;


import com.fenlibao.ud.config.DemoConfig;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口调用参数生成工厂类.
 *
 * @author hush@udcredit.com
 * @version 1.0
 * @date 10/29/15
 */
public final class ParameterFactory {

    /**
     * 获取实名认证packageStr的Map.
     *
     * @param name 姓名.
     * @param idNumber 身份证.
     * @param outerOrderId 外部订单号.
     * @return 字段HashMap
     */
    public static Map<String, String> getShimingParameterMap(String name, String idNumber,
            String outerOrderId) {
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("partner_code", DemoConfig.PARTNER_CODE);
        event.put("event_id", newEventId());
        event.put("event_time", newEventTime());

        event.put("strategy_code", DemoConfig.SHIMING_STRATEGY_CODE);
        event.put("scenario_code", DemoConfig.SHIMING_SENARIO_CODE);

        // Biz data
        event.put("name", name);
        event.put("id_number", idNumber);
        event.put("out_order_id", outerOrderId);
        return event;
    }

    private static String newEventTime() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    private static String newEventId() {
        return DemoConfig.PARTNER_CODE_POSTFIX
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + RandomStringUtils.randomNumeric(6);
    }

    private ParameterFactory() {}
}
