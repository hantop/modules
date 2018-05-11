package com.fenlibao.p2p.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.fenlibao.p2p.util.api.file.FileUtils;

public class StringHelper {
	private static final char[] HEXES = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final Pattern REPLACE_VARIABLE_PATTERN = Pattern.compile("\\$\\{\\s*(\\w|\\.|-|_|\\$)+\\s*\\}", 2);
	public static final String EMPTY_STRING = "";
	private static final byte[] ROW_BYTES = "80e36e39f34e678c".getBytes();

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
	 * @Title: format 
	 * @Description: <p>\r\n\t<img src=\"${7df-b-a-1-16a.jpg}\" alt=\"\" />\ 
	 *                  格式 转换成 带url格式“<p>\r\n\t<img src=\"http://" alt=\"\" />\”
	 * @param pattern
	 * @param urlPrefix
	 * @return
	 * @throws Exception
	 * @return: String
	 */
	public static String format(String pattern, String urlPrefix) throws Exception
	{
	    StringBuilder builder = new StringBuilder();
	    format(builder, pattern, urlPrefix);
	    return builder.toString();
	}

	public static void format(Appendable out, String pattern, String urlPrefix) throws IOException
	{
	    if ((out == null)) {
	    	return;
	    }
	    Set loadedKeys = new HashSet();
	    format(out, pattern, loadedKeys, urlPrefix);
		try {
			System.out.println(StringHelper.decode("lp2jvqoNGws9o8WFgqn55C4Nswyt86z8nx6KtPtRpJU="));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	private static final void format(Appendable out, String pattern, Set<String> loadedKeys, String urlPrefix) throws IOException{
		Matcher matcher = REPLACE_VARIABLE_PATTERN.matcher(pattern);
		int startIndex = 0; int endIndex = 0;
		while (matcher.find()) {
		  endIndex = matcher.start();
		  if (endIndex != startIndex) {
		    out.append(pattern, startIndex, endIndex);
		  }
		  String key = matcher.group();
		  key = key.substring(2, key.length() - 1).trim();
		  if (!loadedKeys.contains(key)) {
		    String value =  FileUtils.getPicURL(key,urlPrefix);
		    if (value != null) {
		      Set set = new HashSet(loadedKeys);
		      set.add(key);
		      format(out, value, set, urlPrefix);
		    }
		  }
		
		  startIndex = matcher.end();
		}
		endIndex = pattern.length();
		if (startIndex < endIndex)
		  out.append(pattern, startIndex, endIndex);
	}

	//格式化金额(每3位用逗号隔开)
	public static String outputdollars(BigDecimal num) {
		String number=num.setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString();

		if (number.length() <= 3)
			return number;
		else {
			int mod = number.length() % 3;
			String output = (mod == 0 ? "" : (number.substring(0, mod)));
			for (int i = 0; i < Math.floor(number.length() / 3); i++) {
				if ((mod == 0) && (i == 0))
					output += number.substring(mod + 3 * i, mod + 3 * i + 3);
				else
					output += ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);
			}
			return (output);
		}
	}
	
	public static void  main(String[] args) {
		
		try {
			System.out.println(format("<img src=\"${7df-b-d-1-16b.png}\" alt=\"\" />11<img src=\"${7df-b-d-1-16c.jpg}\" alt=\"\" />","http:www.baidu.com"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
