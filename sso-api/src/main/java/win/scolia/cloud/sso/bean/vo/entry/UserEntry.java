package win.scolia.cloud.sso.bean.vo.entry;

import javax.validation.constraints.NotNull;

/**
 * 用户信息的输入VO对象
 */
public class UserEntry {

    public interface Register {}

    public interface UserName {}

    public interface Login {}

    public interface UpdatePassword {}

    @NotNull(message = "用户名不能为空", groups = {Register.class, Login.class, UserName.class})
    private String userName;

    @NotNull(message = "密码不能为空", groups = {Register.class, Login.class, UpdatePassword.class})
    private String password;

    private Boolean rememberMe = false;

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

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}



