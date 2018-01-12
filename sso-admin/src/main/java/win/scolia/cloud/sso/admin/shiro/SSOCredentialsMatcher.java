package win.scolia.cloud.sso.admin.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.cloud.sso.bean.entity.User;
import win.scolia.cloud.sso.util.EncryptUtils;

/**
 * 密码比对器, 用于明文密码和密文密码的比对, 需要注入到相应的Realm中.
 * Created by scolia on 2017/9/29
 */
@Component
public class SSOCredentialsMatcher implements CredentialsMatcher {

    @Autowired
    private EncryptUtils encryptUtils;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info) {
        // 真实的用户
        User user = (User) info.getPrincipals().getPrimaryPrincipal();
        String password = (String) info.getCredentials();
        // 用户提交的信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String rawPassword = new String(token.getPassword());
        // 加密
        String tempPassword = encryptUtils.getEncryptedPassword(rawPassword, user.getSalt());
        return StringUtils.equals(password, tempPassword);
    }
}