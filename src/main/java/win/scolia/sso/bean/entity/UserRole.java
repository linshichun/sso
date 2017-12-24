package win.scolia.sso.bean.entity;

import java.util.Date;

/**
 * 用户和角色的映射关系
 */
public class UserRole {

    private Long userId;

    private Long roleId;

    private Date createTime;

    private Date lastModified;

    public UserRole() {
    }

    public UserRole(Long userId, Long roleId, Date createTime, Date lastModified) {
        this.userId = userId;
        this.roleId = roleId;
        this.createTime = createTime;
        this.lastModified = lastModified;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", createTime=" + createTime +
                ", lastModified=" + lastModified +
                '}';
    }
}
