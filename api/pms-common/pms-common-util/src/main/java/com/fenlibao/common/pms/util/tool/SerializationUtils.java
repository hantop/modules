package com.fenlibao.common.pms.util.tool;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;

import java.io.Serializable;

/**
 * Created by Lullaby on 2015-11-17 15:19
 */
public class SerializationUtils {

    public static String sessionKey(String prefix, Serializable sessionId) {
        return prefix + sessionId;
    }

    public static String sessionKey(String prefix, Session session) {
        return prefix + session.getId();
    }

    public static String sessionIdToString(Session session) {
        byte[] content = org.apache.commons.lang3.SerializationUtils.serialize(session.getId());
        return Base64.encodeToString(content);
    }

    public static Serializable sessionIdFromString(String value) {
        byte[] content = org.apache.shiro.codec.Base64.decode(value);
        return org.apache.commons.lang3.SerializationUtils.deserialize(content);
    }

    public static Session sessionFromString(String value) {
        byte[] content = Base64.decode(value);
        return org.apache.commons.lang3.SerializationUtils.deserialize(content);
    }

    public static String sessionToString(Session session) {
        byte[] content = org.apache.commons.lang3.SerializationUtils.serialize((SimpleSession) session);
        return Base64.encodeToString(content);
    }

}
