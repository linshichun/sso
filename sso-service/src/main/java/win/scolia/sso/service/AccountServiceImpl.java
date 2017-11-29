package win.scolia.sso.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.sso.api.pojo.User;
import win.scolia.sso.api.pojo.UserCustom;
import win.scolia.sso.api.service.AccountService;
import win.scolia.sso.mapper.AccountMapper;
import win.scolia.sso.utils.CacheUtils;
import win.scolia.sso.utils.EncryptUtil;
import win.scolia.sso.utils.TokenUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by scolia on 2017/11/27
 */
@Component
@Service
public class AccountServiceImpl implements AccountService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private EncryptUtil encryptUtil;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public Integer register(User user) {
        try {
            String selfSalt = encryptUtil.getRandomSalt();
            user.setSalt(selfSalt);
            String rowPassword = user.getPassword();
            String encryptedPassword = encryptUtil.getEncryptedPassword(rowPassword, user.getSalt());
            user.setPassword(encryptedPassword);
            return accountMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public String login(String username, String password) {
        User user = accountMapper.selectPasswordAndSaltByUsername(username);
        if (user != null) {
            String tempPassword = encryptUtil.getEncryptedPassword(password, user.getSalt());
            if (tempPassword.equals(user.getPassword())) {
                String token = tokenUtils.getToken(username);
                cacheUtils.cacheUser(user, token);
                return token;
            }
        }
        return null;
    }

    @Override
    public String login(String token) {
        UserCustom userCustom = cacheUtils.getUser(token);
        String username = null;
        if (userCustom != null) {
            username = userCustom.getUsername();
        }
        return username;
    }

    @Override
    public void logout(String token) {
        cacheUtils.delCacheUser(token);
    }

    @Override
    public List<String> getRoles(String token) {
        List<String> roles = cacheUtils.getRoles(token);
        if (roles != null) {
            return roles;
        }
        // 因为这是通过token获取的, 所以token找不到用户对象的话, 角色也就不能获取了
        UserCustom userCustom = cacheUtils.getUser(token);
        if (userCustom != null) {
            roles = accountMapper.selectRolesByUserName(userCustom.getUsername());
            cacheUtils.cacheUserRoles(userCustom, roles);
        }
        return roles;
    }

    @Override
    public Set<String> getPermissions(String token) {
        Set<String> permissions = cacheUtils.getPermissions(token);
        if (permissions != null) {
            return permissions;
        }
        // 同上, 必须token是有效的, 否则返回null
        UserCustom userCustom = cacheUtils.getUser(token);
        if (userCustom != null) {
            permissions = new HashSet<>();
            // 如果token是有效的, roles不会得到null, 但如果刚好过期, 就得到null了, 此时只能返回null
            List<String> roles = getRoles(token);
            if (roles != null) {
                for (String role : roles) {
                    List<String> permissionList = accountMapper.selectPermissionsByRoleName(role);
                    permissions.addAll(permissionList);
                }
                cacheUtils.cacheUserPermissions(userCustom, permissions);
            }
        }
        return permissions;
    }
}
