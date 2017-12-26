package win.scolia.sso.bean.entity;

import java.util.Date;

public class Role {

    private Long roleId;

    private String roleName;

    private Date createTime;

    private Date lastModified;

    public Role() {
    }

    public Role(String roleName, Date lastModified) {
        this.roleName = roleName;
        this.lastModified = lastModified;
    }

    public Role(String roleName, Date createTime, Date lastModified) {
        this.roleName = roleName;
        this.createTime = createTime;
        this.lastModified = lastModified;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", createTime=" + createTime +
                ", lastModified=" + lastModified +
                '}';
    }
}
