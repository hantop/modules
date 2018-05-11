package com.fenlibao.p2p.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2017/3/29.
 */
public class StatusUtil {
    public static String status(String statusNum){
        if(!StringUtils.isEmpty(statusNum)){
            if(statusNum.equals("1")){
                return "DTJ";
            }else if (statusNum.equals("2")){
                return "DSH";
            }else if (statusNum.equals("3")){
                return "DFB";
            }else if (statusNum.equals("4")){
                return "TBZ";
            }else if (statusNum.equals("5")){
                return "HKZ";
            }else if (statusNum.equals("6")){
                return "YJQ";
            }else if (statusNum.equals("7")){
                return "YZF";
            }else if (statusNum.equals("8")){
                return "YFB";
            }
        }
        return null;
    }

    public static int investStatus(String statusNum){
        if(!StringUtils.isEmpty(statusNum)){
            if ("TBZ".equals(statusNum) || "DFK".equals(statusNum)) {
                return 1;
            }
            if ("HKZ".equals(statusNum)) {
                return 2;
            }
            if ("YJQ".equals(statusNum)) {
                return 6;
            }
            if("ZRZ".equals(statusNum)) {
                return 3;
            }
        }
        return 0;
    }
}
