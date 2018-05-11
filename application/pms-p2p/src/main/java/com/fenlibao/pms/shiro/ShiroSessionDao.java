package com.fenlibao.pms.shiro;

import com.fenlibao.common.pms.util.constant.RedisConst;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import com.fenlibao.common.pms.util.tool.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lullaby on 2015-11-16 18:42
 */
public class ShiroSessionDao extends CachingSessionDAO {

    private static final Logger logger = LogManager.getLogger(ShiroSessionDao.class.getName());

    private static final String AUTHENTICATED_SESSION_KEY = "org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY";

    @Override
    protected Serializable doCreate(Session session) {
//        System.out.println("------------- doCreate -------------");
        logger.debug("-> Create session with ID [{}]", session.getId());
        Serializable sId = this.generateSessionId(session);
        String sessionId = sId.toString().replaceAll("-", "");
        this.assignSessionId(session, sessionId);
        String key = SerializationUtils.sessionKey(RedisConst.$SESSION_PREFIX, session);
        String value = SerializationUtils.sessionToString(session);
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.setex(key, RedisConst.$SESSION_TIMEOUT, value);
        }
        return sessionId;
    }

    @Override
    public Session readSession(Serializable serializable) throws UnknownSessionException {
//        System.out.println("------------- readSession -------------");
        Session session = getCachedSession(serializable);
        if (session == null ||
                (session.getAttribute(AUTHENTICATED_SESSION_KEY) != null &&
                        !(Boolean) session.getAttribute(AUTHENTICATED_SESSION_KEY))) {
            session = doReadSession(serializable);
            if (session == null) {
                throw new UnknownSessionException("There is no session with id [" + serializable + "].");
            }
            return session;
        }
        return session;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
//        System.out.println("------------- doReadSession -------------");
        logger.debug("-> Read session with ID [{}].", serializable);
        try (Jedis jedis = RedisFactory.getResource()) {
            String value = jedis.get(SerializationUtils.sessionKey(RedisConst.$SESSION_PREFIX, serializable));
            if (value != null) {
                Session session = SerializationUtils.sessionFromString(value);
                super.cache(session, session.getId());
                return session;
            }
        }
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
//        System.out.println("------------- doUpdate -------------");
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            logger.debug("-> Invalid session.");
            return;
        }
        logger.debug("-> Update session with ID [{}]", session.getId());
        String key = SerializationUtils.sessionKey(RedisConst.$SESSION_PREFIX, session);
        String value = SerializationUtils.sessionToString(session);
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.setex(key, RedisConst.$SESSION_TIMEOUT, value);
        }
    }

    @Override
    protected void doDelete(Session session) {
//        System.out.println("------------- doDelete -------------");
        logger.debug("-> Delete session with ID [{}].", session.getId());
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.del(SerializationUtils.sessionKey(RedisConst.$SESSION_PREFIX, session));
            jedis.publish(RedisConst.$SESSION_DELETE_CHANNEL, SerializationUtils.sessionIdToString(session));
        }
    }

    public Collection<Session> getActiveSessions() {
//        System.out.println("------------- getActiveSessions -------------");
        logger.debug("-> Get active sessions.");
        List<Session> sessions = new LinkedList<>();
        try (Jedis jedis = RedisFactory.getResource()) {
            Set<String> keys = jedis.keys(RedisConst.$SESSION_PREFIX + "*");
            Collection<String> values = jedis.mget(keys.toArray(new String[keys.size()]));
            for (String value : values) {
                sessions.add(SerializationUtils.sessionFromString(value));
            }
        }
        return sessions;
    }

}
