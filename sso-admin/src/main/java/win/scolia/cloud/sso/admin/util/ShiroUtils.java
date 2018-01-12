package win.scolia.cloud.sso.admin.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import win.scolia.cloud.sso.bean.entity.User;

/**
 * shiro 的相关工具类
 */
@Slf4j
public class ShiroUtils {

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
            log.error("Get current user's name error", e);
            return "Unknown";
        }
    }
}
