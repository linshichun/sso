package win.scolia.sso.bean.vo.export;

public class AuthenticationExport {

    private String authentication;

    public AuthenticationExport() {
    }

    public AuthenticationExport(String authentication) {
        this.authentication = authentication;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        return "AuthenticationExport{" +
                "authentication='" + authentication + '\'' +
                '}';
    }
}
