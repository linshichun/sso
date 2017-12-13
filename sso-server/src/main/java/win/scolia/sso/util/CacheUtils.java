package win.scolia.sso.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.scolia.sso.api.bean.entity.User;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtils {

    @Value("${sso.cache.prefix}")
    private String prefix;

    @Value("${sso.cache.expire}")
    private Long expire;

    private static final String USER_PREFIX = "USER";

    private static final String ROLE_PREFIX = "ROLE";

    private static final String PERMISSION_PREFIX = "PERMISSION";

    private static Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);

    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    @Autowired
    private RedisTemplate<String, Set<String>> setRedisTemplate;

    public enum CacheType {
        ROLE, PERMISSIONS
    }

    /**
     * 缓存用户对象
     *
     * @param user 用户对象
     */
    public void cacheUser(User user) {
        String cacheKey = String.format("%s_%s_%s", prefix, USER_PREFIX, user.getUserName());
        userRedisTemplate.opsForValue().set(cacheKey, user, expire, TimeUnit.SECONDS);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Cache user: {}", user.getUserName());
        }
    }

    /**
     * 获取缓存的用户对象
     *
     * @param username 用户名
     * @return 缓存的用户对象
     */
    public User getUser(String username) {
        String cacheKey = String.format("%s_%s_%s", prefix, USER_PREFIX, username);
        BoundValueOperations<String, User> operations = userRedisTemplate.boundValueOps(cacheKey);
        User user = operations.get();
        operations.expire(expire, TimeUnit.SECONDS);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Hit cache user: {}", username);
        }
        return user;
    }

    /**
     * 缓存角色或权限信息
     *
     * @param name      用户名或角色名
     * @param content   缓存内容
     * @param cacheType 缓存的是角色还是权限信息
     */
    public void cacheRolesOrPermissions(String name, Set<String> content, CacheType cacheType) {
        String cacheKey;
        if (cacheType.equals(CacheType.ROLE)) {
            cacheKey = String.format("%s_%s_%s", prefix, ROLE_PREFIX, name);
            setRedisTemplate.opsForValue().set(cacheKey, content, expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache role: {}", name);
            }
        } else {
            cacheKey = String.format("%s_%s_%s", prefix, PERMISSION_PREFIX, name);
            setRedisTemplate.opsForValue().set(cacheKey, content, expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache permission: {}", name);
            }
        }
    }

    /**
     * 获取缓存的角色或权限信息
     *
     * @param name      用户名或角色名
     * @param cacheType 缓存的是角色还是权限信息
     * @return 缓存内容
     */
    public Set<String> getRolesOrPermissions(String name, CacheType cacheType) {
        String cacheKey;
        if (cacheType.equals(CacheType.ROLE)) {
            cacheKey = String.format("%s_%s_%s", prefix, ROLE_PREFIX, name);
            BoundValueOperations<String, Set<String>> operations = setRedisTemplate.boundValueOps(cacheKey);
            Set<String> content = operations.get();
            operations.expire(expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Hit cache role: {}", name);
            }
            return content;
        } else {
            cacheKey = String.format("%s_%s_%s", prefix, PERMISSION_PREFIX, name);
            BoundValueOperations<String, Set<String>> operations = setRedisTemplate.boundValueOps(cacheKey);
            Set<String> content = operations.get();
            operations.expire(expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Hit cache permission: {}", name);
            }
            return content;
        }
    }
}
