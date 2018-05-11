package com.fenlibao.platform.model;

/**
 * Created by Lullaby on 2016/2/25.
 */
public interface RedisConst {
	
	int DEFAULT_PLATFORM_REDIS_INDEX = 10;

    /**
     * IP白名单前缀
     */
    String $LEGAL_IP_PREFIX = "client:ip:";

    int $LEGAL_IP_TIMEOUT = 43200;
    
    String $MERCHANT_SECRET_PREFIX = "merchant:secret:";
    
    int $MERCHANT_SECRET_TIMEOUT = 43200;

    String $REQUEST_CACHE_KEY = "request:cache:key:";
    
    int $REQUEST_CACHE_TIMEOUT = 30;
    
    /**
     * 第三方用户token过期时间
     */
    int $THIRD_PARTY_USER_TOKEN_TIMEOUT = 3600;
    
    /**
     *	商户ID前缀
     */
    String $MERCHANT_ID_PREFIX = "merchant:id:";
    
    String $MERCHANT_CHANNEL_CODE_PREFIX = "merchant:channelCode:";
    
    /**
     * 第三方用户token
     */
    String $THIRD_PARTY_USER_TOKEN_PREFIX = "tp:user:token:";

    /**
     * 第三方用户资源URI
     */
    String $THIRD_PARTY_USER_RESOURCE_URI_PREFIX = "tp:user:uri:";

    /**
     * 商户AES密钥前缀
     */
    String $MERCHANT_SECRET_AES_PREFIX = "merchant:secret:aes:";
}
