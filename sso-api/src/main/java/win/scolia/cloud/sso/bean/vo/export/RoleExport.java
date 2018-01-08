package win.scolia.cloud.sso.bean.vo.export;

import win.scolia.cloud.sso.bean.entity.Role;

import java.util.Set;

public class RoleExport {

    private Role role;

    private Set<String> permissions;

    public RoleExport(Role role, Set<String> permissions) {
        this.role = role;
        this.permissions = permissions;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
