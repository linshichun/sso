package win.scolia.sso.api.pojo;

/**
 * Created by scolia on 2017/11/29
 */
public class UserCustom extends User {

    private String cacheKey;

    private String token;

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
}
