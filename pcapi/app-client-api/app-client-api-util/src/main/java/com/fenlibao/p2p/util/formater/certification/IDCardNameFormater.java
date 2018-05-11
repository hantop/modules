package com.fenlibao.p2p.util.formater.certification;

import org.apache.commons.lang3.StringUtils;

/**
 * 身份证姓名格式化
 * Created by chenzhixuan on 2015/9/10.
 */
public class IDCardNameFormater {

    private static final String symbol = "*";

    public static String format(String identityName) {
        if(StringUtils.isNotBlank(identityName)
                && identityName.length() >= 2) {
            StringBuffer result = new StringBuffer(identityName);
            int start;
            if(identityName.length() < 4) {
                start = 1;
            } else {
                start = 2;
            }
            for(int i = start; i < identityName.length(); i++) {
                result.replace(i, i + 1, symbol);
            }
            return result.toString();
        }
        return null;
    }

}
