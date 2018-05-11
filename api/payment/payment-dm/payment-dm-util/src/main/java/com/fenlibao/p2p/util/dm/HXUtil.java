package com.fenlibao.p2p.util.dm;

import com.fenlibao.p2p.model.dm.config.HXConfig;
import org.aeonbits.owner.ConfigCache;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zcai on 2016/9/22.
 */
public class HXUtil {

    public static HXConfig CONFIG;
    static {
        CONFIG = ConfigCache.getOrCreate(HXConfig.class);
    }

    public static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HHmmss");

    public static String getTestFlowNum() {
        return UUID.randomUUID().toString().replace("-","").substring(4);
    }

    /**
     * 流水号需严格按照华兴文档
     * @param tradeCode
     * @param orderId
     * @return
     */
    public static String getChannelFlow(String tradeCode, int orderId) {
        if (StringUtils.isBlank(tradeCode) || orderId < 1) {
            return "";
        }
        return CONFIG.channelCode() + getSimpleDate(new Date())
                + tradeCode.substring(tradeCode.length() - 3, tradeCode.length())
                + String.format("%011d", orderId);
    }

    /**
     * 查询用的流水号（查询不产生订单）
     * (count使用redis计数器)
     * @param tradeCode
     * @return
     */
    public static synchronized String getChannelFlowForQuery(String tradeCode) {
        if (StringUtils.isBlank(tradeCode)) {
            return "";
        }
        Date now = new Date();
        return CONFIG.channelCode() + getSimpleDate(now)
                + tradeCode.substring(tradeCode.length() - 3, tradeCode.length())
                + getSimpleTime(now)
                + UUID.randomUUID().toString().replaceAll("-","").substring(27);//String.format("%05d", count)
    }

    /**
     * 获取YYYYMMDD格式
     * @param date
     * @return
     */
    public static synchronized String getSimpleDate(Date date) {
        return YYYYMMDD.format(date);
    }
    
    /**
     * 获取YYYYMMDD格式
     * @param date
     * @return
     */
    public static synchronized String getSimpleTime(Date date) {
    	return HHMMSS.format(date);
    }

    /**
     * 时间转字符串yyyyMMddHHmmss
     * @param date
     * @return
     */
    public static synchronized String getSimpleDateTime(Date date) {
        return simpleDateTimeFormat.format(date);
    }


    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        String a;
        List<String> str = new ArrayList<>();
        for (int i=0;i<10;i++) {
            a = sdf.format(new Date());
            if (str.contains(a)) {
                System.out.println(a);
            } else {
                str.add(a);
            }
        }
    }
}
