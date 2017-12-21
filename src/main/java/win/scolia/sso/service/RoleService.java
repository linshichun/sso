package win.scolia.sso.service;

import com.github.pagehelper.PageInfo;
import win.scolia.sso.bean.entity.Role;

import java.util.Set;

public interface RoleService {

    /**
     * 创建一个角色
     * @param roleName 角色名
     */
    void createRole(String roleName);

    /**
     * 删除一个角色, 同时将其在映射表中的记录也删除掉
     * @param roleName 角色名
     */
    void removeRole(String roleName);

    /**
     * 更新角色名称
     * @param oldRoleName 旧角色名
     * @param newRoleName 新角色名
     */
    void changeRoleName(String oldRoleName, String newRoleName);

    /**
     * 根据用户名获取用户的角色
     * @param username 用户名
     * @return 返回角色列表
     */
    Set<String> getUserRolesByUserName(String username);

    /**
     * 获取所有的角色信息
     * @param pageNum 页码
     * @return 角色列表
     */
    PageInfo<Role> listRoles(Integer pageNum);
}
