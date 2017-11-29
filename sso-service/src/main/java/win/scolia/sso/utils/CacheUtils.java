package win.scolia.sso.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import win.scolia.sso.api.pojo.User;
import win.scolia.sso.api.pojo.UserCustom;

import java.util.List;
import java.util.Set;

/**
 * Created by scolia on 2017/11/29
 */
@Component
public class CacheUtils {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private TokenUtils tokenUtils;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 根据token, 去redis获取用户对象
     * @param token 用户的token
     * @return 成功时返回用户对象, 失败时返回null
     */
    public UserCustom getUser(String token) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = tokenUtils.getKey(token);
            String json = jedis.get(key);
            if (json != null) {
                jedis.expire(key, tokenUtils.getExpireTime());
                return MAPPER.readValue(json, UserCustom.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cacheUser(User user, String token) {
        String key = tokenUtils.getKey(token);
        UserCustom userCustom = new UserCustom();
        BeanUtils.copyProperties(user, userCustom);
        userCustom.setCacheKey(key);
        userCustom.setToken(token);
        cacheUser(userCustom, false);
    }

    private void cacheUser(UserCustom userCustom, boolean reset) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = userCustom.getCacheKey();
            // 如果不强制重新设置, 且用户已经缓存, 就刷新其生存时间
            if (!reset && jedis.exists(key)) {
                jedis.expire(key, tokenUtils.getExpireTime());
                return;
            }
            String json = MAPPER.writeValueAsString(userCustom);
            jedis.set(key, json);
            jedis.expire(key, tokenUtils.getExpireTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delCacheUser(String token) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = tokenUtils.getKey(token);
            jedis.del(key);
        }
    }

    public List<String> getRoles(String token) {
        UserCustom userCustom = getUser(token);
        if (userCustom != null) {
            return userCustom.getRoles();
        }
        return null;
    }

    /**
     * 缓存用户的角色信息, 这里会覆盖原来的缓存, 请确保userCustom中有原先的信息
     * @param userCustom 原先的用户对象
     * @param roles 用户的角色列表
     */
    public void cacheUserRoles(UserCustom userCustom, List<String> roles) {
        userCustom.setRoles(roles);
        cacheUser(userCustom, true);
    }

    public Set<String> getPermissions(String token) {
        UserCustom userCustom = getUser(token);
        if (userCustom != null) {
            return userCustom.getPermissions();
        }
        return null;
    }

    /**
     * 缓存用户的权限信息, 这里会覆盖原来的缓存, 请确保userCustom中有原先的信息
     * @param userCustom 原先的用户对象
     * @param permissions 用户的权限列表
     */
    public void cacheUserPermissions(UserCustom userCustom, Set<String> permissions) {
        userCustom.setPermissions(permissions);
        cacheUser(userCustom, true);
    }
}
