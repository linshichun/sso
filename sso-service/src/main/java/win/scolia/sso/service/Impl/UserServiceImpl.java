package win.scolia.sso.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.dao.UserMapper;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.CacheUtils;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private CacheUtils cacheUtils;

    @Override
    public void createUser(User user) {
        User cacheUser = getUserByUsername(user.getUserName());
        if (cacheUser != null) {
            throw new DuplicateUserException("该用户已存在");
        }
        try {
            userMapper.insertUser(user);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserException("该用户已存在", e);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        User user = cacheUtils.getUser(username);
        if (user != null) {
            return user;
        }
        user = userMapper.selectUserByUserName(username);
        cacheUtils.cacheUser(user);
        return user;
    }

    @Override
    public Set<String> getRolesByUserName(String userName) {
        Set<String> roles = cacheUtils.getRoles(userName);
        if (roles != null) {
            return roles;
        }
        roles = roleMapper.selectRolesByUserName(userName);
        cacheUtils.cacheRoles(userName, roles);
        return roles;
    }

    @Override
    public Set<String> getPermissionsByRoleName(String roleName) {
        Set<String> permissions = cacheUtils.getPermissions(roleName);
        if (permissions != null) {
            return permissions;
        }
        permissions = permissionMapper.selectPermissionsByRoleName(roleName);
        cacheUtils.cachePermissions(roleName, permissions);
        return permissions;
    }
}
