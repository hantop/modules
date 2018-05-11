package com.fenlibao.common.pms.util.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lullaby on 2015-12-28 17:14
 */
public class TimeUtil {

    private static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static String dateToString(Date date) {
        return date == null ? null : new SimpleDateFormat(YYYYMMDDHHMMSS).format(date);
    }

}
