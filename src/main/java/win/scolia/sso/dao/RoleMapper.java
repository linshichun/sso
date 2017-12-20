package win.scolia.sso.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import win.scolia.sso.bean.entity.Role;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleMapper {

    /**
     * 添加一个角色
     * @param roleName 角色名
     */
    void insertRole(@Param("roleName") String roleName);


    /**
     * 根据角色名删除记录
     * @param roleName 角色名
     */
    void deleteRoleByName(@Param("roleName") String roleName);

    /**
     * 通过角色id, 删除其在 用户-角色 映射表中的所有记录
     * @param roleId 角色id
     */
    void deleteUserRoleMapByRoleId(@Param("roleId") Long roleId);

    /**
     * 通过用户id, 删除其在 用户-角色 映射表中的所有记录
     * @param userId 用户id
     */
    void deleteUserRoleMapByUserId(@Param("userId") Long userId);

    /**
     * 更新角色名
     * @param oldRoleName 旧角色名
     * @param newRoleName 新角色名
     */
    void updateRoleByName(@Param("oldRoleName") String oldRoleName,
                          @Param("newRoleName") String newRoleName);

    /**
     * 根据用户名获取用户的角色
     * @param username 用户名
     * @return 返回角色列表
     */
    Set<String> selectUserRolesByUserName(@Param("userName") String username);

    /**
     * 通过角色名获取角色对象
     * @param roleName 角色名
     * @return 角色对象
     */
    Role selectRoleByRoleName(@Param("roleName") String roleName);

    /**
     * 列出所有的角色
     * @return 角色列表
     */
    List<Role> selectAllRoles();
}
