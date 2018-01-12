package win.scolia.cloud.sso.admin.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.service.UserService;

import java.util.Set;

@Component
public class SSORealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 进行用户登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        User user = userService.getUserByName(userName);
        if (user == null) {
            throw new UnknownAccountException(String.format("Can not find the user: %s", userName));
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    /**
     * 获取用户的角色和角色所对应的权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user  = (User) principals.getPrimaryPrincipal();
        Set<String> roles = userService.getUserRoles(user.getUserName());
        Set<String> permissions = userService.getUserPermissions(user.getUserName());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }
}