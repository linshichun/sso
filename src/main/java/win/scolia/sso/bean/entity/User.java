package win.scolia.sso.bean.entity;

import java.io.Serializable;

/**
 * 用户详细信息
 */
public class User extends UserSafely implements Serializable {
    private static final long serialVersionUID = -3188105297711919345L;

    private String password;

    private String salt;

    public User() {
    }

    public User(String userName) {
        super.setUserName(userName);
    }

    public User(Long userId, String password) {
        super.setUserId(userId);
        this.password = password;
    }

    public User(String userName, String password, String salt) {
        this(userName);
        this.password = password;
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                "} " + super.toString();
    }
}
