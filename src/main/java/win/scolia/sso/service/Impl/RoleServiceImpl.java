package win.scolia.sso.service.Impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.Role;
import win.scolia.sso.bean.entity.UserRole;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.exception.DuplicateRoleException;
import win.scolia.sso.exception.MissRoleException;
import win.scolia.sso.exception.MissUserException;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.PageUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public void createRole(String roleName) {
        Role cacheRole = this.getRoleByRoleName(roleName);
        if (cacheRole != null) {
            throw new DuplicateRoleException(String.format("%s already exist", roleName));
        }
        Role role = new Role(roleName, new Date(), new Date());
        try {
            roleMapper.insertRole(role);
        } catch (DuplicateKeyException e) {
            throw new DuplicateRoleException(String.format("%s already exist", roleName), e);
        }
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        UserSafely user = userService.getUserSafelyByUserName(userName);
        if (user == null) {
            throw new MissUserException(String.format("%s not exist", userName));
        }
        Role role = this.getRoleByRoleName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("%s not exist", roleName));
        }
        UserRole userRole = new UserRole(user.getUserId(), role.getRoleId(), new Date(), new Date());
        try {
            roleMapper.insertUserRoleMap(userRole);
            cacheUtils.clearUserRoles(userName);
        } catch (DuplicateKeyException e) {
            throw new DuplicateRoleException(e);
        }

    }

    @Transactional
    @Override
    public void removeRole(String roleName) {
        Role role = this.getRoleByRoleName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("%s not exist", roleName));
        }
        roleMapper.deleteRoleByName(roleName);
        roleMapper.deleteUserRoleMapByRoleId(role.getRoleId()); // 删除 用户-角色 的映射
        permissionMapper.deleteRolePermissionMapByRoleId(role.getRoleId()); // 删除 角色-权限 的映射
        cacheUtils.clearRole(roleName); // 清除 角色 缓存
        cacheUtils.clearAllUserRoles(); // 清除所有的 用户-角色 缓存
        cacheUtils.clearRolePermissions(roleName); // 清除对应的 角色-权限 缓存
    }

    @Override
    public void romeUserRole(String userName, String roleName) {
        UserSafely user = userService.getUserSafelyByUserName(userName);
        if (user == null) {
            throw new MissUserException(String.format("%s not exist", userName));
        }
        Role role = this.getRoleByRoleName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("%s not exist", roleName));
        }
        roleMapper.deleteUserRoleMapByUserIdAndRoleId(user.getUserId(), role.getRoleId()); // 删除 用户-角色 的映射
        cacheUtils.clearUserRoles(userName); // 清除对应的 用户-角色 缓存
    }

    @Override
    public void changeRoleName(String oldRoleName, String newRoleName) {
        Role role = this.getRoleByRoleName(oldRoleName);
        if (role == null) {
            throw new MissRoleException(String.format("%s not exist", oldRoleName));
        }
        Role newRole = this.getRoleByRoleName(newRoleName);
        if (newRole != null) {
            throw new DuplicateRoleException(String.format("%s already exist", newRoleName));
        }
        role.setRoleName(newRoleName);
        role.setLastModified(new Date());
        roleMapper.updateRoleByName(oldRoleName, role);
        cacheUtils.clearRole(oldRoleName);
        cacheUtils.clearAllUserRoles(); // 清除所有的 用户-角色 缓存
        cacheUtils.clearRolePermissions(oldRoleName); // 清除对应的 角色-权限 缓存
    }

    @Override
    public Set<String> getUserRolesByUserName(String userName) {
        Set<String> roles = cacheUtils.getUserRoles(userName);
        if (roles == null) {
            roles = roleMapper.selectUserRolesByUserName(userName);
            cacheUtils.cacheUserRoles(userName, roles);
        }
        return roles;
    }

    @Override
    public Role getRoleByRoleName(String roleName) {
        Role role = cacheUtils.getRole(roleName);
        if (role == null) {
            role = roleMapper.selectRoleByRoleName(roleName);
            cacheUtils.cacheRole(role);
        }
        return role;
    }

    @Override
    public PageInfo<Role> listRoles(Integer pageNum) {
        pageUtils.startPage(pageNum);
        List<Role> roles = roleMapper.selectAllRoles();
        return pageUtils.getPageInfo(roles);
    }
}
