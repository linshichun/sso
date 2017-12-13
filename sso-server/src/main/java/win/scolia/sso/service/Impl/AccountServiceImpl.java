package win.scolia.sso.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import win.scolia.sso.api.bean.entity.User;
import win.scolia.sso.api.bean.vo.UserVO;
import win.scolia.sso.api.server.AccountService;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.CacheUtils;
import win.scolia.sso.util.EncryptUtils;
import win.scolia.sso.util.TokenUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by scolia on 2017/11/27
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private EncryptUtils encryptUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheUtils cacheUtils;

    @Override
    public Long register(UserVO userVO) {
        User user = new User();
        user.setUserName(userVO.getUserName());
        user.setSalt(encryptUtil.getRandomSalt());
        user.setPassword(encryptUtil.getEncryptedPassword(userVO.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        user.setLastModified(new Date());
        userService.createUser(user);
        return user.getUserId();
    }

    @Override
    public String login(UserVO userVO) {
        User user = userService.getUserByUsername(userVO.getUserName());
        String tempPassword = encryptUtil.getEncryptedPassword(userVO.getPassword(), user.getSalt());
        if (StringUtils.equals(user.getPassword(), tempPassword)) {
            String token = TokenUtils.getToken(userVO.getUserName());
            return token;
        }
        return null;
    }

    @Override
    public String login(String token) {
        return null;
    }

    @Override
    public void logout(UserVO userVO) {

    }

    @Override
    public void logout(String token) {

    }

    @Override
    public Set<String> getRoles(UserVO userVO) {
        String userName = userVO.getUserName();
        return userService.getRolesByUserName(userName);
    }

    @Override
    public Set<String> getRoles(String token) {
        return null;
    }

    @Override
    public Set<String> getPermissions(UserVO userVO) {
        Set<String> roles = this.getRoles(userVO);
        Set<String> permissions = new HashSet<>();
        for (String role : roles) {
            Set<String> permission = userService.getPermissionsByRoleName(role);
            permissions.addAll(permission);
        }
        return permissions;
    }

    @Override
    public Set<String> getPermissions(String token) {
        return null;
    }
}
