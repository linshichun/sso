package win.scolia.sso.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 用户信息, 不包含敏感信息
 */
public class UserSafely extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 5395248029913469424L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserSafely{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
