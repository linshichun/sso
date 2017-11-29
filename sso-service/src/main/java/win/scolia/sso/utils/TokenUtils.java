package win.scolia.sso.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by scolia on 2017/11/28
 */
@Component
@PropertySource("classpath:sso.properties")
public class TokenUtils {

    @Value("${token.salt}")
    private String tokenSalt;

    @Value("${token.prefix}")
    private String keyPrefix;

    @Value("${token.expire}")
    private Integer expireTime;

    public Integer getExpireTime() {
        return expireTime;
    }

    public String getKey(String key) {
        return String.format("%s_%s", keyPrefix.toUpperCase(), key.toUpperCase());
    }
    public String getToken(String username) {
        return DigestUtils.md5Hex(username + tokenSalt + Long.toString(System.currentTimeMillis()));
    }
}
