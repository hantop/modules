package com.fenlibao.p2p.launch.interceptor;

import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.channel.PaymentChannelService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户添加系统的设置参数
 * @author Mingway.Xu
 * @date 2017/3/17 14:46
 */
public class BaseSetInterceptor implements HandlerInterceptor {
    @Resource
    private PaymentChannelService paymentChannelService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {

        Map resultMap = paymentChannelService.getBaseChannel();
        int cgMode = (int)resultMap.get("CGMode");
        int paymentCode = (int)resultMap.get("TPPaymentChannelCode");
        String url = request.getRequestURI();
        if (2 != cgMode && url.contains("hx")) {
            HttpResponse resp = new HttpResponse();
            resp.setCodeMessage(ResponseCode.PAYMENT_HUAXING_CANNOT_FIND);
            ResponseUtil.response(Jackson.getBaseJsonData(resp), response);
            return false;
        }
        if ((102 == paymentCode && url.contains("lianlianPay"))
                || (101 == paymentCode && url.contains("payment/baofoo"))) {
            HttpResponse resp = new HttpResponse();
            resp.setCodeMessage(ResponseCode.PAYMENT_CHANNEL_CANNOT_FIND);
            ResponseUtil.response(Jackson.getBaseJsonData(resp), response);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mv) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception ex) throws Exception {

    }
}
