package win.scolia.sso.api.bean.vo;

import java.io.Serializable;

/**
 * 用户的传输对象
 */
public class UserVO implements Serializable{

    private static final long serialVersionUID = -7331594504420320333L;

    private String userName;

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
