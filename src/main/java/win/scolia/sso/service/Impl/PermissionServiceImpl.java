package win.scolia.sso.service.Impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.Permission;
import win.scolia.sso.bean.entity.Role;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.PageUtils;

import java.util.List;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public void createPermission(String permission) {
        permissionMapper.insertPermission(permission);
    }

    @Override
    public void addPermissionToRole(String roleName, String permission) {
        Role role = roleService.getRoleByRoleName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Role not exist");
        }
        Permission p = this.getPermission(permission);
        if (p == null) {
            throw new IllegalArgumentException("Permission not exist");
        }
        permissionMapper.insertRolePermission(role.getRoleId(), p.getPermissionId());
        cacheUtils.clearRolePermissions(roleName);
    }

    @Transactional
    @Override
    public void removePermission(String permission) {
        Permission p = this.getPermission(permission);
        if (p == null) {
            return;
        }
        // 删除权限的同时, 也删除其映射表中的相关记录
        permissionMapper.deletePermission(permission);
        permissionMapper.deleteRolePermissionMapByPermissionId(p.getPermissionId());
        cacheUtils.clearPermission(permission);
        cacheUtils.clearAllRolePermissions();
    }

    @Override
    public void removeRolePermission(String roleName, String permission) {
        Role role = roleService.getRoleByRoleName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Role not exist");
        }
        Permission p = this.getPermission(permission);
        if (p == null) {
            throw new IllegalArgumentException("Permission not exist");
        }
        permissionMapper.deleteRolePermissionMapByRoleIdAndPermissionId(role.getRoleId(), p.getPermissionId());
        cacheUtils.clearRolePermissions(roleName);
    }

    @Override
    public void changePermission(String oldPermission, String newPermission) {
        permissionMapper.updatePermission(oldPermission, newPermission);
        cacheUtils.clearPermission(oldPermission);
        cacheUtils.clearAllRolePermissions();
    }

    @Override
    public Set<String> getPermissionsByRoleName(String roleName) {
        Set<String> permissions = cacheUtils.getRolePermissions(roleName);
        if (permissions == null) {
            permissions = permissionMapper.selectPermissionsByRoleName(roleName);
            cacheUtils.cacheRolePermissions(roleName, permissions);
        }
        return permissions;
    }

    @Override
    public Permission getPermission(String permission) {
        Permission p = cacheUtils.getPermission(permission);
        if (p == null) {
            p = permissionMapper.selectPermission(permission);
            cacheUtils.cachePermission(p);
        }
        return p;
    }

    @Override
    public PageInfo<Permission> listAllPermission(Integer pageNum) {
        pageUtils.startPage(pageNum);
        List<Permission> permissions = permissionMapper.selectAllPermissions();
        return pageUtils.getPageInfo(permissions);
    }
}
