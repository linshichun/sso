package win.scolia.sso.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 启用spring session
 */
@EnableRedisHttpSession
@AutoConfigureAfter(ShiroConfig.class)
public class HttpSessionConfig {
}
