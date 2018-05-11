package com.fenlibao.pms.launch.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.fenlibao.common.pms.util.http.YinTongUtil;
import com.fenlibao.model.pms.common.enums.LogUrlEnum;
import com.fenlibao.model.pms.common.global.OperateLog;
import com.fenlibao.model.pms.common.global.SessionKeyEnum;
import com.fenlibao.service.pms.idmt.log.PmsLogService;
import com.google.gson.Gson;
import org.apache.catalina.connector.CoyoteOutputStream;
import org.apache.catalina.connector.OutputBuffer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.buf.ByteChunk;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class LogInterceptor implements HandlerInterceptor {
    Logger logger = LogManager.getLogger(LogInterceptor.class);

    @Resource
    private PmsLogService pmsLogService;

    public String[] logUrls;

    public void setLogUrls(String[] logUrls) {
        this.logUrls = logUrls;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        Object object = request.getSession().getAttribute(SessionKeyEnum.LOG_KEY.getKey());
        if (object == null) {
            logger.info("请求日志不存在");
            return;
        }
        if (object instanceof OperateLog) {
            OperateLog operateLog = (OperateLog) object;
            int status = response.getStatus();
            if (status == 200) {
                operateLog.setStatus(1);
            } else {
                operateLog.setStatus(0);
            }

            if (operateLog.getOperation() == LogUrlEnum.DOREPAY.getIndex()){ //还款细分为还款 00 提前还款 10 担保代偿 01 提前担保代偿 11
                String isPreRepay = request.getParameter("isPreRepay");
                String isSubrogation = request.getParameter("isSubrogation");
                if ("true".equals(isPreRepay) && "false".equals(isSubrogation)){
                    operateLog.setOperation(LogUrlEnum.PREDOREPAY.getIndex());
                }else if ("false".equals(isPreRepay) && "true".equals(isSubrogation)){
                    operateLog.setOperation(LogUrlEnum.SUBROGATION.getIndex());
                }else if ("true".equals(isPreRepay) && "true".equals(isSubrogation)){
                    operateLog.setOperation(LogUrlEnum.PRESUBROGATION.getIndex());
                }
            }

            if (operateLog.getOperation() == LogUrlEnum.REPLACEMENTRECHARGEAUDIT.getIndex()){ //代充值审核
                String flag = request.getParameter("flag");
                if (!"1".equals(flag)){  //审核不通过
                    operateLog.setOperation(LogUrlEnum.REPLACEMENTRECHARGEAUDITNOTPASS.getIndex());
                }
            }

            if (operateLog.getOperation() == LogUrlEnum.ACTIVITYCREATE.getIndex()){ //活动编辑,区别新增或者编辑
                if ("POST".equals(request.getMethod())){
                    Map activityForm = request.getParameterMap();
                    String[] activityIdArr = (String[]) activityForm.get("id");
                    String activityIdString = activityIdArr[0];
                    int activityId = Integer.parseInt(activityIdString);
                    if (activityId > 0) {
                        operateLog.setOperation(LogUrlEnum.ACTIVITYEDIT.getIndex());
                    }
                }
            }

            if (operateLog.getOperation() == LogUrlEnum.UNBINDBANKCARD.getIndex()){ //区别解绑银行卡与解绑银行卡审核
                    String userRole = request.getParameter("userRole");
                    if (userRole != null && !"0".equals(userRole)) {
                        operateLog.setOperation(LogUrlEnum.UNBINDBANKCARDAUDIT.getIndex());
                    }
            }

            pmsLogService.addOperateLog(operateLog);
        }

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String requestUrl = request.getRequestURI();
        if (requestUrl == null){
            request.getSession().setAttribute(SessionKeyEnum.LOG_KEY.getKey(), null);
            return true;
        }

        OperateLog userLog = new OperateLog();
        String userName = request.getUserPrincipal().toString();
        userLog.setUsername(userName);
        userLog.setIp(YinTongUtil.getIpAddr(request));

        for (LogUrlEnum item : LogUrlEnum.values()) {
            if (requestUrl.contains(item.getCode())) {
                userLog.setOperation(item.getIndex());
                break;
            }
        }

        if (userLog.getOperation() == LogUrlEnum.ACTIVITYCREATE.getIndex()){ //活动编辑,去掉跳转页面操作
            if ("GET".equals(request.getMethod())){
                request.getSession().setAttribute(SessionKeyEnum.LOG_KEY.getKey(), null);
                return true;
            }
        }

        if (userLog.getOperation() == LogUrlEnum.REPLACEMENTRECHARGE.getIndex()){ //代充值,去掉跳转页面操作
            if ("GET".equals(request.getMethod())){
                request.getSession().setAttribute(SessionKeyEnum.LOG_KEY.getKey(), null);
                return true;
            }
        }

        if (userLog.getOperation() == null){
            request.getSession().setAttribute(SessionKeyEnum.LOG_KEY.getKey(), null);
            return true;
        }

        Map map = request.getParameterMap();
        if (map != null) {
            JSONObject json = (JSONObject) JSONObject.toJSON(map);
            userLog.setRequestMessage(json.toString());
        }

        request.getSession().setAttribute(SessionKeyEnum.LOG_KEY.getKey(), userLog);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
}
