package win.scolia.cloud.sso.service.impl;

import com.github.pagehelper.PageInfo;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import win.scolia.cloud.sso.annotation.NotBlank;
import win.scolia.cloud.sso.bean.entity.Role;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.bean.entity.UserRole;
import win.scolia.cloud.sso.bean.entity.UserSafely;
import win.scolia.cloud.sso.dao.UserMapper;
import win.scolia.cloud.sso.dao.UserRoleMapper;
import win.scolia.cloud.sso.exception.*;
import win.scolia.cloud.sso.service.RoleService;
import win.scolia.cloud.sso.service.UserService;
import win.scolia.cloud.sso.util.EncryptUtils;
import win.scolia.cloud.sso.util.PageUtils;
import win.scolia.cloud.sso.util.cache.UserCacheUtils;
import win.scolia.cloud.sso.util.cache.UserRoleCacheUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserCacheUtils userCacheUtils;

    @Autowired
    private UserRoleCacheUtils userRoleCacheUtils;

    @Autowired
    private EncryptUtils encryptUtils;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public void createUser(@NotBlank String userName, @NotBlank String password) {
        User cache = userCacheUtils.get(userName);
        if (cache != null) {
            throw new DuplicateUserException(String.format("User already exist: %s", userName));
        }
        String salt = encryptUtils.getRandomSalt();
        User record = User.of(userName)
                .setPassword(encryptUtils.getEncryptedPassword(password, salt))
                .setSalt(salt)
                .forCreate();
        try {
            userMapper.insert(record);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserException(String.format("User already exist: %s", userName), e);
        }
    }

    @Transactional
    @Override
    public void removeUserByName(@NotBlank String userName) {
        User record = this.getUserByName(userName);
        if (record == null) {
            throw new MissUserException(String.format("User can not be found: %s", userName));
        }
        userMapper.delete(record);
        userRoleMapper.delete(
                UserRole.builder().userId(record.getUserId()).build()
        );
        userCacheUtils.delete(userName);
        userRoleCacheUtils.delete(userName);
    }

    @Override
    public void changePassword(@NotBlank String userName, @NotBlank String current, @NotBlank String target) {
        User record = this.getUserByName(userName);
        if (record != null) {
            String password = encryptUtils.getEncryptedPassword(current, record.getSalt());
            if (StringUtils.equals(password, record.getPassword())) {
                record = User.of(userName)
                        .setPassword(password)
                        .forUpdate();
                userMapper.updateByPrimaryKeySelective(record);
                userCacheUtils.delete(userName);
            } else {
                throw new AuthenticationException("Authentication fail");
            }
        }
        throw new MissUserException(String.format("User can not be found: %s", userName));
    }

    @Override
    public void changePasswordDirect(@NotBlank String userName, @NotBlank String password) {
        User record = this.getUserByName(userName);
        if (record == null) {
            throw new MissUserException(String.format("User not found: %s", userName));
        }
        User update = User.builder()
                .userId(record.getUserId())
                .password(encryptUtils.getEncryptedPassword(password, record.getSalt()))
                .build()
                .forUpdate();
        userMapper.updateByPrimaryKeySelective(update);
        userCacheUtils.delete(userName);
    }

    @Override
    public User getUserByName(@NotBlank String userName) {
        User query = User.of(userName);
        User record = userMapper.selectOne(query);
        if (record != null) {
            userCacheUtils.cache(userName, record);
        }
        return record;
    }

    @Override
    public PageInfo<UserSafely> listUsersSafely(@NonNull Integer pageNum) {
        pageUtils.startPage(pageNum);
        List<UserSafely> list = userMapper.selectAllUserSafely();
        return pageUtils.getPageInfo(list);
    }

    @Override
    public Set<String> getUserRoles(@NotBlank String userName) {
        Set<String> roles = userRoleCacheUtils.get(userName);
        if (roles == null) {
            roles = userRoleMapper.selectUserRolesByUserName(userName);
            userRoleCacheUtils.cache(userName, roles);
        }
        return roles;
    }

    @Override
    public Set<String> getUserPermissions(@NotBlank String userName) {
        Set<String> permissions = new HashSet<>();
        Set<String> roles = this.getUserRoles(userName);
        if (!CollectionUtils.isEmpty(roles)) {
            for (String roleName : roles) {
                permissions.addAll(roleService.getRolePermissions(roleName));
            }
        }
        return permissions;
    }

    @Override
    public void addUserRole(@NotBlank String userName, @NotBlank String roleName) {
        User user = this.getUserByName(userName);
        if (user == null) {
            throw new MissUserException(String.format("User can not be found: %s", userName));
        }
        Role role = roleService.getRoleByName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("Role can not be found: %s", roleName));
        }
        UserRole record = UserRole.of(user.getUserId(), role.getRoleId()).forCreate();
        try {
            userRoleMapper.insert(record);
            userRoleCacheUtils.delete(userName);
        } catch (DuplicateKeyException e) {
            throw new DuplicateRoleException(String.format("User already has role: %s", roleName));
        }

    }

    @Override
    public void removeUserRole(String userName, String roleName) {
        User user = this.getUserByName(userName);
        if (user == null) {
            throw new MissUserException(String.format("User can not be found: %s", userName));
        }
        Role role = roleService.getRoleByName(roleName);
        if (role == null) {
            throw new MissRoleException(String.format("Role can not be found: %s", roleName));
        }
        UserRole record = UserRole.of(user.getUserId(), role.getRoleId());
        userRoleMapper.deleteByPrimaryKey(record);
        userCacheUtils.delete(userName);
    }
}
