package com.fenlibao.p2p.launch.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;

/**
 * 版本控制
 * @author yangzengcai
 * @date 2016年3月5日
 */
public class VersionControllerFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(VersionControllerFilter.class); 
	
	private static final List<String> VERSIONS = new ArrayList<>(3);
	private static final String VERSION_PREFIX = "Version=";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		VERSIONS.add(APIVersion.V_1_0_0);
		VERSIONS.add(APIVersion.V_1_1_0);
		logger.info("VersionControllerFilter init.......");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String version = req.getHeader("version");
		if (StringUtils.isNotBlank(version)
				&& VERSIONS.contains(VERSION_PREFIX.concat(version))) {
			HttpResponse data = new HttpResponse();
			data.setCodeMessage(ResponseCode.COMMON_VERSION_TOO_OLD);
			ResponseUtil.response(Jackson.getBaseJsonData(data), resp);
			return;
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		logger.info("VersionControllerFilter destroy.......");
	}

}
