package com.fenlibao.p2p.weixin.controller;

import com.fenlibao.p2p.weixin.defines.APICode;
import com.fenlibao.p2p.weixin.exception.WeixinException;
import com.fenlibao.p2p.weixin.message.FenlibaoApi;
import com.fenlibao.p2p.weixin.message.template.TemplateMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2015/8/18.
 */
@RestController
@RequestMapping("/weixin")
public class WeixinController extends WeixinSuport {

    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);

    @RequestMapping(value = "/signature", method = RequestMethod.POST)
    public FenlibaoApi<Map<String, ?>> signature(@RequestParam(value = "url") String url, HttpServletResponse response) {
        log.debug("网页授权...");
        response.setHeader("Access-Control-Allow-Origin", "*");
        FenlibaoApi<Map<String, ?>> fenlibaoApi = new FenlibaoApi<>();
        fenlibaoApi.setCode(APICode.SUCCESS.getCode());
        fenlibaoApi.setMessage(APICode.SUCCESS.getErrmsg());
        fenlibaoApi.setData(wxApi.signature(url));
        return fenlibaoApi;
    }

    /**
     * 接收向指定用户发送模板消息的请求
     * @param templateMsgs
     * @return
     */
    @RequestMapping(value = "/sendTmplateMsg", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<TemplateMsg> sendTmplateMsg(@RequestBody List<TemplateMsg> templateMsgs) {
        try {
            Future<List<TemplateMsg>> futureResult = wxApi.sendTmplateMsg(templateMsgs);
            templateMsgs = futureResult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (WeixinException e) {
            log.error(e.getMessage());
        }
        return templateMsgs;
    }

    /**
     * 接收群发模板消息的请求
     * @param templateMsgs
     * @return
     */
    @RequestMapping(value = "/sendTmplateMsgToAllFans", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public FenlibaoApi sendTmplateMsgToAllFans(@RequestBody List<TemplateMsg> templateMsgs) {
        wxApi.sendTmplateMsgToAllFans(templateMsgs);
        return new FenlibaoApi(200, "submit ok");
    }

    @RequestMapping(value = "/redirectUrl")
    public ModelAndView redirectUrl(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
            @RequestParam("state") String state,
            HttpServletRequest request) {
        log.debug("授权url，并返回openid和eventkey");
        boolean flag = Boolean.parseBoolean(state);
        return parse(code, request, redirectUrl, state, flag);

    }
}
