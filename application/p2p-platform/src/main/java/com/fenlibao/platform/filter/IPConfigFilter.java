package com.fenlibao.platform.filter;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.common.util.IPUtil;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.service.CommonService;
import com.google.inject.Inject;

/**
 * Created by Lullaby on 2016/2/2.
 */
@Singleton
public class IPConfigFilter extends AbstractFilter implements Filter {

    @Inject
    private CommonService commonService;

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("IPConfigFilter initialized.");
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ip = IPUtil.getIPAddress(request);
        String appid = request.getParameter("appid");
        if (!validateThirdParty(request, response, filterChain)) {
        	if (StringUtils.isNoneBlank(ip, appid)) {
        		if (commonService.isIPAuthorized(ip, appid)) {
        			filterChain.doFilter(request, response);
        		} else {
        			logger.warn("appid>>[{}], ip>>[{}] => ip非法请求", appid, ip);
        			writeResponse(Response.SYSTEM_REQUEST_IP_ILLEGAL, response);
        		}
        	} else {
        		logger.warn("ip>>[{}],appid>>[{}]", ip, appid);
        		writeResponse(Response.SYSTEM_EMPTY_PARAMETERS, response);
        	}
        }
    }

    public void destroy() {
        logger.warn("IPConfigFilter destroyed.");
    }

}
