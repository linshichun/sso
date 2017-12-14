package win.scolia.sso.service;

import win.scolia.sso.bean.vo.UserVO;

import java.util.Set;

/**
 * Created by scolia on 2017/11/27
 */
public interface AccountService {

    /**
     * 注册
     * @param userVO 要注册的用户对象
     * @return 返回用户id, 返回null表示注册失败
     */
    Long register(UserVO userVO);

    /**
     * 登录
     * @param userVO 要登录的用户对象
     * @return 登录成功时返回token, 失败时返回null
     */
    String login(UserVO userVO);

    /**
     * 根据token登录
     * @param userName 用户名
     * @param token token
     * @return 返回对应的用户名, 失败返回null
     */
    Boolean login(String userName, String token);

    /**
     * 根据用户名登出
     * @param userVO 要登出的用户对象
     */
    void logout(UserVO userVO);

    /**
     * 登出
     * @param token 要登出的用户的token
     */
    void logout(String token);

    /**
     * 获取用户名获取角色
     * @param userVO 角色信息
     * @return 返回用户的角色列表
     */
    Set<String> getRoles(UserVO userVO);

    /**
     * 获取用户的角色
     * @param token 用户token
     * @return 返回用户的角色列表
     */
    Set<String> getRoles(String token);

    /**
     * 获取用户名获取权限
     * @param userVO 角色信息
     * @return 返回权限列表
     */
    Set<String> getPermissions(UserVO userVO);

    /**
     * 获取角色的权限
     * @param token 用户token
     * @return 返回权限列表
     */
    Set<String> getPermissions(String token);
}
