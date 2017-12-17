package win.scolia.sso.service.Impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.vo.UserVO;
import win.scolia.sso.dao.PermissionMapper;
import win.scolia.sso.dao.RoleMapper;
import win.scolia.sso.dao.UserMapper;
import win.scolia.sso.exception.DuplicateUserException;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.EncryptUtils;

import java.util.Date;
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

    @Autowired
    private EncryptUtils encryptUtils;


    @Override
    public void createUser(UserVO userVO) {
        User cacheUser = getUserByUsername(userVO.getUserName());
        if (cacheUser != null) {
            throw new DuplicateUserException("该用户已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        user.setSalt(encryptUtils.getRandomSalt());
        user.setPassword(encryptUtils.getEncryptedPassword(user.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        user.setLastModified(new Date());
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
