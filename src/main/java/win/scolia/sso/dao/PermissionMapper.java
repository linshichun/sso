package win.scolia.sso.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import win.scolia.sso.bean.entity.Permission;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionMapper {

    /**
     * 新增一条权限
     * @param permission 权限
     */
    void insertPermission(@Param("permission") String permission);

    /**
     * 为角色添加权限
     * @param roleId 角色id
     * @param permissionId 权限id
     */
    void insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 根据权限名称来删除权限
     * @param permission 权限名称
     */
    void deletePermission(@Param("permission") String permission);

    /**
     * 通过权限id, 删除其在 角色-权限 映射表中的所有记录
     * @param permissionId 权限id
     */
    void deleteRolePermissionMapByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 通过角色id, 删除其在 角色-权限 映射表中的所有记录
     * @param roleId 角色id
     */
    void deleteRolePermissionMapByRoleId(@Param("roleId") Long roleId);

    /**
     *  通过 角色id和权限id, 删除其在 角色-权限 映射表中的所有记录
     * @param roleId 角色id
     * @param permissionId 权限id
     */
    void deleteRolePermissionMapByRoleIdAndPermissionId(@Param("roleId") Long roleId,
                                                        @Param("permissionId") Long permissionId);

    /**
     * 更新权限名
     * @param oldPermission 旧的权限名
     * @param newPermission 新的权限名
     */
    void updatePermission(@Param("oldPermission") String oldPermission,
                          @Param("newPermission") String newPermission);

    /**
     * 根据角色名获取角色的权限
     * @param roleName 角色名称
     * @return 返回权限列表
     */
    Set<String> selectPermissionsByRoleName(@Param("roleName") String roleName);

    /**
     * 获取权限对象
     * @param permission 权限
     * @return 权限对象
     */
    Permission selectPermission(@Param("permission") String permission);

    /**
     * 列出所有的权限
     * @return 权限列表
     */
    List<Permission> selectAllPermissions();
}
