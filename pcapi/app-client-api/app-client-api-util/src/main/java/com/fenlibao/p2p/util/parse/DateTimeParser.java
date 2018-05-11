package com.fenlibao.p2p.util.parse;

import com.fenlibao.p2p.util.StringHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LouisWang on 2015/8/11.
 */
public class DateTimeParser {
    public DateTimeParser() {
    }

    public static Date parse(String value) {
        return parse(value, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String value, String pattern) {
        if(StringHelper.isEmpty(value)) {
            return null;
        } else {
            if(StringHelper.isEmpty(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat fmt = new SimpleDateFormat(pattern);

            try {
                return fmt.parse(value);
            } catch (ParseException var4) {
                return null;
            }
        }
    }

    public static Date[] parseArray(String[] values) {
        return parseArray(values, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date[] parseArray(String[] values, String pattern) {
        if(values != null && values.length != 0) {
            if(StringHelper.isEmpty(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat fmt = new SimpleDateFormat(pattern);
            Date[] dates = new Date[values.length];

            for(int i = 0; i < values.length; ++i) {
                if(!StringHelper.isEmpty(values[i])) {
                    try {
                        dates[i] = fmt.parse(values[i]);
                    } catch (ParseException var6) {
                        ;
                    }
                }
            }

            return dates;
        } else {
            return null;
        }
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String pattern) {
        if(date == null) {
            return "";
        } else {
            if(StringHelper.isEmpty(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat format = new SimpleDateFormat(pattern);

            try {
                return format.format(date);
            } catch (IllegalArgumentException var4) {
                return "";
            }
        }
    }
}
