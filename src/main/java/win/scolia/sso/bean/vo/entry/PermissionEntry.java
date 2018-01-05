package win.scolia.sso.bean.vo.entry;

public class PermissionEntry {

    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "PermissionEntry{" +
                "permission='" + permission + '\'' +
                '}';
    }
}
