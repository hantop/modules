package com.fenlibao.platform.common.config;

import org.aeonbits.owner.Config;

/**
 * Created by Lullaby on 2016/2/24.
 */
@Config.Sources("classpath:system.properties")
public interface SystemConfig extends Config {

    @Key("server.port")
    @DefaultValue("8016")
    int port();

    @Key("context.path")
    String contextPath();

    @Key("datasource.environment")
    @DefaultValue("default")
    String datasourceEnvironment();

}
