package win.scolia.cloud.sso.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.service.PermissionService;
import win.scolia.cloud.sso.service.RoleService;
import win.scolia.cloud.sso.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class MybatisRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 进行用户登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        User user = userService.getUserByUserName(userName);
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
        Set<String> roles = roleService.getUserRolesByUserName(user.getUserName());
        Set<String> permissions = new HashSet<>();
        for (String role : roles) {
            Set<String> perms = permissionService.getPermissionsByRoleName(role);
            permissions.addAll(perms);
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }
}