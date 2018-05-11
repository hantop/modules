package com.fenlibao.p2p.util.api.http;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.util.api.http.defines.HttpFormat;
import com.fenlibao.p2p.util.api.http.defines.HttpResult;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.*;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 * Created by lenovo on 2015/9/4.
 */
public final class HttpClientUtil {

    private static CloseableHttpClient httpClient;

    static {
        try {
            httpClient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore())
                    .setDefaultRequestConfig(defaultRequestConfig())
                    .setDefaultCredentialsProvider(credentialsProvider())
                    .setConnectionManager(connectionManager())
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static HttpClientConnectionManager connectionManager() throws NoSuchAlgorithmException {
        SSLContext sslcontext = SSLContext.getDefault();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();

        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory());

        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("localhost")) {
                    return new InetAddress[]{InetAddress.getByAddress(new byte[]{127, 0, 0, 1})};
                } else {
                    return super.resolve(host);
                }
            }
        };
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory, dnsResolver);
        connManager.setDefaultConnectionConfig(defaultConnectionConfig());
        connManager.setMaxTotal(100);
        return connManager;
    }

    private static ConnectionConfig defaultConnectionConfig() {
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        return connectionConfig;
    }

    private static HttpMessageParserFactory<HttpResponse> responseParserFactory() {
        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {
            @Override
            public HttpMessageParser<HttpResponse> create(
                    SessionInputBuffer buffer, MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {
                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }
                };
                return new DefaultHttpResponseParser(
                        buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {
                    @Override
                    protected boolean reject(final CharArrayBuffer line, int count) {
                        return false;
                    }
                };
            }
        };
        return responseParserFactory;
    }

    private static CookieStore cookieStore() {
        CookieStore cookieStore = new BasicCookieStore();
        return cookieStore;
    }

    private static RequestConfig defaultRequestConfig() {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(5000)
                .setCookieSpec(CookieSpecs.STANDARD)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        return defaultRequestConfig;
    }

    private static CredentialsProvider credentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        return credentialsProvider;
    }

    private HttpClientUtil() {

    }


    public static HttpResult httpsPost(String url, HttpFormat format, JSONObject params) {
        return httpsPost(url, format, params, null);
    }

    public static HttpResult httpsPost(String url, HttpFormat format, Header... headers) {
        return httpsPost(url, format, null, headers);
    }

    public static HttpResult httpsPost(String url, HttpFormat format) {
        return httpsPost(url, format, null, null);
    }

    public static HttpResult httpsJsonPost(String url) {
        return httpsPost(url, HttpFormat.JSON, null, null);
    }

    public static HttpResult httpsJsonPost(String url, JSONObject params) {
        return httpsPost(url, HttpFormat.JSON, params, null);
    }

    public static HttpResult httpsJsonPost(String url, Header... headers) {
        return httpsPost(url, HttpFormat.JSON, null, headers);
    }

    public static HttpResult httpsMapPost(String url) {
        return httpsPost(url, HttpFormat.MAP, null, null);
    }

    public static HttpResult httpsMapPost(String url, JSONObject params) {
        return httpsPost(url, HttpFormat.MAP, params, null);
    }

    public static HttpResult httpsMapPost(String url, JSONObject params,Header... headers) {
        return httpsPost(url, HttpFormat.MAP, params, headers);
    }

    public static HttpResult httpsMapPost(String url, Header... headers) {
        return httpsPost(url, HttpFormat.MAP, null, headers);
    }

    public static HttpResult httpsPost(String url, HttpFormat format, JSONObject params, Header... headers) {
        Assert.notNull(format, "提交数据格式不能为null");
        HttpResult result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null && !params.isEmpty() && format == HttpFormat.MAP) {
                List<NameValuePair> nvps = new ArrayList<>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            } else if (params != null && !params.isEmpty() && format == HttpFormat.JSON) {
                StringEntity entity = new StringEntity(params.toString(), Consts.UTF_8);//解决中文乱码问题
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            if (headers != null) {
                for (Header header : headers) {
                    httpPost.addHeader(header);
                }
            }
            result = httpClient.execute(httpPost, new CustomResponseHandler());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (result == null) {
                result = new HttpResult(-1);
                result.setMessage("网络异常");
            }
        }
        return result;
    }


    public static HttpResult httpsGet(String url, Map<String, Serializable> params) {
        return httpsGet(url, params, null);
    }

    public static HttpResult httpsGet(String url) {
        return httpsGet(url, null, null);
    }

    public static HttpResult httpsGet(String url, Header... headers) {
        return httpsGet(url, null, headers);
    }

    public static HttpResult httpsGet(String url, Map<String, Serializable> params, Header... headers) {
        HttpResult result = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getKey());
                }
            }
            HttpGet httpGet = new HttpGet(builder.build());
            if (headers != null) {
                for (Header header : headers) {
                    httpGet.addHeader(header);
                }
            }
            result = httpClient.execute(httpGet, new CustomResponseHandler());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (result == null) {
                result = new HttpResult(-1);
                result.setMessage("网络异常");
            }
        }
        return result;
    }
}
