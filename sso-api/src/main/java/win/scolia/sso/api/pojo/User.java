package win.scolia.sso.api.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by scolia on 2017/11/27
 */
public class User implements Serializable {

    private static final long serialVersionUID = -2589045871735773540L;

    private Integer uId;

    private String username;

    private String password;

    private String salt;

    private Date createTime;

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
