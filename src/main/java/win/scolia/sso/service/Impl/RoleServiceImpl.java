package win.scolia.sso.service.Impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import win.scolia.sso.bean.entity.Role;
import win.scolia.sso.bean.entity.RolePermission;
import win.scolia.sso.bean.entity.UserRole;
import win.scolia.sso.bean.entity.UserSafely;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.dao.RolePermissionMapper;
import win.scolia.sso.dao.UserRoleMapper;
import win.scolia.sso.exception.DuplicateRoleException;
import win.scolia.sso.exception.MissRoleException;
import win.scolia.sso.exception.MissUserException;
import win.scolia.sso.service.RoleService;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.PageUtils;
import win.scolia.sso.util.cache.RoleCacheUtils;
import win.scolia.sso.util.cache.RolePermissionCacheUtils;
import win.scolia.sso.util.cache.UserRoleCacheUtils;

import java.util.List;
import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private RoleCacheUtils roleCacheUtils;

    @Autowired
    private UserRoleCacheUtils userRoleCacheUtils;

    @Autowired
    private RolePermissionCacheUtils rolePermissionCacheUtils;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public void createRole(String roleName) {
        Role cacheRole = this.getRoleByRoleName(roleName);
        if (cacheRole != null) {
            throw new DuplicateRoleException(String.format("%s already exist", roleName));
        }
        Role record = new Role(roleName);
        record.forCreate();
        try {
            roleMapper.insert(record);
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
        UserRole record = new UserRole(user.getUserId(), role.getRoleId());
        record.forCreate();
        try {
            userRoleMapper.insert(record);
            userRoleCacheUtils.delete(userName);
        } catch (DuplicateKeyException e) {
            throw new DuplicateRoleException(e);
        }

    }

    @Transactional
    @Override
    public void removeRole(String roleName) {
        Role record = this.getRoleByRoleName(roleName);
        if (record == null) {
            throw new MissRoleException(String.format("%s not exist", roleName));
        }
        roleMapper.deleteByPrimaryKey(record);
        // 删除 用户-角色 的映射
        UserRole userRoleRecord = new UserRole();
        userRoleRecord.setRoleId(record.getRoleId());
        userRoleMapper.delete(userRoleRecord);
        // 删除 角色-权限 的映射
        RolePermission target =  new RolePermission();
        target.setRoleId(record.getRoleId());
        rolePermissionMapper.delete(target);
        // 清除缓存
        roleCacheUtils.delete(roleName); // 清除 角色 缓存
        userRoleCacheUtils.deleteAll(); // 清除所有的 用户-角色 缓存
        rolePermissionCacheUtils.delete(roleName); // 清除对应的 角色-权限 缓存
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
        // 删除 用户-角色 的映射
        UserRole record = new UserRole(user.getUserId(), role.getRoleId());
        userRoleMapper.delete(record);
        userRoleCacheUtils.delete(userName); // 清除对应的 用户-角色 缓存
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
        Role record = new Role(role.getRoleId(), newRoleName);
        record.forUpdate();
        roleMapper.updateByPrimaryKeySelective(record);
        // 清除缓存
        roleCacheUtils.delete(oldRoleName);
        userRoleCacheUtils.deleteAll(); // 清除所有的 用户-角色 缓存
        rolePermissionCacheUtils.delete(oldRoleName); // 清除对应的 角色-权限 缓存
    }

    @Override
    public Set<String> getUserRolesByUserName(String userName) {
        Set<String> roles = userRoleCacheUtils.get(userName);
        if (roles == null) {
            roles = roleMapper.selectUserRolesByUserName(userName);
            userRoleCacheUtils.cache(userName, roles);
        }
        return roles;
    }

    @Override
    public Role getRoleByRoleName(String roleName) {
        Role role = roleCacheUtils.get(roleName);
        if (role == null) {
            Role query = new Role(roleName);
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
}
