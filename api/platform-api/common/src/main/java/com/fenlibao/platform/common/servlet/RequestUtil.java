package com.fenlibao.platform.common.servlet;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lullaby on 2015/7/9.
 */
public class RequestUtil {

    public static Map<String, String> getAllParameters(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paramName = String.valueOf(enums.nextElement());
            String paramValue = request.getParameter(paramName);
            map.put(paramName, paramValue);
        }
        return map;
    }

    public static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<String, String>();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paramName = enums.nextElement();
            String paramValue = request.getParameter(paramName);
            if (StringUtils.isNotEmpty(paramValue)) {
                parameters.put(paramName, paramValue);
            }
        }
        return parameters;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String xmlhttprequest = request.getHeader("X-Requested-With");
        if (StringUtils.isNotEmpty(xmlhttprequest)
                && "XMLHttpRequest".equalsIgnoreCase(xmlhttprequest)) {
            return true;
        }
        return false;
    }

}
