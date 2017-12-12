package win.scolia.sso.api.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * 用户的传输对象
 */
public class UserVO {

    // 注册组
    public interface register {}

    // 登录组
    public interface login {}

    @NotNull(message = "用户名不能为空", groups = {register.class, login.class})
    private String userName;

    @NotNull(message = "密码不能为空", groups = {register.class, login.class})
    private String password;

    public UserVO() {
    }

    public UserVO(String userName, String password) {
        this.userName = userName;
        this.password = password;
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

    @Override
    public String toString() {
        return "UserVO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}



