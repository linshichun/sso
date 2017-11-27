package win.scolia.sso.api.service;

import win.scolia.sso.api.pojo.User;

import java.util.List;

/**
 * Created by scolia on 2017/11/27
 */
public interface AccountService {

    /**
     * 注册
     * @param user 要注册的用户对象
     * @return 返回用户id
     */
    Integer register(User user);

    /**
     * 登录
     * @param username 要登录的用户名
     * @param password 要登录的密码
     * @return bool, 表示是否登录成功
     */
    boolean login(String username, String password);

    /**
     * 登出
     * @param username 要登出的用户名
     * @return bool, 表示是否登出成功
     */
    boolean logout(String username);

    /**
     * 获取用户的角色
     * @param username 用户名
     * @return 返回用户的角色列表
     */
    List<String> getRoles(String username);

    /**
     * 获取角色的权限
     * @param roleName 角色名
     * @return 返回权限列表
     */
    List<String> getPermissions(String roleName);
}
