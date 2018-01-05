package win.scolia.sso.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sso")
public class SSOProperties {

    private Register register;

    private Encrypt encrypt;

    private Cache cache;

    private Cookie cookie;

    private Page page;

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public Encrypt getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Encrypt encrypt) {
        this.encrypt = encrypt;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }



    /**
     * 注册相关设置
     */
    public static class Register {
        private boolean enable = true;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    /**
     * 加密配置
     */
    public static class Encrypt {
        private String salt;

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }
    }

    /**
     * 缓存配置
     */
    public static class Cache{
        private String prefix = "SSO";
        private int expire = 2592000;
        private boolean flushExpire = true;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public boolean isFlushExpire() {
            return flushExpire;
        }

        public void setFlushExpire(boolean flushExpire) {
            this.flushExpire = flushExpire;
        }
    }

    /**
     * cookie配置
     */
    public static class Cookie {
        private int maxAge = 1209600;

        public int getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(int maxAge) {
            this.maxAge = maxAge;
        }
    }

    /**
     * 分页配置
     */
    public static class Page {
        private int size = 10;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}

