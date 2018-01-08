package win.scolia.cloud.sso.util.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.bean.entity.Role;

@Component
public class RoleCacheUtils extends BaseCacheUtils<Role> {

    private static final String ROLE_PREFIX = "ROLE";

    private static Logger LOGGER = LoggerFactory.getLogger(UserCacheUtils.class);

    @Autowired
    private RedisTemplate<String, Role> redisTemplate;

    @Override
    protected String getSelfPrefix() {
        return ROLE_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected RedisTemplate<String, Role> getRedisTemplate() {
        return redisTemplate;
    }
}
