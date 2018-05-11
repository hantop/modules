package com.fenlibao.p2p.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 根据数字返回标/计划状态
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
}
