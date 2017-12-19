package win.scolia.sso.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.util.CacheUtils;

import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Override
    public Set<String> getPermissionsByRoleName(String roleName) {
        Set<String> permissions = cacheUtils.getRolePermissions(roleName);
        if (permissions != null) {
            return permissions;
        }
        permissions = permissionMapper.selectPermissionsByRoleName(roleName);
        cacheUtils.cacheRolePermissions(roleName, permissions);
        return permissions;
    }
}
