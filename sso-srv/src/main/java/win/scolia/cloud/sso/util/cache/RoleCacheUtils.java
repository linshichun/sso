package win.scolia.cloud.sso.util.cache;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.bean.entity.Role;

@Slf4j
@Component
public class RoleCacheUtils extends BaseCacheUtils<Role> {

    private static final String ROLE_PREFIX = "ROLE";

    @Autowired
    private RedisTemplate<String, Role> redisTemplate;

    @Override
    protected String getSelfPrefix() {
        return ROLE_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    protected RedisTemplate<String, Role> getRedisTemplate() {
        return redisTemplate;
    }
}
