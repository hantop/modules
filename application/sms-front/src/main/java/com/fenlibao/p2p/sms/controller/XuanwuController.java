package com.fenlibao.p2p.sms.controller;

import com.fenlibao.p2p.sms.domain.GsmsResponse;
import com.fenlibao.p2p.sms.domain.MTPack;
import com.fenlibao.p2p.sms.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2015/8/24.
 */
@RestController
@RequestMapping("/sms")
public class XuanwuController {

    private static final Logger log = LoggerFactory.getLogger(XuanwuController.class);

    @Autowired
    private SmsService smsService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public
    @ResponseBody
    GsmsResponse send(@RequestBody final MTPack mtPack) {
        GsmsResponse response = this.smsService.sendMessage(mtPack);
        return response;
    }

}
