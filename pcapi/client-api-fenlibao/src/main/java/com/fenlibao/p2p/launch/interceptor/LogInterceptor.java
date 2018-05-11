package com.fenlibao.p2p.launch.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dimeng.util.parser.BigDecimalParser;
import com.fenlibao.lianpay.v_1_0.utils.YinTongUtil;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserLog;
import com.fenlibao.p2p.model.entity.creditassignment.TransferOutInfo;
import com.fenlibao.p2p.model.enums.LogUrlEnum;
import com.fenlibao.p2p.model.global.SessionKeyEnum;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import com.fenlibao.p2p.service.user.UserLogService;
import com.fenlibao.p2p.util.CommonTool;
import com.google.gson.Gson;
import org.apache.catalina.connector.CoyoteOutputStream;
import org.apache.catalina.connector.OutputBuffer;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.ByteChunk;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.xbill.DNS.AAAARecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2018/3/1.
 */
public class LogInterceptor implements HandlerInterceptor {

    public String[] logUrls;

    public void setLogUrls(String[] logUrls) {
        this.logUrls = logUrls;
    }

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserLogService userLogService;

    @Resource
    TransferInService transferInService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        // 在已经将信息写出后才进行插入头信息日志操作
        // 第二个原因是需要解析请求参数中用户标识才将日志写入,拦截器不获取解析参数
        String wholeUrl = request.getRequestURI();
        //logger.error("请求地址"+requestUrl);
        /*if(!Arrays.asList(logUrls).contains(requestUrl)){
            return  true;
        }*/
        String urlArray[] = wholeUrl.split("/");
        String requestUrl = null;
        StringBuffer sb = new StringBuffer("");
        if(!wholeUrl.equals("/")&&!StringUtils.isEmpty(urlArray.toString())){
            requestUrl =sb.append("/").append(urlArray[urlArray.length-2]).append("/").append(urlArray[urlArray.length-1]).toString();
        }
        for (String url : logUrls) {
            if (url.equals(requestUrl)) {
                Object object = request.getSession().getAttribute(SessionKeyEnum.LOG_KEY.getKey());

                if (object == null) {
                    //logger.info("请求日志不存在");
                    return;
                }
                if (object instanceof UserLog) {
                    UserLog log = (UserLog) object;
                    //log.time4 = System.currentTimeMillis();
                    int status = 0;

                    CoyoteOutputStream os = (CoyoteOutputStream) response.getOutputStream();
                    // 取到流对象对应的Class对象
                    Class<CoyoteOutputStream> c = CoyoteOutputStream.class;
                    // 取出流对象中的OutputBuffer对象，该对象记录响应到客户端的内容
                    Field fs = c.getDeclaredField("ob");
                    if (fs.getType().toString().endsWith("OutputBuffer")) {
                        fs.setAccessible(true);// 设置访问ob属性的权限
                        OutputBuffer ob = (OutputBuffer) fs.get(os);// 取出ob
                        Class<OutputBuffer> cc = OutputBuffer.class;
                        Field ff = cc.getDeclaredField("outputChunk");// 取到OutputBuffer中的输出流
                        ff.setAccessible(true);
                        if (ff.getType().toString().endsWith("ByteChunk")) {
                            ByteChunk bc = (ByteChunk) ff.get(ob);// 取到byte流
                            String val = new String(bc.getBytes(), "UTF-8").trim();// 最终的值
                            String[] ary = val.split(",");//调用API方法按照逗号分隔字符串
                            String a = ary[0].concat("}");
                            Map<String, Object> map = new HashMap<String, Object>();
                            Gson gson = new Gson();
                            map = gson.fromJson(a, map.getClass());
                            status = Integer.valueOf((String) map.get("code"));
                        }
                    }

                    if (status==200) {
                        log.setStatus(1);
                    } else {
                        log.setStatus(0);
                    }
                    userLogService.addUserLog(log);
                }
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String wholeUrl = request.getRequestURI();

        String urlArray[] = wholeUrl.split("/");
        String requestUrl = null;
        StringBuffer sb = new StringBuffer("");
        Map<String,String[]> map = request.getParameterMap();
        if(wholeUrl.equals("/")){
            return true;
        }
        if(!wholeUrl.equals("/")&&!StringUtils.isEmpty(urlArray.toString())){
            requestUrl =sb.append("/").append(urlArray[urlArray.length-2]).append("/").append(urlArray[urlArray.length-1]).toString();
        }
        if(!Arrays.asList(logUrls).contains(requestUrl)){
            return  true;
        }
        AES aesInstance = AES.getInstace();
        int contuct;//记录用户操作行为
        UserLog userLog = new UserLog();
        for (String url : logUrls) {
            if (url.equals(requestUrl)) {
                String userId = null;
                if(StringUtils.isEmpty(request.getParameter("userId"))) {
                    Map paramMap = new HashMap();
                    if(StringUtils.isEmpty(request.getParameter("phoneNum"))){
                        return  true;
                    }

                    String username =request.getParameter("username");
                    if(!StringUtils.isEmpty(username)){
                        username =  aesInstance.decrypt2(request.getParameter("username"));
                        paramMap.put("username", username);
                    }else {
                        paramMap.put("username", aesInstance.decrypt2(request.getParameter("phoneNum")));
                    }

                    // 根据手机号获取用户信息
                    UserInfo uInfo = userInfoService.getUserInfoByPhoneNumOrUsername(paramMap);

                    if(uInfo==null){
                        return  true;
                    }
                    userId = uInfo.getUserId();
                }else {
                    userId = request.getParameter("userId");
                }

                UserAccountInfoVO userAccountInfoVO = new UserAccountInfoVO();
                userAccountInfoVO = userInfoService.getUserAccountInfo(userId,"ZRR");
                //也可能是非自然人登录
                if(userAccountInfoVO==null){
                    userAccountInfoVO = userInfoService.getUserAccountInfo(userId,"FZRR");
                }
                userLog.setUserId(Integer.valueOf(userId));

                userLog.setIp( CommonTool.getIp(request));
                if(requestUrl.equals(LogUrlEnum.BIND_CARD.getCode())){
                    userLog.setRemarks(request.getParameter("bankCardNo"));
                }else if(requestUrl.equals(LogUrlEnum.CANCLE_CARD.getCode())){
                    List<BankCard> cards = userAccountInfoVO.getBankCards();
                    if(cards!=null && cards.size()>0){
                        userLog.setRemarks(cards.get(0).getBankNum());
                    }
                }

                if(map!=null){
                    JSONObject json =(JSONObject) JSONObject.toJSON(map);
                    userLog.setRequestStr(json.toString());
                }
                LogUrlEnum logUrlEnum = LogUrlEnum.parse(requestUrl);
                userLog.setConduct(logUrlEnum.getIndex());
                request.getSession().setAttribute(SessionKeyEnum.LOG_KEY.getKey(), userLog);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String ip = YinTongUtil.getIpAddr(httpServletRequest);
    }
}
