package win.scolia.sso.service;

import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.vo.UserVO;

import java.util.Set;

public interface UserService {
    /**
     * 插入一个新的用户
     * @param userVO 插入的用户对象
     */
    void createUser(UserVO userVO);

    /**
     * 根据用户名获得用户信息
     * @param username 要查询的用户名
     * @return 返回查询得到的用户密码
     */
    User getUserByUsername(String username);

    /**
     * 根据用户名获取用户的角色
     * @param username 用户名
     * @return 返回角色列表
     */
    Set<String> getRolesByUserName(String username);

    /**
     * 根据角色名获取角色的权限
     * @param roleName 角色名称
     * @return 返回权限列表
     */
    Set<String> getPermissionsByRoleName(String roleName);
}
