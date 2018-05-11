package com.fenlibao.p2p.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HTTP请求
 *
 */
public class HttpUtil {

	private static String DEF_CHARSET = "UTF-8";
	
	public static String httpget(final String urlstr) {
        final StringBuilder sb = new StringBuilder();
        try {
            final URL url = new URL(urlstr);
            final HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            final BufferedReader reader =
                new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"GBK"));
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
	
	public static String httpget(final String urlstr, final Map<String, Object> params) {
        final StringBuilder sb = new StringBuilder();
        try {
            final URL url = new URL(urlstr + "?" + createParams(params));
            final HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            final BufferedReader reader =
                new BufferedReader(new InputStreamReader(urlConn.getInputStream(), DEF_CHARSET));
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
	
	
	public static String httppost(final String urlstr, final Map<String, Object> params, final Map<String, Byte[]> data) {
        final StringBuilder sb = new StringBuilder();
        try {
            final URL url = new URL(urlstr);
            final HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            if (data != null && !data.isEmpty()) {
                urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=HXHttppost");
            } else {
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            urlConn.setRequestMethod("POST");
            urlConn.connect();
            final DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            if (params != null) {
                String content = createParams(params);
                out.writeBytes(content);
            }
            if (data != null && !data.isEmpty()) {
                for (String key : data.keySet()) {
                    String content = key + "=" + data.get(key);
                    out.writeBytes(content);
                }
            }
            out.flush();
            out.close(); // flush and close
            
            final BufferedReader reader =
                new BufferedReader(new InputStreamReader(urlConn.getInputStream(), DEF_CHARSET));
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
	public static String createParams(final Map<String, Object> params) {
        final StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            int cnt = 0;
            for (String key : params.keySet()) {
                final Object val = params.get(key);
                sb.append(url_encode(key));
                sb.append("=");
                if (val != null && !val.toString().isEmpty())
                    sb.append(url_encode(val.toString()));
                if (cnt++ != params.keySet().size() - 1) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }
	
	public static String url_encode(final String text) {
        String res = null;
        try {
            if (text != null) {
                res = URLEncoder.encode(text, DEF_CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
}
