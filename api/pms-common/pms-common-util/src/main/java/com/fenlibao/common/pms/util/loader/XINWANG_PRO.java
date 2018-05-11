package com.fenlibao.common.pms.util.loader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Lullaby on 2015/7/20.
 */
public class XINWANG_PRO {

    private static final String FILE_CONFIG_PROPERTIES = "xinwang.properties";

    private static Properties props = new Properties();

    public static void loadProperties() {
        InputStreamReader input = null;
        try {
            input = new InputStreamReader(
                    XINWANG_PRO.class.getClassLoader().getResourceAsStream(FILE_CONFIG_PROPERTIES), "UTF-8");
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
