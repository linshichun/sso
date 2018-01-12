package win.scolia.cloud.sso.util.cache;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class UserRoleCacheUtils extends BaseCacheUtils<Set<String>> {

    private static final String USER_ROLE_PREFIX = "USER_ROLE";

    @Autowired
    private RedisTemplate<String, Set<String>> redisTemplate;

    @Override
    protected String getSelfPrefix() {
        return USER_ROLE_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    protected RedisTemplate<String, Set<String>> getRedisTemplate() {
        return redisTemplate;
    }
}
