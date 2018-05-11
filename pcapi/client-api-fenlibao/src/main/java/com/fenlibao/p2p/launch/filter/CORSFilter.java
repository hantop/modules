package com.fenlibao.p2p.launch.filter;

import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持现代浏览器允许的CORS(Cross-Origin Resource Sharing)跨域请求过滤器
 * 不支持不允许Access-Control-Allow-Origin:*
 *
 * Created by Lullaby on 2015-11-13 14:19
 */
public class CORSFilter implements Filter {

    private static List<String> CORS_ORIGIN = new ArrayList<>();
    private static String CORS_METHODS;
    private static String CORS_HEADERS;
    private static String CORS_MAXAGE;

    private static final String ACCESS_ORIGIN = "Access-Control-Allow-Origin";
    private static final String ACCESS_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_MAXAGE = "Access-Control-Max-Age";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String origin = Config.get("cors.origin");
        if (StringUtils.isNotEmpty(origin)) {
            for (String item : origin.split(",")) {
                CORS_ORIGIN.add(item);
            }
        }
        CORS_METHODS = Config.get("cors.methods");
        CORS_HEADERS = Config.get("cors.headers");
        CORS_MAXAGE = Config.get("cors.maxage");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String requestOrigin = req.getHeader("Origin");
        
        if (!CORS_ORIGIN.isEmpty()){
            if (CORS_ORIGIN.contains(requestOrigin)) {
                res.setHeader(ACCESS_ORIGIN, requestOrigin);
            }
        }else{
        	res.setHeader(ACCESS_ORIGIN, "*");
        }
        if (StringUtils.isNotEmpty(CORS_METHODS)) {
            res.setHeader(ACCESS_METHODS, CORS_METHODS);
        }
        if (StringUtils.isNotEmpty(CORS_HEADERS)) {
            res.setHeader(ACCESS_HEADERS, CORS_HEADERS);
        }
        if (StringUtils.isNotEmpty(CORS_MAXAGE)) {
            res.setHeader(ACCESS_MAXAGE, CORS_MAXAGE);
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

}
