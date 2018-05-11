package com.fenlibao.p2p.weixin.controller;

import com.fenlibao.p2p.weixin.crypt.AesException;
import com.fenlibao.p2p.weixin.service.IWxApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by Bogle on 2015/11/20.
 */
@RestController
@RequestMapping("/access")
public class AccessController {

    private static final Logger log = LoggerFactory.getLogger(AccessController.class);

    @Autowired
    protected IWxApi wxApi;

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public
    @ResponseBody
    void process(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr, OutputStream outputStream) throws IOException {
        if (!wxApi.checkSignature(signature, timestamp, nonce)) {
            echostr = "0";
        }
        outputStream.write(echostr.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public
    @ResponseBody
    void process(
            @RequestParam(value = "timestamp", required = false) String timestamp,
            @RequestParam(value = "nonce", required = false) String nonce,
            @RequestParam(value = "msg_signature", required = false) String msgSignature,
            @RequestParam(value = "encrypt_type", required = false) String encryptType,
            @RequestBody String requestbody,
            OutputStream outputStream) throws IOException {
        log.debug("微信交互消息");
        Serializable result = null;
        try {
            result = this.wxApi.process(timestamp,nonce,msgSignature,encryptType,requestbody);
        } catch (AesException e) {
            log.error(e.getMessage());
        }
        log.debug("服务器响应信息：{}", result);
        if (result != null)
            outputStream.write(result.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
