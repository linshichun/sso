package win.scolia.sso.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import win.scolia.sso.bean.entity.User;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@PropertySource("classpath:sso.properties")
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

    /**
     * 缓存用户对象
     *
     * @param user 用户对象
     */
    public void cacheUser(User user) {
        if (user == null) {
            return;
        }
        String cacheKey = String.format("%s_%s_%s", prefix, USER_PREFIX, user.getUserName());
        try {
            userRedisTemplate.opsForValue().set(cacheKey, user, expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache user: {}", user.getUserName());
            }
        } catch (Exception e) {
            LOGGER.error("Cache error: {}", e);
        }
    }

    /**
     * 获取缓存的用户对象
     *
     * @param username 用户名
     * @return 缓存的用户对象, 失败时返回null
     */
    public User getUser(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        String cacheKey = String.format("%s_%s_%s", prefix, USER_PREFIX, username);
        User user = null;
        try {
            BoundValueOperations<String, User> operations = userRedisTemplate.boundValueOps(cacheKey);
            user = operations.get();
            operations.expire(expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Hit cache user: {}", username);
            }
        } catch (Exception e) {
            LOGGER.error("Cache error: {}", e);
        }
        return user;
    }


    /**
     * 缓存用户角色信息
     *
     * @param userName 用户名称
     * @param roles    用户对应的角色
     */
    public void cacheRoles(String userName, Set<String> roles) {
        if (StringUtils.isEmpty(userName) || CollectionUtils.isEmpty(roles)) {
            return;
        }
        String cacheKey = String.format("%s_%s_%s", prefix, ROLE_PREFIX, userName);
        try {
            setRedisTemplate.opsForValue().set(cacheKey, roles, expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache roles: {}:{}", userName, roles);
            }
        } catch (Exception e) {
            LOGGER.error("Cache error: {}", e);
        }
    }

    /**
     * 根据用户名称, 获取缓存的角色信息
     *
     * @param userName 用户名
     * @return 角色信息, 失败时返回null
     */
    public Set<String> getRoles(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        String cacheKey = String.format("%s_%s_%s", prefix, ROLE_PREFIX, userName);
        Set<String> roles = null;
        try {
            BoundValueOperations<String, Set<String>> operations = setRedisTemplate.boundValueOps(cacheKey);
            roles = operations.get();
            operations.expire(expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Hit cache role: {}:{}", userName, roles);
            }
        } catch (Exception e) {
            LOGGER.error("Cache error: {}", e);
        }
        return roles;
    }

    /**
     * 缓存角色的权限信息
     *
     * @param roleName    角色名
     * @param permissions 权限信息
     */
    public void cachePermissions(String roleName, Set<String> permissions) {
        if (StringUtils.isEmpty(roleName) || CollectionUtils.isEmpty(permissions)) {
            return;
        }
        String cacheKey = String.format("%s_%s_%s", prefix, PERMISSION_PREFIX, roleName);
        try {
            setRedisTemplate.opsForValue().set(cacheKey, permissions, expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache permissions: {}:{}", roleName, permissions);
            }
        } catch (Exception e) {
            LOGGER.error("Cache error: {}", e);
        }
    }

    /**
     * 获取角色的权限信息
     * @param roleName 角色名称
     * @return 权限信息, 失败时返回null
     */
    public Set<String> getPermissions(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            return null;
        }
        String cacheKey = String.format("%s_%s_%s", prefix, PERMISSION_PREFIX, roleName);
        Set<String> permissions = null;
        try {
            BoundValueOperations<String, Set<String>> operations = setRedisTemplate.boundValueOps(cacheKey);
            permissions = operations.get();
            operations.expire(expire, TimeUnit.SECONDS);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Hit cache permissions: {}:{}", roleName, permissions);
            }
        } catch (Exception e) {
            LOGGER.error("Cache error: {}", e);
        }
        return permissions;
    }
}
