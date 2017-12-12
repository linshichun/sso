package win.scolia.sso.util;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenUtils {

    /**
     * 获取token
     * @param userName 用户名称
     * @return token
     */
    public static String getToken(String userName) {
        return DigestUtils.md5Hex(userName + System.currentTimeMillis());
    }

}
