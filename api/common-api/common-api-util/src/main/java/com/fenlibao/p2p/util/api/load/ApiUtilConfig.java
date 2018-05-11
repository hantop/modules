package com.fenlibao.p2p.util.api.load;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author zeronx on 2017/12/21 13:45.
 * @version 1.0
 */
public class ApiUtilConfig {

    private static final String CONFIG_PROPERTIES = "config.properties";

    private static Properties props = new Properties();

    public static void loadUtilConfig() {
        ApiUtilConfig config = new ApiUtilConfig();
        config.loadProperties();
    }

    private void loadProperties() {
        InputStreamReader input = null;
        try {
            input = new InputStreamReader(
                    ApiUtilConfig.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES), "UTF-8");
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static Object get(Object obj) {
        return props.get(obj);
    }

}
