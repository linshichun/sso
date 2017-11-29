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
    private String tokenPrefix;

    @Value("${token.expire}")
    private Integer expireTime;

    public Integer getExpireTime() {
        return expireTime;
    }

    public String getKey(String username) {
        return String.format("%s_%s", tokenPrefix.toUpperCase(), username.toUpperCase());
    }
    public String getToken(String username) {
        return DigestUtils.md5Hex(username + tokenSalt + Long.toString(System.currentTimeMillis()));
    }
}
