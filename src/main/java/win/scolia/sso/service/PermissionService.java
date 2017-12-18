package win.scolia.sso.service;

import java.util.Set;

public interface PermissionService {

    /**
     * 根据角色名获取角色的权限
     * @param roleName 角色名称
     * @return 返回权限列表
     */
    Set<String> getPermissionsByRoleName(String roleName);
}
