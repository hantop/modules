package com.fenlibao.p2p.util.api;

//import com.fenlibao.p2p.util.file.FileUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class StringHelper {
	private static final char[] HEXES = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final Pattern REPLACE_VARIABLE_PATTERN = Pattern.compile("\\$\\{\\s*(\\w|\\.|-|_|\\$)+\\s*\\}", 2);
	public static final String EMPTY_STRING = "";
	private static final byte[] ROW_BYTES = "80e36e39f34e678c".getBytes();
	protected static final DecimalFormat AMOUNT_SPLIT_FORMAT = new DecimalFormat("#,##,##0.00");
	public static final String INTEREST_FORMAT = "0.0#"; //利息格式，精确到小数点后两位
	public static final String AMOUNT_FORMAT = "#,##0.00";  //金额格式，每三位一个逗号，精确到小数点后两位

	public StringHelper() {
	}

	public static String trim(String value) {
		return value == null?null:value.trim();
	}

	public static boolean isEmpty(String value) {
		int length;
		if(value != null && (length = value.length()) != 0) {
			for(int index = 0; index < length; ++index) {
				char ch = value.charAt(index);
				if(ch != 32 && ch != 160 && !Character.isISOControl(ch)) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public static void filterHTML(Appendable writer, String str) throws IOException {
		if(!isEmpty(str)) {
			for(int i = 0; i < str.length(); ++i) {
				char ch = str.charAt(i);
				if(!Character.isISOControl(ch)) {
					switch(ch) {
						case '\"':
						case '&':
						case '\'':
						case '<':
						case '>':
							writer.append("&#");
							writer.append(Integer.toString(ch));
							writer.append(';');
							break;
						default:
							writer.append(ch);
					}
				}
			}

		}
	}

	public static void filterQuoter(Appendable writer, String str) throws IOException {
		if(!isEmpty(str)) {
			for(int i = 0; i < str.length(); ++i) {
				char ch = str.charAt(i);
				if(ch == 34) {
					writer.append('\\');
				}

				writer.append(ch);
			}

		}
	}

	public static void filterSingleQuoter(Appendable writer, String str) throws IOException {
		if(!isEmpty(str)) {
			for(int i = 0; i < str.length(); ++i) {
				char ch = str.charAt(i);
				if(ch == 39) {
					writer.append('\\');
				}

				writer.append(ch);
			}

		}
	}

	public static String digest(String content) throws Throwable {
		if(isEmpty(content)) {
			return content;
		} else {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] ciphertext = digest.digest(content.getBytes());
			char[] chars = new char[ciphertext.length + ciphertext.length];
			int i = 0;
			byte[] arr$ = ciphertext;
			int len$ = ciphertext.length;

			for(int i$ = 0; i$ < len$; ++i$) {
				byte element = arr$[i$];
				chars[i++] = HEXES[element & 15];
				chars[i++] = HEXES[element >> 4 & 15];
			}

			return new String(chars);
		}
	}

	public static String encode(String content) throws Exception {
		if(isEmpty(content)) {
			return content;
		} else {
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec keySpec = new SecretKeySpec(ROW_BYTES, "AES");
			cipher.init(1, keySpec);
			byte[] ciphertext = cipher.doFinal(content.getBytes());
			return Base64.encodeBase64String(ciphertext);
		}
	}

	public static String decode(String content) throws Exception {
		if(isEmpty(content)) {
			return content;
		} else {
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec keySpec = new SecretKeySpec(ROW_BYTES, "AES");
			cipher.init(2, keySpec);
			byte[] ciphertext = cipher.doFinal(Base64.decodeBase64(content));
			return new String(ciphertext);
		}
	}

	public static String truncation(String string, int maxLength) {
		if(isEmpty(string)) {
			return "";
		} else {
			try {
				StringBuilder e = new StringBuilder();
				truncation(e, string, maxLength, (String)null);
				return e.toString();
			} catch (IOException var3) {
				return "";
			}
		}
	}

	public static String truncation(String string, int maxLength, String replace) {
		if(isEmpty(string)) {
			return "";
		} else {
			try {
				StringBuilder e = new StringBuilder();
				truncation(e, string, maxLength, replace);
				return e.toString();
			} catch (IOException var4) {
				return "";
			}
		}
	}

	public static void truncation(Appendable out, String string, int maxLength) throws IOException {
		truncation(out, string, maxLength, (String)null);
	}

	public static void truncation(Appendable out, String string, int maxLength, String replace) throws IOException {
		if(!isEmpty(string) && maxLength > 0) {
			if(isEmpty(replace)) {
				replace = "...";
			}

			int index = 0;

			for(int end = Math.min(string.length(), maxLength); index < end; ++index) {
				out.append(string.charAt(index));
			}

			if(string.length() > maxLength) {
				out.append(replace);
			}

		}
	}
	

	/**
	 * 判断目标字符串是否为 null 或 "null"字符串 或 ""
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str)
    {
        if (null == str || "null".equals(str.toLowerCase())
                || str.equals(""))
        {
            return true;
        } else
            return false;
    }
	
	/**
	 * 字符串替换
	 * @param start 从1开始
	 * @param end   从1开始
	 * @param source  源字符串
	 * @param newChar 替换的字符串
	 * @return
	 */
	public static String replace(int start,int end,String source,String newChar){
		if(StringUtils.isEmpty(source)){
			return "";
		}
		StringBuilder sb = new StringBuilder(source);
		sb.replace(start, end, newChar);
		return sb.toString();
	}

	/**
	 * 银行卡号打星号返回(自行校验银行卡号)
	 * @param bankCardNo
	 * @return
     */
	public static String getBankCardNoAsterisk(String bankCardNo) {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isBlank(bankCardNo)) {
			return "";
		}
		sb.append(bankCardNo.substring(0, 3));
		sb.append("*************");
		sb.append(bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length()));
		return sb.toString();
	}

	/**
	 * 身份证号打星号返回
	 * 3-16位星号替换
	 * @param idCardNo
	 * @return
     */
	public static String getIdCardNoAsterisk(String idCardNo){
		String result=idCardNo.substring(0, 2)+"************" + idCardNo.substring(idCardNo.length()-4,idCardNo.length());
		return result;
	}

	/**
	 * 身份证号打星号返回
	 * 9-16位星号替换
	 * @param idCardNo
	 * @return
	 */
	public static String getIdCardReplace9To16(String idCardNo){
		String result = idCardNo.substring(0, 8) + "********" + idCardNo.substring(idCardNo.length() - 2,idCardNo.length());
		return result;
	}

	/**
	 * 手机号码4-7位用星号代替
	 * @param phone
	 * @return
	 */
	public static String getPhoneReplace4To7(String phone){
		String result = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4,phone.length());
		return result;
	}

	public static String getNoSensitiveName(String name) {
		int length = name.length();
		String result = name.substring(0,1)+"*";
		if (length == 3) {
			result = name.substring(0, 1) + "**";
		}
		if (length > 3) {
			result = name.substring(0, 2) + "**";
		}
		return result;
	}

	public static Date getBirthdayFromIdCard(String cardNo) {
		StringBuffer tempStr = new StringBuffer("");
		if (cardNo != null && cardNo.trim().length() > 0) {
			if (cardNo.trim().length() == 15) {
				tempStr.append(cardNo.substring(6, 12));
				tempStr.insert(4, '-');
				tempStr.insert(2, '-');
				tempStr.insert(0, "19");
			} else if (cardNo.trim().length() == 18) {
				tempStr = new StringBuffer(cardNo.substring(6, 14));
				tempStr.insert(6, '-');
				tempStr.insert(4, '-');
			}
		}
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(tempStr.toString());
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getAssetAccountName(String prefix, Integer userId) throws Exception{
		DecimalFormat df = new DecimalFormat("00000000000");
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(df.format(userId));
		return sb.toString();
	}

	/**
	 * 在字符串的n个位置插入字符串
	 * @param before
	 * @param n
	 * @param insert
	 * @return
	 */
	public static String  insertInString(String before, String[] insert, int... n){
		StringBuffer sb = new StringBuffer(before);
		StringBuffer after = new StringBuffer();
		int i = 0;
		for(String s: insert){
			after = sb.insert(n[i], s);
			i++;
		}
		String result = after.toString();
		return result;
	}

	/**
	 * 姓名带星返回
	 * @param name
	 * @return
	 */
	public static String getNameWithStarByName(String name){
		String secondName = name.substring(0, 1);
		String firstName = name.substring(1 ,name.length()).replaceAll(".", "\\*");
		return secondName + firstName;
	}

    public static String formatAmount(BigDecimal number){
        if (number == null)
        {
            return "";
        }
        return AMOUNT_SPLIT_FORMAT.format(number);
   }

	/**
	 * 传入一个数字与格式，返回一个既定格式的数字型字符串
	 * @param number
	 * @return
	 */
	public static String formatNumber(Object number, String pattern){
		if (number == null){
			number = 0;
		}
		if (pattern == null){
			pattern = "";
		}
		return new DecimalFormat(pattern).format(number);
	}

	/**
	 * 传入一个字符串，定义前缀长度，带几个*然后返回(目前用于地址加密)
	 * @param s 字符串
	 * @param prefixLength 留下来的前缀长度
	 * @param severalStar 带几个星
	 * @return
	 */
	public static String getStringPrefixWithSomeStar(String s, int prefixLength, int severalStar){
		if (s == null || prefixLength <= 0){
			s = "";
			prefixLength = 0;
		}

		if (s.length() <= prefixLength){
			prefixLength = s.length();
			severalStar = 0;
		}

		if (severalStar < 0){
			severalStar = 0;
		}

		StringBuilder sb = new StringBuilder("");
		for (int i = 0 ; i < severalStar ; i++){
			sb.append("*");
		}

		return s.substring(0, prefixLength) + sb.toString();
	}

	/**
	 * 传入一个字符串，定义前缀长度，后缀长度，中间带几个*然后返回
	 * @param s 字符串
	 * @param prefixLength 留下来的前缀长度
	 * @param suffixLength 留下来的后缀长度
	 * @param severalStar 带几个星
	 * @return
	 */
	public static String getStringWithSomeStar(String s, int prefixLength, int suffixLength, int severalStar){
		if (s == null){
			s = "";
		}
		if (prefixLength < 0){
			prefixLength = 0;

		}
		if (suffixLength < 0){
			suffixLength = 0;
		}

		if (severalStar < 0){
			severalStar = 0;
		}

		if (s.length() <= prefixLength){
			prefixLength = s.length();
		}
		if (s.length() <= suffixLength){
			suffixLength = s.length();
		}

		StringBuilder sb = new StringBuilder("");
		for (int i = 0 ; i < severalStar ; i++){
			sb.append("*");
		}

		return s.substring(0, prefixLength) + sb.toString() + s.substring(s.length() - suffixLength, s.length());
	}

	/**
	 * 替换多个字符
	 * @param start 从1开始
	 * @param end 从1开始
	 * @param source 原字符串
	 * @param newStr 替换字符
	 * @return
	 */
	public static String recurseReplace(int start, int end, String source, String newStr) {
		if(StringUtils.isEmpty(source)){
			return "";
		}
		if (start >= end) {
			return source;
		}
		int i = end - start;
		StringBuilder sb = new StringBuilder(newStr);
		while (i-- > 0) {
			sb.append(newStr);
		}
		StringBuilder result = new StringBuilder(source);
		result.replace(start, end, sb.toString());
		return result.toString();
	}

//	public static void main(String[] args) {
//		System.out.println(addStar("广东",2));
//	}
}
