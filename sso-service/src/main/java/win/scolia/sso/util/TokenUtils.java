package win.scolia.sso.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@PropertySource("classpath:sso.properties")
public class TokenUtils {

    @Value("${sso.cache.prefix}")
    private String prefix;

    @Value("${sso.cache.expire}")
    private Long expire;

    @Value("${sso.token.salt}")
    private String salt;

    private static final String TOKEN_PREFIX = "TOKEN";

    private static Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取token
     *
     * @param userName 用户名称
     * @return token
     */
    public String getNewToken(String userName) {
        return DigestUtils.md5Hex(userName + salt + System.currentTimeMillis());
    }

    /**
     * 因为token代表了session, 所以不允许失败
     *
     * @param userName 用户名
     * @param token    token
     */
    public void cacheToken(String userName, String token) {
        String cacheKey = String.format("%s_%s_%s", prefix, TOKEN_PREFIX, userName);
        stringRedisTemplate.opsForValue().set(cacheKey, token, expire, TimeUnit.SECONDS);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Cache token: {}:{}", userName, token);
        }
    }

    /**
     * 在缓存中获取token
     * @param userName 用户名
     * @return token 或 null
     */
    public String getToken(String userName) {
        String cacheKey = String.format("%s_%s_%s", prefix, TOKEN_PREFIX, userName);
        BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps(cacheKey);
        String token = operations.get();
        operations.expire(expire, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 清理用户的登录token
     * @param userName 用户名
     */
    public void clearToken(String userName) {
        String cacheKey = String.format("%s_%s_%s", prefix, TOKEN_PREFIX, userName);
        stringRedisTemplate.delete(cacheKey);
    }

}
