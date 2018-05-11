package com.fenlibao.p2p.common.util.database;

/**
 * Created by Lullaby on 2015/8/5.
 */
public enum DruidConfig {

    MYSQL_DRIVER("com.mysql.jdbc.Driver"),
    MYSQL_URL("jdbc:mysql://192.168.27.239:3306?characterEncoding=UTF-8&useUnicode=true&zeroDateTimeBehavior=convertToNull"),
    MYSQL_USERNAME("develop"),
    MYSQL_PASSWORD("flb.123"),
    DRUID_INITIALSIZE("10"),
    DRUID_MINIDLE("10"),
    DRUID_MAXACTIVE("30"),
    DRUID_MAXWAIT("60000"),
    DRUID_TIMEBETWEENEVICTIONRUNSMILLIS("60000"),
    DRUID_MINEVICTABLEIDLETIMEMILLIS("300000"),
    DRUID_VALIDATIONQUERY("SELECT 'x'"),
    DRUID_TESTWHILEIDLE("true"),
    DRUID_TESTONBORROW("false"),
    DRUID_TESTONRETURN("false"),
    DRUID_POOLPREPAREDSTATEMENTS("false"),
    DRUID_MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE("20"),
    DRUID_FILTERS("wall,stat"),
    DRUID_REMOVEABANDONED("true"),
    DRUID_REMOVEABANDONEDTIMEOUT("1800"),
    DRUID_LOGABANDONED("true");

    private String value;

    DruidConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
