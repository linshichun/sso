package win.scolia.sso.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import win.scolia.sso.api.bean.entity.User;
import win.scolia.sso.api.bean.vo.UserVO;
import win.scolia.sso.api.server.AccountService;
import win.scolia.sso.dao.AccountMapper;
import win.scolia.sso.util.EncryptUtils;
import win.scolia.sso.util.TokenUtils;

import java.util.*;

/**
 * Created by scolia on 2017/11/27
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private EncryptUtils encryptUtil;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long register(UserVO userVO) {
        User user = new User();
        user.setUsername(userVO.getUserName());
        user.setSalt(encryptUtil.getRandomSalt());
        user.setPassword(encryptUtil.getEncryptedPassword(userVO.getPassword(), user.getSalt()));
        user.setCreateTime(new Date());
        user.setLastModified(new Date());
        accountMapper.insertUser(user);
        return user.getUserId();
    }

    @Override
    public String login(UserVO userVO) {
        User user = accountMapper.selectPasswordAndSaltByUsername(userVO.getUserName());
        String tempPassword = encryptUtil.getEncryptedPassword(userVO.getPassword(), user.getSalt());
        if (StringUtils.equals(user.getPassword(), tempPassword)) {
            return TokenUtils.getToken(userVO.getUserName());
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
        return accountMapper.selectRolesByUserName(userName);
    }

    @Override
    public Set<String> getRoles(String token) {
        return null;
    }

    @Override
    public Set<String> getPermissions(UserVO userVO) {
        Set<String> roles = accountMapper.selectRolesByUserName(userVO.getUserName());
        Set<String> permissions = new HashSet<>();
        for (String role : roles) {
            Set<String> permission = accountMapper.selectPermissionsByRoleName(role);
            permissions.addAll(permission);
        }
        return permissions;
    }

    @Override
    public Set<String> getPermissions(String token) {
        return null;
    }
}
