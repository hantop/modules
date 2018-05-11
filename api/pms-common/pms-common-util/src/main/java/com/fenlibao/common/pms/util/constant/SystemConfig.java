package com.fenlibao.common.pms.util.constant;

/**
 * 系统常量配置
 * Created by Lullaby on 2015-09-25 11:52
 */
public interface SystemConfig {

    String[] $SYSTEM_PROPERTIES = {"config.properties"};

    String $PREFIX_SYSTEM_CONFIG = "@pms.system.config.";

    String $COOKIE_SESSION_NAME = $PREFIX_SYSTEM_CONFIG + "cookie.session.name";

    String $COOKIE_VALID_PATH = $PREFIX_SYSTEM_CONFIG + "cookie.valid.path";

    String $RESOURCE_SERVER_PATH = $PREFIX_SYSTEM_CONFIG + "resource.server.path";

    String $CLIENT_STARTUP_IMAGE_PATH = $PREFIX_SYSTEM_CONFIG + "client.startup.image.path";

    String $CLIENT_ADVERT_IMAGE_PATH = $PREFIX_SYSTEM_CONFIG + "client.advert.image.path";

}
