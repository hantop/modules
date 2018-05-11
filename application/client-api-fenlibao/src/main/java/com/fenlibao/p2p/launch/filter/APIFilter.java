package com.fenlibao.p2p.launch.filter;

import com.fenlibao.p2p.common.util.encrypt.AESCoder;
import com.fenlibao.p2p.common.util.http.ParameterRequestWrapper;
import com.fenlibao.p2p.common.util.http.RequestUtil;
import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * APIFilter
 * 
 * @author kop_still
 *
 */
public class APIFilter implements Filter {
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpResponse result = new HttpResponse();
		try {
			boolean flag = Boolean.parseBoolean(Config.get("interface.filter.enabled"));
			String requestURI = request.getRequestURI();
			String suffix = Config.get("interface.suffix");
			String method;
			if (StringUtils.isNotEmpty(suffix)) {
				method = requestURI.substring(requestURI.lastIndexOf("/") + 1, requestURI.lastIndexOf("."));
			} else {
				method = requestURI.substring(requestURI.lastIndexOf("/") + 1, requestURI.length());
			}
			if (flag) {
				String ticket = Config.get("interface.ticket.name");
				String openticket = request.getParameter(ticket);
				if (StringUtils.isEmpty(openticket)) {
					result.setCodeMessage(ResponseCode.FAILURE);
					ResponseUtil.response(Jackson.getBaseJsonData(result), response);
				} else {
					String salt = Config.get("interface.static.salt");
					String right = AESCoder.parseByte2HexStr(AESCoder.encrypt(method, salt));
					if (openticket.equals(right)) {
						boolean mark = Boolean.parseBoolean(Config.get("interface.parameter.encryption"));
						if (mark) {
							Map<String, String> params = RequestUtil.getAllParameters(request);
							try {
								Map<String, Object> extend = new HashMap<>();
								for (Map.Entry<String, String> entry : params.entrySet()) {
									String key = entry.getKey();
									String value = entry.getValue();
									if (ticket.equals(key)) {
										continue;
									}
									if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
										extend.put(entry.getKey(),
												new String(AESCoder.decrypt(AESCoder.parseHexStr2Byte(value), salt)));
									}
								}
								ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request, extend);
								chain.doFilter(wrapper, response);
							} catch (Exception e) {
								e.printStackTrace();
								result.setCodeMessage(ResponseCode.FAILURE);
								ResponseUtil.response(Jackson.getBaseJsonData(result), response);
							}
						} else {
							chain.doFilter(request, response);
						}
					} else {
						result.setCodeMessage(ResponseCode.FAILURE);
						ResponseUtil.response(Jackson.getBaseJsonData(result), response);
					}
				}
			} else {
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setCodeMessage(ResponseCode.FAILURE);
			ResponseUtil.response(Jackson.getBaseJsonData(result), response);
		}
	}

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {

	}

}
