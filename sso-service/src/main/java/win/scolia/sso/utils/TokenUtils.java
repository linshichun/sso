package win.scolia.sso.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * Created by scolia on 2017/11/28
 */
@Component
@PropertySource("classpath:encrypt.properties")
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

    public String getTokenKey(String username) {
        return String.format("%s_%s", tokenPrefix.toUpperCase(), username.toUpperCase());
    }
    public String getToken(String username) {
        MessageDigest messageDigest =  DigestUtils.getMd5Digest();
        messageDigest.update(tokenSalt.getBytes());
        messageDigest.update(Long.toString(System.currentTimeMillis()).getBytes());
        messageDigest.update(username.getBytes());
        return Hex.encodeHexString(messageDigest.digest());
    }
}
