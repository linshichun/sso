package win.scolia.sso.bean.entity;

import java.io.Serializable;

/**
 * 用户详细信息
 */
public class User extends UserSafely implements Serializable {

    private static final long serialVersionUID = -3188105297711919345L;

    private String password;

    private String salt;

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
