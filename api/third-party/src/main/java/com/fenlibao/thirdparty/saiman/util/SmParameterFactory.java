package com.fenlibao.thirdparty.saiman.util;


import java.util.HashMap;
import java.util.Map;

import com.fenlibao.thirdparty.saiman.config.SmDemoConfig;

/**
 * Created by Louis Wang on 2016/3/14.
 */

public class SmParameterFactory {

    /**
     * 获取实名认证packageStr的Map.
     *
     * @param name 姓名.
     * @param idNumber 身份证.
     * @param outerOrderId 外部订单号.
     * @return 字段HashMap
     */
    public static Map<String, String> getBsParameterMap(String name, String idNumber, String outerOrderId) {
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SmDemoConfig.PARTNER_CODE);
        // Biz data
        event.put("idcode", idNumber);
        event.put("idname", name);
        event.put("ordersn", outerOrderId);
        return event;
    }
}
