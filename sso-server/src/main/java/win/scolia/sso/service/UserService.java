package win.scolia.sso.service;

import org.apache.ibatis.annotations.Param;
import win.scolia.sso.api.bean.entity.User;

import java.util.Set;

public interface UserService {
    /**
     * 插入一个新的用户
     * @param user 插入的用户对象
     */
    void createUser(@Param("user") User user);

    /**
     * 根据用户名获得用户信息
     * @param username 要查询的用户名
     * @return 返回查询得到的用户密码
     */
    User getUserByUsername(@Param("username") String username);

    /**
     * 根据用户名获取用户的角色
     * @param username 用户名
     * @return 返回角色列表
     */
    Set<String> getRolesByUserName(@Param("username") String username);

    /**
     * 根据角色名获取角色的权限
     * @param roleName 角色名称
     * @return 返回权限列表
     */
    Set<String> getPermissionsByRoleName(@Param("roleName") String roleName);
}
