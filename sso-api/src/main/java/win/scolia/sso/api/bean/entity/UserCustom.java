package win.scolia.sso.api.bean.entity;

import java.util.List;
import java.util.Set;

/**
 * Created by scolia on 2017/11/29
 */
public class UserCustom extends User {

    private String cacheKey;

    private String token;

    private List<String> roles;

    private Set<String> permissions;

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }


}
