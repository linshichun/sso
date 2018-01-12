package win.scolia.cloud.sso.service;

import com.github.pagehelper.PageInfo;
import win.scolia.cloud.sso.bean.entity.Role;
import win.scolia.cloud.sso.exception.DuplicateRoleException;
import win.scolia.cloud.sso.exception.MissPermissionException;
import win.scolia.cloud.sso.exception.MissRoleException;

import java.util.Set;

public interface RoleService {

    /**
     * 创建一个角色
     *
     * @param roleName 角色名
     * @throws DuplicateRoleException 角色重复
     */
    void createRole(String roleName);

    /**
     * 删除一个角色, 同时将其在映射表中的记录也删除掉
     *
     * @param roleName 角色名
     * @throws MissRoleException 角色不存在
     */
    void removeRole(String roleName);

    /**
     * 根据角色名获取角色
     *
     * @param roleName 角色名
     * @return 角色对象或null
     */
    Role getRoleByName(String roleName);

    /**
     * 获取所有的角色信息
     *
     * @param pageNum 页码
     * @return 角色列表
     */
    PageInfo<Role> listRoles(Integer pageNum);

    /**
     * 根据角色名获取权限
     *
     * @param roleName 角色名
     * @return 权限集合
     * @throws MissRoleException 角色不存在
     */
    Set<String> getRolePermissions(String roleName);

    /**
     * 为角色添加权限
     * @param roleName 角色名
     * @param permission 权限
     * @throws MissRoleException 角色不存在
     * @throws MissPermissionException 权限不存在
     */
    void addRolePermission(String roleName, String permission);

    /**
     * 删除角色的权限
     *
     * @param permission 具体权限
     * @throws MissRoleException 角色不存在
     * @throws MissPermissionException 权限不存在
     */
    void removeRolePermission(String roleName, String permission);
}
