package win.scolia.sso.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取配置的类
 */
@Component
@PropertySource("classpath:sso.properties")
public class EnvUtils {

    @Value("${sso.encrypt.salt}")
    private String encryptSalt;

    @Value("${sso.cache.prefix}")
    private String cachePrefix;

    @Value("${sso.cache.expire}")
    private long cacheExpire;

    @Value("${sso.cache.flushExpireWhenHit}")
    private boolean cacheExpireFlushWhenHit;

    public String getEncryptSalt() {
        return encryptSalt;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public long getCacheExpire() {
        return cacheExpire;
    }

    public boolean getCacheExpireFlushWhenHit() {
        return cacheExpireFlushWhenHit;
    }
}
