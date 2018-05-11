package com.fenlibao.p2p.util.formater.certification;

import org.apache.commons.lang3.StringUtils;

/**
 * 身份证号码格式化
 * Created by chyenzhixuan on 2015/9/10.
 */
public class IDCardFormater {

    private static final String symbol = "*";

    /**
     * 明文身份证格式化
     *
     * @param plaintext 明文身份证
     * @return
     */
    public static String format(String plaintext) {
        if (StringUtils.isNotBlank(plaintext)
                && plaintext.length() > 4) {
            StringBuffer result = new StringBuffer(plaintext);
            for(int i = 0; i < plaintext.length() - 4; i++) {
                result.replace(i, i + 1, symbol);
            }
            return result.toString();
        }
        return null;
    }
}
