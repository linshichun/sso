package win.scolia.sso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 启用spring session
 */
@AutoConfigureAfter(ShiroConfig.class)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 14)
public class SpringSessionConfig {

    @Value("${sso.cookie.max-age}")
    private Integer maxAge;


    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieMaxAge(maxAge);
        return serializer;
    }

}
