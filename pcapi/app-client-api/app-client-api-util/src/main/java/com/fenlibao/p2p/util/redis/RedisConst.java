package com.fenlibao.p2p.util.redis;

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
    
}
