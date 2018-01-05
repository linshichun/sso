package win.scolia.sso.bean.vo.entry;

public class RoleEntry {

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleEntry{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}
