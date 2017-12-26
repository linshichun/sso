package win.scolia.sso.bean.entity;

import java.util.Date;

/**
 * 角色和权限的映射关系
 */
public class RolePermission {

    private Long roleId;

    private Long permissionId;

    private Date createTime;

    private Date lastModified;

    public RolePermission() {
    }

    public RolePermission(Long roleId, Long permissionId) {
        this(roleId, permissionId, new Date(), new Date());
    }

    public RolePermission(Long roleId, Long permissionId, Date createTime, Date lastModified) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.createTime = createTime;
        this.lastModified = lastModified;
    }

    public RolePermission(Long roleId, Long permissionId, Date lastModified) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.lastModified = lastModified;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
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
}
