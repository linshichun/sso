package win.scolia.sso.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import win.scolia.sso.api.pojo.User;
import win.scolia.sso.api.pojo.UserCustom;

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

    public UserCustom getUser(String username) {
        try {
            Jedis jedis = jedisPool.getResource();
            String Key = tokenUtils.getKey(username);
            String json = jedis.get(Key);
            if (json != null) {
                jedis.expire(Key, tokenUtils.getExpireTime());
                return MAPPER.readValue(json, UserCustom.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cacheUser(User user, String token) {
        String key = tokenUtils.getKey(user.getUsername());
        UserCustom userCustom = new UserCustom();
        BeanUtils.copyProperties(user, userCustom);
        userCustom.setCacheKey(key);
        userCustom.setToken(token);
        cacheUser(userCustom);
    }

    public void cacheUser(UserCustom userCustom) {
        Jedis jedis = jedisPool.getResource();
        String key = userCustom.getCacheKey();
        // 如何要缓存的用户已经存在, 则延迟其生存时间
        if (jedis.exists(key)) {
            jedis.expire(key, tokenUtils.getExpireTime());
            return;
        }
        try {
            String json = MAPPER.writeValueAsString(userCustom);
            jedis.set(key, json);
            jedis.expire(key, tokenUtils.getExpireTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
