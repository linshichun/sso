package win.scolia.sso.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * 启用spring session
 */
@AutoConfigureAfter(ShiroConfig.class)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 14)
public class HttpSessionConfig {

    /**
     * 使用 Header 模式
     */
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

}
