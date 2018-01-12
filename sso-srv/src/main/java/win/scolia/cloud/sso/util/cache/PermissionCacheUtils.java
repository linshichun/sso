package win.scolia.cloud.sso.util.cache;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.bean.entity.Permission;

@Slf4j
@Component
public class PermissionCacheUtils extends BaseCacheUtils<Permission> {

    private static final String PERMISSION_PREFIX = "PERMISSION";

    @Autowired
    private RedisTemplate<String, Permission> redisTemplate;

    @Override
    protected String getSelfPrefix() {
        return PERMISSION_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    protected RedisTemplate<String, Permission> getRedisTemplate() {
        return redisTemplate;
    }
}
