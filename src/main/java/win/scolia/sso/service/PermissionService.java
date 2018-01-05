package win.scolia.sso.service;

import com.github.pagehelper.PageInfo;
import win.scolia.sso.bean.entity.Permission;

import java.util.Set;

public interface PermissionService {


    /**
     * 创建一个权限
     * @param permission 权限
     */
    void createPermission(String permission);

    /**
     * 为角色添加权限
     * @param roleName 角色名
     * @param permission 权限
     */
    void addPermissionToRole(String roleName, String permission);

    /**
     * 删除一个权限
     * @param permission 权限
     */
    void removePermission(String permission);

    /**
     * 删除角色的权限
     * @param roleName 角色名
     * @param permission 权限
     */
    void removeRolePermission(String roleName, String permission);

    /**
     * 修改一个权限
     * @param current 旧权限
     * @param target 新权限
     */
    void changePermission(String current, String target);

    /**
     * 根据角色名获取角色的权限
     * @param roleName 角色名称
     * @return 返回权限列表
     */
    Set<String> getPermissionsByRoleName(String roleName);

    /**
     * 获取权限对象
     * @param permission 具体的权限
     * @return 权限对象
     */
    Permission getPermission(String permission);

    /**
     * 列出所有的权限
     * @return 权限列表
     */
    PageInfo<Permission> listAllPermission(Integer pageNum);
}
