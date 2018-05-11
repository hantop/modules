package com.fenlibao.p2p.util.loan;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class LoanUtil {

	private static final String PREFIX = "JK";
	private static final String FORMAT = "yyMMdd";
	private static final String START = "0000";
	public static final String END = "9999";
	
	private static String getDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
		return sdf.format(new Date());
	}
	
	public static String generateCode(String code) {
		String suffix = START;
		if (StringUtils.isNotBlank(code)) {
			String dateStr = code.substring(2, 8);
			if (getDateStr().equals(dateStr)) {
				Integer No = Integer.valueOf(code.substring(8, 12));
				if (END.equals(No.toString())) {
					return END;
				}
				No++;
				suffix = String.format("%04d", No);
			}
		}
		return PREFIX + getDateStr() + suffix;
	}
	
}
