package win.scolia.sso.bean.vo.export;

import win.scolia.sso.bean.entity.UserSafely;

import java.util.Set;

/**
 * 用户当前信息
 */
public class UserExport {

    private UserSafely user;

    private Set<String> roles;

    private Set<String> permissions;

    public UserExport() {
    }

    public UserExport(UserSafely user, Set<String> roles, Set<String> permissions) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
    }

    public UserSafely getUser() {
        return user;
    }

    public void setUser(UserSafely user) {
        this.user = user;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
