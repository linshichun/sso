package win.scolia.cloud.sso.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.cloud.sso.annotation.NotBlank;
import win.scolia.cloud.sso.bean.entity.Permission;
import win.scolia.cloud.sso.bean.entity.Role;
import win.scolia.cloud.sso.bean.entity.RolePermission;
import win.scolia.cloud.sso.bean.entity.UserRole;
import win.scolia.cloud.sso.dao.RoleMapper;
import win.scolia.cloud.sso.dao.RolePermissionMapper;
import win.scolia.cloud.sso.dao.UserRoleMapper;
import win.scolia.cloud.sso.exception.DuplicatePermissionException;
import win.scolia.cloud.sso.exception.DuplicateRoleException;
import win.scolia.cloud.sso.exception.MissPermissionException;
import win.scolia.cloud.sso.exception.MissRoleException;
import win.scolia.cloud.sso.service.PermissionService;
import win.scolia.cloud.sso.service.RoleService;
import win.scolia.cloud.sso.util.PageUtils;
import win.scolia.cloud.sso.util.cache.RoleCacheUtils;
import win.scolia.cloud.sso.util.cache.RolePermissionCacheUtils;
import win.scolia.cloud.sso.util.cache.UserCacheUtils;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserCacheUtils userCacheUtils;

    @Autowired
    private RoleCacheUtils roleCacheUtils;

    @Autowired
    private RolePermissionCacheUtils rolePermissionCacheUtils;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public void createRole(@NotBlank String roleName) {
        Role role = roleCacheUtils.get(roleName);
        if (role != null) {
            throw new DuplicateRoleException(String.format("Role already exist: %s", roleName));
        }
        try {
            Role record = Role.of(roleName).forCreate();
            roleMapper.insert(record);
        } catch (DuplicateKeyException e) {
            throw new DuplicateRoleException(String.format("Role already exist: %s", roleName), e);
        }
    }

    @Transactional
    @Override
    public void removeRole(@NotBlank String roleName) {
        Role role = this.getRoleByName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("Role can not be found: %s", roleName));
        }
        roleMapper.deleteByPrimaryKey(role);
        userRoleMapper.delete(
                UserRole.builder().roleId(role.getRoleId()).build()
        );
        rolePermissionMapper.delete(
                RolePermission.builder().roleId(role.getRoleId()).build()
        );
        roleCacheUtils.delete(roleName);
        userCacheUtils.deleteAll();
        rolePermissionCacheUtils.delete(roleName);
    }

    @Override
    public Role getRoleByName(@NotBlank String roleName) {
        Role role = roleCacheUtils.get(roleName);
        if (role == null) {
            Role query = Role.of(roleName);
            role = roleMapper.selectOne(query);
            roleCacheUtils.cache(roleName, role);
        }
        return role;
    }

    @Override
    public PageInfo<Role> listRoles(Integer pageNum) {
        pageUtils.startPage(pageNum);
        List<Role> roles = roleMapper.selectAll();
        return pageUtils.getPageInfo(roles);
    }

    @Override
    public Set<String> getRolePermissions(@NotBlank String roleName) {
        Set<String> permissions = rolePermissionCacheUtils.get(roleName);
        if (permissions == null) {
            Role role = this.getRoleByName(roleName);
            if (role == null) {
                throw new MissRoleException(String.format("Role can not be found: %s", roleName));
            }
            permissions = roleMapper.selectPermissionsByRoleName(roleName);
            rolePermissionCacheUtils.cache(roleName, permissions);
        }
        return permissions;
    }

    @Override
    public void addRolePermission(@NotBlank String roleName, @NotBlank String permission) {
        Role role = this.getRoleByName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("Role can not be found: %s", roleName));
        }
        Permission p = permissionService.getPermission(permission);
        if (p == null) {
            throw new MissPermissionException(String.format("Permission can not be found: %s", permission));
        }
        try {
            rolePermissionMapper.insert(
                    RolePermission.of(role.getRoleId(), p.getPermissionId()).forCreate()
            );
            rolePermissionCacheUtils.delete(roleName);
        } catch (DuplicateKeyException e) {
            throw new DuplicatePermissionException(String.format("Role already has permission; %s", permission), e);
        }
    }

    @Override
    public void removeRolePermission(@NotBlank String roleName, @NotBlank String permission) {
        Role role = this.getRoleByName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("Role can not be found: %s", roleName));
        }
        Permission p = permissionService.getPermission(permission);
        if (p == null) {
            throw new MissPermissionException(String.format("Permission can not be found: %s", permission));
        }
        rolePermissionMapper.delete(
                RolePermission.of(role.getRoleId(), p.getPermissionId())
        );
        rolePermissionCacheUtils.delete(roleName);
    }
}
