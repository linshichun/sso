package win.scolia.sso.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:sso.properties")
public class TokenUtils {

    @Value("${sso.token.salt}")
    private String salt;

    /**
     * 获取token
     *
     * @param userName 用户名称
     * @return token
     */
    public String getToken(String userName) {
        return DigestUtils.md5Hex(userName + salt + System.currentTimeMillis());
    }

}
