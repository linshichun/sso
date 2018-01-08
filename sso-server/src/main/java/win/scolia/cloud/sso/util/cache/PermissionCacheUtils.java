package win.scolia.cloud.sso.util.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.bean.entity.Permission;

@Component
public class PermissionCacheUtils extends BaseCacheUtils<Permission> {

    private static final String PERMISSION_PREFIX = "PERMISSION";

    private static Logger LOGGER = LoggerFactory.getLogger(PermissionCacheUtils.class);

    @Autowired
    private RedisTemplate<String, Permission> redisTemplate;

    @Override
    protected String getSelfPrefix() {
        return PERMISSION_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected RedisTemplate<String, Permission> getRedisTemplate() {
        return redisTemplate;
    }
}
