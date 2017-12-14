package win.scolia.sso.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * 用户的传输对象
 */
public class UserVO {

    // 注册组
    public interface Register {}

    // 密码登录组
    public interface LoginByPassword {}

    // token登录zu
    public interface LoginByToken {}

    @NotNull(message = "用户名不能为空", groups = {Register.class, LoginByPassword.class, LoginByToken.class})
    private String userName;

    @NotNull(message = "密码不能为空", groups = {Register.class, LoginByPassword.class})
    private String password;

    @NotNull(message = "token不能为空", groups = {LoginByToken.class})
    private String token;

    public UserVO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}



