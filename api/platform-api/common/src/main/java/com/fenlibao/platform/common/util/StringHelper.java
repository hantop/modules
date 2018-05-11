package com.fenlibao.platform.common.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class StringHelper {
	
	private static final char[] HEXES = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	public static final String EMPTY_STRING = "";
	private static final byte[] ROW_BYTES = "80e36e39f34e678c".getBytes();
	
	private static final int  deviation_num=1000;

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

	public static String encode(String content) throws Throwable {
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

	public static String decode(String content) throws Throwable {
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
	 * 校验手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^(13|14|15|16|17|18|19)[0-9]{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	
	/**
	 * id 偏移
	 * @return
	 */
	public static String  deviation(int planId){
		return String.valueOf(planId+deviation_num);
	}
	
	


}
