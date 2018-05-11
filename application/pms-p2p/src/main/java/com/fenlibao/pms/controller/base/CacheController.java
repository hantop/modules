package com.fenlibao.pms.controller.base;

import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

/**
 * Created by Lullaby on 2015-09-23 22:34
 */
@RestController
@RequestMapping("system/cache")
public class CacheController {

    @RequestMapping
    public ModelAndView cache() {
        ModelAndView view = new ModelAndView("system/cache/index");
        return view;
    }

    @RequestMapping(value = "test")
    public HttpResponse test(String key, String value) {
        HttpResponse response = new HttpResponse();
        try (Jedis jedis = RedisFactory.getPool().getResource()) {
            jedis.set(key, value);
        }
        return response;
    }

}
