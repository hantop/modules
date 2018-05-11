package com.fenlibao.common.pms.util.constant;

/**
 * Created by Lullaby on 2015-11-18 11:32
 */
public interface RedisConst {

    String $SESSION_PREFIX = "pms:session:";//会话key前缀

    String $SESSION_DELETE_CHANNEL = "pms:session:delete";//移除方式

    int $SESSION_TIMEOUT = 3600;//sessioni key timeout

    String $PMS_SPECIAL_USER_TYPE_KEY = "pms:user:special:type:";

    int $SPECIAL_USER_TYPE_KEY_TIMEOUT = 10 * 60;
    int $SPECIAL_USER_TYPE_KEY_TIMEOUT2 = 60 * 60;
}
