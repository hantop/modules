package com.fenlibao.p2p.util.formater.certification;

import org.apache.commons.lang3.StringUtils;

/**
 * 銀行卡格式化
 * Created by chyenzhixuan on 2015/9/10.
 */
public class BankCardFormater {

    private static final String symbol = "*";

    /**
     * 銀行卡式化
     *
     * @param plaintext 銀行卡號
     * @return
     */

    public static String format(String plaintext) {
        if (StringUtils.isNotBlank(plaintext)
                && plaintext.length() > 4) {
            StringBuffer result = new StringBuffer(plaintext);
            for(int i = 4; i < plaintext.length() - 4; i++) {
                result.replace(i, i + 1, symbol);
            }
            return result.toString();
        }
        return null;
    }
    
}
