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
     * @param userName 用户名
     * @param password 密码
     * @return 登录成功时返回token, 失败时返回null
     */
    String loginByPassword(String userName, String password);

    /**
     * 根据token登录
     * @param userName 用户名
     * @param token token
     * @return 返回对应的用户名, 失败返回null
     */
    Boolean loginByToken(String userName, String token);

    /**
     * 根据用户名登出
     * @param userName 用户名
     */
    void logout(String userName);

    /**
     * 根据用户名获取角色
     * @param userName 用户名
     * @return 返回用户的角色列表
     */
    Set<String> getRoles(String userName);

    /**
     * 根据角色获取权限
     * @param roleName 角色名
     * @return 返回权限列表
     */
    Set<String> getPermissions(String roleName);
}
