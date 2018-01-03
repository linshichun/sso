package win.scolia.sso.bean.entity;

import javax.persistence.Id;

/**
 * 角色和权限的映射关系
 */
public class RolePermission extends BaseEntity {
    private static final long serialVersionUID = -4885042955282179774L;

    @Id
    private Long roleId;

    @Id
    private Long permissionId;

    public RolePermission() {
    }

    public RolePermission(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
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

    @Override
    public String toString() {
        return "RolePermission{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                "} " + super.toString();
    }
}
