package com.fenlibao.p2p.sms.service;

import com.fenlibao.p2p.sms.config.annotation.PropMap;
import com.fenlibao.p2p.sms.domain.Config;
import com.fenlibao.p2p.sms.persistence.ConfigMapper;
import com.fenlibao.p2p.sms.config.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2015/9/16.
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    public <T> T selectByType(String type, Class<?> classType) {
        List<Config> configs = configMapper.selectByType(type);
        Object target = null;
        try {
            target = classType.newInstance();
            this.setMapValue(configs, target);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) target;
    }


    /**
     * 根据target属性的注解将
     *
     * @param configs
     * @param target
     */
    private Object setMapValue(List<Config> configs, Object target) {

        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            PropMap propMap = field.getAnnotation(PropMap.class);
            String targetKey = field.getName();
            if (propMap != null && !"".equals(propMap.value())) {
                targetKey = propMap.value();
            }
            for (Config config : configs) {
                String key = config.getKey();
                if (key.equals(targetKey)) {
                    Object value = config.getValue();
                    org.springframework.util.ReflectionUtils.makeAccessible(field);
                    Class clazz = field.getType();
                    if (clazz == Boolean.class) {
                        value = new Boolean(value.toString());
                    } else if (clazz == Integer.class || clazz == int.class) {
                        value = Integer.parseInt(value.toString());
                    }
                    org.springframework.util.ReflectionUtils.setField(field, target, value);
                    break;
                }
            }
        }
        Utils.validate(target);
        return target;
    }
}
