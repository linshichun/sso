package win.scolia.sso.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.scolia.sso.bean.entity.User;
import win.scolia.sso.util.EncryptUtils;

/**
 * 密码比对器, 用于明文密码和密文密码的比对, 需要注入到相应的Realm中.
 * Created by scolia on 2017/9/29
 */
@Component
public class MyCredentialsMatcher implements CredentialsMatcher {

    @Autowired
    private EncryptUtils encryptUtils;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 真实的用户
        User realUser = (User) info.getPrincipals().getPrimaryPrincipal();
        // 用户提交的信息
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String rawPassword = new String(usernamePasswordToken.getPassword());
        // 加密
        String password = encryptUtils.getEncryptedPassword(rawPassword, realUser.getSalt());
        return StringUtils.equals(password, realUser.getPassword());
    }
}