package win.scolia.cloud.sso.bean.entity;

import javax.persistence.Id;

/**
 * 用户和角色的映射关系
 */
public class UserRole extends BaseEntity {
    private static final long serialVersionUID = 7108259844144855041L;

    @Id
    private Long userId;

    @Id
    private Long roleId;

    public UserRole() {
    }

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
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

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                "} " + super.toString();
    }
}
