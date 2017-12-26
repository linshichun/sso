package win.scolia.sso.service.Impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.Permission;
import win.scolia.sso.bean.entity.Role;
import win.scolia.sso.bean.entity.RolePermission;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.exception.DuplicatePermissionException;
import win.scolia.sso.exception.MissPermissionException;
import win.scolia.sso.exception.MissRoleException;
import win.scolia.sso.service.PermissionService;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.PageUtils;

import java.util.Date;
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
        Permission cachePermission = this.getPermission(permission);
        if (cachePermission != null) {
            throw new DuplicatePermissionException(String.format("%s already exist", permission));
        }
        Permission p = new Permission(permission, new Date(), new Date());
        try {
            permissionMapper.insertPermission(p);
        } catch (DuplicateKeyException e) {
            throw new DuplicatePermissionException(String.format("%s already exist", permission), e);
        }
    }

    @Override
    public void addPermissionToRole(String roleName, String permission) {
        Role role = roleService.getRoleByRoleName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("%s not exist", roleName));
        }
        Permission p = this.getPermission(permission);
        if (p == null) {
            throw new MissPermissionException(String.format("%s not exist", permission));
        }
        RolePermission rolePermission = new RolePermission(role.getRoleId(), p.getPermissionId(), new Date(), new Date());
        permissionMapper.insertRolePermission(rolePermission);
        cacheUtils.clearRolePermissions(roleName);
    }

    @Transactional
    @Override
    public void removePermission(String permission) {
        Permission p = this.getPermission(permission);
        if (p == null) {
            throw new MissPermissionException(String.format("%s not exist", permission));
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
            throw new MissRoleException(String.format("%s not exist", roleName));
        }
        Permission p = this.getPermission(permission);
        if (p == null) {
            throw new MissPermissionException(String.format("%s not exist", permission));
        }
        permissionMapper.deleteRolePermissionMapByRoleIdAndPermissionId(role.getRoleId(), p.getPermissionId());
        cacheUtils.clearRolePermissions(roleName);
    }

    @Override
    public void changePermission(String oldPermission, String newPermission) {
        Permission cachePermission = this.getPermission(oldPermission);
        if (cachePermission == null) {
            throw new MissPermissionException(String.format("%s not exist", oldPermission));
        }
        Permission permission = new Permission(newPermission, new Date());
        permissionMapper.updatePermission(oldPermission, permission);
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
