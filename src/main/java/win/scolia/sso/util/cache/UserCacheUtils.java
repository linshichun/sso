package win.scolia.sso.util.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.scolia.sso.bean.entity.User;

/**
 * 用户的缓存工具
 */
@Component
public class UserCacheUtils extends BaseCacheUtils<User> {

    private static final String USER_PREFIX = "USER";

    private static Logger LOGGER = LoggerFactory.getLogger(UserCacheUtils.class);

    @Autowired
    private RedisTemplate<String, User> redisTemplate;


    @Override
    protected String getSelfPrefix() {
        return USER_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected RedisTemplate<String, User> getRedisTemplate() {
        return redisTemplate;
    }


}
