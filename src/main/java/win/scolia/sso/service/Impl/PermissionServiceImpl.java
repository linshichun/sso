package win.scolia.sso.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.Permission;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.util.CacheUtils;

import java.util.List;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Override
    public void createPermission(String permission) {
        permissionMapper.insertPermission(permission);
    }

    @Transactional
    @Override
    public void removePermission(String permission) {
        Permission p = permissionMapper.selectPermission(permission);
        if (p == null) {
            return;
        }
        // 删除权限的同时, 也删除其映射表中的相关记录
        permissionMapper.deleteRolePermissionMapByPermissionId(p.getPermissionId());
    }

    @Override
    public void changePermission(String oldPermission, String newPermission) {
        permissionMapper.updatePermission(oldPermission, newPermission);
    }

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

    @Override
    public List<Permission> listAllPermission() {
        // TODO 列出所有权限
        return null;
    }
}
