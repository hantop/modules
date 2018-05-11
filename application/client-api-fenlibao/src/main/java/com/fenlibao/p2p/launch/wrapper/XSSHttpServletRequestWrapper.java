package com.fenlibao.p2p.launch.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Toby on 2016/5/20.
 */
public class XSSHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XSSHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getServletPath() {
        String value = super.getServletPath();
        return encodeXSS(value);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        return encodeValues(values);
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return encodeXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return encodeXSS(value);
    }

    @Override
    public Map getParameterMap() {
        Map params = super.getParameterMap();
        if (params == null) {
            return null;
        }
        Map encodeParams = new java.util.HashMap();
        Iterator it = params.keySet().iterator();
        while (it.hasNext()) {
            String paramName = (String) it.next();
            encodeParams.put(paramName, encodeValues((String[]) params.get(paramName)));
        }
        return encodeParams;
    }

    @Override
    public String getRequestURI() {
        return super.getRequestURI();
    }

    @Override
    public String getPathInfo() {
        return super.getPathInfo();
    }

    @Override
    public String getContextPath() {
        return super.getContextPath();
    }


    private String[] encodeValues(String[] values) {
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = encodeXSS(values[i]);
        }
        return encodedValues;
    }

    public String encodeXSS(String value) {
        String canonical = null;
        if (value != null) {
            canonical = Normalizer.normalize(value, Normalizer.Form.NFC);
            canonical = HtmlUtils.htmlEscape(canonical);
        }
        if (StringUtils.isNotBlank(canonical)) {
            return canonical.trim();
        }
        return canonical;
    }
}
