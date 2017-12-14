package win.scolia.sso.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.bean.vo.UserVO;
import win.scolia.sso.service.AccountService;
import win.scolia.sso.service.UserService;
import win.scolia.sso.util.EncryptUtils;
import win.scolia.sso.util.TokenUtils;

import java.util.Date;
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
    private TokenUtils tokenUtils;

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
    public String loginByPassword(String userName, String password) {
        User user = userService.getUserByUsername(userName);
        String tempPassword = encryptUtil.getEncryptedPassword(password, user.getSalt());
        if (StringUtils.equals(user.getPassword(), tempPassword)) {
            String token = tokenUtils.getNewToken(userName);
            tokenUtils.cacheToken(userName, token);
            return token;
        }
        return null;
    }

    @Override
    public Boolean loginByToken(String userName, String token) {
        String realToken = tokenUtils.getToken(userName);
        return StringUtils.equals(realToken, token);
    }

    @Override
    public void logout(String userName) {
        tokenUtils.clearToken(userName);
    }

    @Override
    public Set<String> getRoles(String userName) {
        return userService.getRolesByUserName(userName);
    }

    @Override
    public Set<String> getPermissions(String roleName) {
        return userService.getPermissionsByRoleName(roleName);
    }


}
