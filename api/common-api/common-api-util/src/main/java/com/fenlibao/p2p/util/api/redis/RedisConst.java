package com.fenlibao.p2p.util.api.redis;

/**
 * Redis内存数据相关常量
 * Created by Lullaby on 2015-10-22 17:51
 */
public interface RedisConst {

    // 用户登录态存储命名前缀(user:token:0be4f783cc1145868c2b726d02826b73)
    String $LOGIN_STATE = "user:token:";

    // 用户登录态的客户端类型命名前缀(user:client:12138:5)
    String $LOGIN_CLIENT = "user:client:";

    // 用户登录态token有效期(单位：秒)
    int $LOGIN_STATE_TIMEOUT = 60 * 60 * 48;
    
    //验证码的KEY有效期(单位：秒)
    int $VERIFY_CODE_KEY_TIMEOUT = 10 * 60;

    //发送短信验证码需要验证的KEY
    String $CAPTCHA_KEY = "captcha:key:";

    String $REQUEST_CACHE_KEY = "request:cache:key:";
    String $REQUEST_CACHE_KEY_USERID = "userid:"; //userId是唯一的可以用来防重复提交，一个用户不可能同时进行多个操作
    int $REQUEST_CACHE_TIMEOUT = 60;

    int $MQ_DATABASE_INDEX = 1;
    String $MQ_AUTO_TENDER_TOPIC = "mq:autotender:topic";

    String $SPECIAL_USER_TYPE_KEY = "user:special:type:";

    /**
     * 订单号计数器key有效时间（单位秒）
     */
    int COUNTER_TIMEOUT_ORDER_NO = 1;
    /**
     * 订单号计数器key有效时间（单位秒）
     */
    String COUNTER_KEY_ORDER_NO = "counter:orderno";
    /**
     * 流水号计数器key有效时间（单位秒）
     */
    int COUNTER_TIMEOUT_FLOW_NO = 1;
    /**
     * 流水号号计数器key有效时间（单位秒）
     */
    String COUNTER_KEY_FLOW_NO = "counter:flowno";
    // 保存用户device_token有效期(单位：秒)
    int $DEVICE_TOKEN_TIMEOUT = 60 * 60 * 48;
    //特殊账号的KEY有效期(单位：秒)
    int $SPECIAL_USER_TYPE_KEY_TIMEOUT = 10 * 60;
    int $SPECIAL_USER_TYPE_KEY_TIMEOUT2 = 60 * 60;
}
