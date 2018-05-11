package com.fenlibao.pms.shiro.web;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * filter代理类
 */
public class ProxyFilter implements Filter {
    private String name;
    private Filter filter;
    private PathMatcher pathMatcher = new AntPathMatcher("/");
    private String urlPattern = "/**";//可匹配的路径
    private Map<String, String> map = Collections.EMPTY_MAP;
    private boolean enable = true;
    private List<String> excludes = new ArrayList<>();

    public void destroy() {
        if (enable) {
            filter.destroy();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        if (enable) {
            ProxyFilterConfig proxyFilterConfig = new ProxyFilterConfig(
                    config.getServletContext());
            proxyFilterConfig.setFilterName(name);
            proxyFilterConfig.setMap(map);
            filter.init(proxyFilterConfig);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (enable) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            String contextPath = req.getContextPath();
            String requestUri = req.getRequestURI();
            String path = requestUri.substring(contextPath.length());

            // 如果在黑名单中，直接略过
            if (isExcluded(path)) {
                chain.doFilter(request, response);
                return;
            }

            // 如果符合redirect规则，进行跳转
            if (this.shouldRedirect(path)) {
                res.sendRedirect(contextPath + path + "/");
                return;
            }

            // 如果都没问题，才会继续进行判断
            if (pathMatcher.match(urlPattern, path)) {
                filter.doFilter(request, response, chain);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 拦截直接进行redirect的路径
     * @param path
     * @return
     */
    private boolean shouldRedirect(String path) {
        return false;
    }

    /**
     * 排除不处理的路径
     * @param path
     * @return
     */
    protected boolean isExcluded(String path) {
        for (String exclude : excludes) {
            if (pathMatcher.match(exclude, path)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setExcludePatterns(List<String> excludePatterns) {
        excludes.addAll(excludePatterns);
    }
}
