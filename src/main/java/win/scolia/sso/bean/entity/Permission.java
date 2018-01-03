package win.scolia.sso.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Permission extends BaseEntity {
    private static final long serialVersionUID = 6544068627532597615L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    private String permission;

    public Permission() {
    }

    public Permission(String permission) {
        this.permission = permission;
    }

    public Permission(Long permissionId, String permission) {
        this.permissionId = permissionId;
        this.permission = permission;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionId=" + permissionId +
                ", permission='" + permission + '\'' +
                "} " + super.toString();
    }
}
