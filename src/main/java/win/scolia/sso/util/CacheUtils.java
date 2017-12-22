package win.scolia.sso.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.scolia.sso.bean.entity.User;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtils {

    @Value("${sso.cache.prefix:SSO}")
    private String prefix;

    @Value("${sso.cache.expire:2592000}")
    private long expire;

    @Value("${sso.cache.flushExpireWhenHit:true}")
    private boolean isFlush;

    private static final String USER_PREFIX = "USER";

    private static final String ROLE_PREFIX = "ROLE";

    private static final String PERMISSION_PREFIX = "PERMISSION";

    private static final String USER_ROLE_PREFIX = "USER_ROLE";

    private static final String ROLE_PERMISSION_PREFIX = "ROLE_PERMISSION";

    private static Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存key
     *
     * @param prefix 前置
     * @param key 原来的key
     * @return cacheKey
     */
    private String getCacheKey(String prefix, String key){
        assert !StringUtils.isEmpty(prefix) : "Prefix can not be empty";
        assert !StringUtils.isEmpty(key) : "Key can not be empty";
        return String.format("%s:%s:%s", this.prefix, prefix, key);
    }

    /**
     * 缓存一个对象
     *
     * @param cacheKey key
     * @param target 缓存目标
     * @return boolean 表示是否缓存成功
     */
    private boolean cacheObject(String cacheKey, Object target) {
        assert !StringUtils.isEmpty(cacheKey) : "CacheKey can not be empty";
        assert target != null : "Cache target can not be null";
        try {
            redisTemplate.opsForValue().set(cacheKey, target, expire, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cache error: {}", e);
            return false;
        }
    }

    /**
     * 获取缓存的对象
     *
     * @param cacheKey 缓存key
     * @param clazz 缓存的对象类型
     * @return 缓存的对象
     */
    private <T> T getCacheObject(String cacheKey, Class<T> clazz) {
        assert !StringUtils.isEmpty(cacheKey) : "CacheKey can not be empty";
        assert clazz != null : "Clazz can not be null";
        try {
            BoundValueOperations<String, Object> operations = redisTemplate.boundValueOps(cacheKey);
            Object target = operations.get();
            if (target != null) {
                if (this.isFlush) {
                    operations.expire(expire, TimeUnit.SECONDS);
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Hit cache: {}", cacheKey);
                }
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Cache miss: {}", cacheKey);
                }
            }
            return clazz.cast(target);
        } catch (Exception e) {
            LOGGER.error("Get cache error: {}", cacheKey);
            return null;
        }
    }

    /**
     * 清除缓存
     *
     * @param cacheKey 缓存key
     */
    private void clearCache(String cacheKey) {
        assert !StringUtils.isEmpty(cacheKey) : "CacheKey can not be empty";
        redisTemplate.delete(cacheKey);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Clear cache user: {}", cacheKey);
        }
    }

    /**
     * 缓存用户对象
     *
     * @param user 用户对象
     */
    public void cacheUser(User user) {
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return;
        }
        String cacheKey = this.getCacheKey(USER_PREFIX, user.getUserName());
        if (this.cacheObject(cacheKey, user)){
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache user: {}", user.getUserName());
            }
        }
    }

    /**
     * 获取缓存的用户对象
     *
     * @param userName 用户名
     * @return 缓存的用户对象, 失败时返回null
     */
    public User getUser(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        String cacheKey = this.getCacheKey(USER_PREFIX, userName);
        return this.getCacheObject(cacheKey, User.class);
    }

    /**
     * 清除用户的缓存
     *
     * @param userName 用户名
     */
    public void clearUser(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return;
        }
        String cacheKey = this.getCacheKey(USER_PREFIX, userName);
        this.clearCache(cacheKey);
    }

    /**
     * 缓存用户角色信息
     *
     * @param userName 用户名称
     * @param roles    用户对应的角色, 可以为空集合
     */
    public void cacheUserRoles(String userName, Set<String> roles) {
        if (StringUtils.isEmpty(userName) || roles == null) {
            return;
        }
        String cacheKey = this.getCacheKey(USER_ROLE_PREFIX, userName);
        if (this.cacheObject(cacheKey, roles)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache: {}", cacheKey);
            }
        }
    }

    /**
     * 根据用户名称, 获取缓存的角色信息
     *
     * @param userName 用户名
     * @return 角色信息, 失败时返回null
     */
    @SuppressWarnings("unchecked")
    public Set<String> getUserRoles(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        String cacheKey = this.getCacheKey(USER_ROLE_PREFIX, userName);
        return this.getCacheObject(cacheKey, Set.class);
    }

    /**
     * 清除特定用户的 用户-角色 缓存信息
     * @param userName 用户名
     */
    public void clearUserRoles(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return;
        }
        String cacheKey = this.getCacheKey(USER_ROLE_PREFIX, userName);
        this.clearCache(cacheKey);
    }

    /**
     * 清除所有的 用户-角色 缓存信息
     */
    public void clearAllUserRoles() {
        Set<String> keys = redisTemplate.keys(String.format("%s:%s", prefix, USER_ROLE_PREFIX));
        redisTemplate.delete(keys);
    }

    /**
     * 缓存角色的权限信息
     *
     * @param roleName    角色名
     * @param permissions 权限信息
     */
    public void cacheRolePermissions(String roleName, Set<String> permissions) {
        if (StringUtils.isEmpty(roleName) || permissions == null) {
            return;
        }
        String cacheKey = this.getCacheKey(ROLE_PERMISSION_PREFIX, roleName);
        if (this.cacheObject(cacheKey, permissions)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Cache: {}", cacheKey);
            }
        }
    }

    /**
     * 获取角色的权限信息
     * @param roleName 角色名称
     * @return 权限信息, 失败时返回null
     */
    @SuppressWarnings("unchecked")
    public Set<String> getRolePermissions(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            return null;
        }
        String cacheKey = this.getCacheKey(ROLE_PERMISSION_PREFIX, roleName);
        return this.getCacheObject(cacheKey, Set.class);
    }

    /**
     * 清除 角色-权限 的缓存信息
     * @param roleName 角色名
     */
    public void clearRolePermissions(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            return ;
        }
        String cacheKey = this.getCacheKey(ROLE_PERMISSION_PREFIX, roleName);
        this.clearCache(cacheKey);
    }

    /**
     * 清除所有的 角色-权限 缓存信息
     */
    public void clearAllRolePermissions(){
        Set<String> keys = redisTemplate.keys(String.format("%s:%s", prefix, ROLE_PERMISSION_PREFIX));
        redisTemplate.delete(keys);
    }

}