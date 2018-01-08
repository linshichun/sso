package win.scolia.cloud.sso.util.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserRoleCacheUtils extends BaseCacheUtils<Set<String>> {

    private static final String USER_ROLE_PREFIX = "USER_ROLE";

    private static Logger LOGGER = LoggerFactory.getLogger(UserRoleCacheUtils.class);

    @Autowired
    private RedisTemplate<String, Set<String>> redisTemplate;

    @Override
    protected String getSelfPrefix() {
        return USER_ROLE_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected RedisTemplate<String, Set<String>> getRedisTemplate() {
        return redisTemplate;
    }
}
