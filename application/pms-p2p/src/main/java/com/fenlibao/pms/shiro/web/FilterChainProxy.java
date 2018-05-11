package com.fenlibao.pms.shiro.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * filterchainProxy 入口，将所有的filter交给spring管理
 */
public class FilterChainProxy implements Filter {

    private static final Logger log = LoggerFactory.getLogger(FilterChainProxy.class);
    private List<? extends Filter> filters = new ArrayList<>();

    public void setFilters(List<? extends Filter> filters) {
        this.filters = new ArrayList<>(filters);
    }

    public void destroy() {
        for (int i = filters.size(); i-- > 0;) {
            Filter filter = filters.get(i);
            filter.destroy();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        for (Filter filter : filters) {
            filter.init(config);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        new VirtualFilterChain(chain, filters).doFilter(request, response);
    }

    private static final class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<? extends Filter> filters;
        private int index = 0;

        private VirtualFilterChain(FilterChain chain,
                List<? extends Filter> filters) {
            this.originalChain = chain;
            this.filters = filters;
        }

        public void doFilter(final ServletRequest request,
                final ServletResponse response) throws IOException,
                ServletException {
            if (this.filters == null || this.filters.size() == this.index) {
                log.trace("Invoking original filter chain.");
                originalChain.doFilter(request, response);
            } else {
                log.trace("Invoking wrapped filter at index [" + this.index + "]");
                this.filters.get(this.index++).doFilter(request, response, this);
            }
        }
    }
}
