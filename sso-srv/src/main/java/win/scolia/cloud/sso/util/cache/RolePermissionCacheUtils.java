package win.scolia.cloud.sso.util.cache;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class RolePermissionCacheUtils extends BaseCacheUtils<Set<String>> {

    private static final String ROLE_PERMISSION_PREFIX = "ROLE_PERMISSION";

    @Autowired
    private RedisTemplate<String, Set<String>> redisTemplate;

    @Override
    protected String getSelfPrefix() {
        return ROLE_PERMISSION_PREFIX;
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
