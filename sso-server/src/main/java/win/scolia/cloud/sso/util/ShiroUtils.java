package win.scolia.cloud.sso.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import win.scolia.cloud.sso.bean.entity.User;

/**
 * shiro 的相关工具类
 */
public class ShiroUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ShiroUtils.class);

    /**
     * 获取当前登录的用户对象
     *
     * @return 用户对象
     */
    public static User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

    /**
     * 获取当前的用户名
     *
     * @return 用户名, 无法获取是返回Unknown
     */
    public static String getCurrentUserName() {
        try {
            return getCurrentUser().getUserName();
        } catch (Exception e) {
            LOGGER.error("Get current user's name error", e);
            return "Unknown";
        }
    }
}
