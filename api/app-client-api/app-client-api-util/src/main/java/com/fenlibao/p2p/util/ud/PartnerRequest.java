package com.fenlibao.p2p.util.ud;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 有盾实名认证工具类，直接使用
 * @date 2017/04/27
 */
public class PartnerRequest {
    public static String apiCall(String url, String pubkey, String secretkey,
                                 String serviceCode,String outOrderId, Map<String, String> parameter) throws Exception {
        if (parameter == null || parameter.isEmpty())
            throw new Exception("error ! the parameter Map can't be null.");
        StringBuffer bodySb = new StringBuffer("{");
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            bodySb.append("'").append(entry.getKey()).append("':'").append(entry.getValue()).append("',");
        }
        String bodyStr = bodySb.substring(0, bodySb.length() - 1) + "}";
        String signature = encodeByMD5(bodyStr + "|" + secretkey);
        String urlStr = String.format(url, pubkey, serviceCode,
                outOrderId, signature);
        HttpResponse r = makePostRequest(urlStr, bodyStr);
        return EntityUtils.toString(r.getEntity());
    }

    private static final CloseableHttpClient client =
            HttpClientBuilder.create().build();

    private static HttpResponse makePostRequest(String uri, String jsonData)
            throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(jsonData, "UTF-8"));
        httpPost.setHeader("Accept", "application/json");

        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        return client.execute(httpPost);
    }

//    private static String md5(String data) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(data.toString().getBytes());
//        return bytesToHex(md.digest());
//    }

//    private static String bytesToHex(byte[] ch) {
//        StringBuffer ret = new StringBuffer("");
//        for (int i = 0; i < ch.length; i++)
//            ret.append(byteToHex(ch[i]));
//        return ret.toString();
//    }

    /**
     * 字节转换为16进制字符串
     */
//    private static String byteToHex(byte ch) {
//        String str[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B",
//                "C", "D", "E", "F"};
//        return str[ch >> 4 & 0xF] + str[ch & 0xF];
//    }

    /**
     *
     * 将指定的字符串用MD5加密
     * originstr 需要加密的字符串
     * @param originstr
     * @return
     */
    private static String encodeByMD5(String originstr) {
        String result = null;
        char hexDigits[] = {//用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        if(originstr != null){
            try {
                //返回实现指定摘要算法的 MessageDigest 对象
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用utf-8编码将originstr字符串编码并保存到source字节数组
                byte[] source = originstr.getBytes("utf-8");
                //使用指定的 byte 数组更新摘要
                md.update(source);
                //通过执行诸如填充之类的最终操作完成哈希计算，结果是一个128位的长整数
                byte[] tmp = md.digest();
                //用16进制数表示需要32位
                char[] str = new char[32];
                for(int i=0,j=0; i < 16; i++){
                    //j表示转换结果中对应的字符位置
                    //从第一个字节开始，对 MD5 的每一个字节
                    //转换成 16 进制字符
                    byte b = tmp[i];
                    //取字节中高 4 位的数字转换
                    //无符号右移运算符>>> ，它总是在左边补0
                    //0x代表它后面的是十六进制的数字. f转换成十进制就是15
                    str[j++] = hexDigits[b>>>4 & 0xf];
                    // 取字节中低 4 位的数字转换
                    str[j++] = hexDigits[b&0xf];
                }
                result = new String(str);//结果转换成字符串用于返回
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
