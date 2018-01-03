package win.scolia.sso.util.cache;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class BaseCacheUtils<T> implements CacheUtils<T> {

    @Value("${scolia.sso.cache.prefix}")
    private String prefix;

    @Value("${scolia.sso.cache.expire}")
    private long expire;

    @Value("${scolia.sso.cache.flush-expire}")
    private boolean isFlush;

    protected String getCacheKey(String prefix, String key) {
        return String.format("%s:%s:%s", this.prefix.toUpperCase(), prefix.toUpperCase(), key.toUpperCase());
    }

    protected String getPrefix() {
        return prefix;
    }

    protected long getExpire() {
        return expire;
    }

    protected boolean isFlush() {
        return isFlush;
    }

    protected abstract String getSelfPrefix();

    protected abstract Logger getLogger();

    protected abstract RedisTemplate<String, T> getRedisTemplate();

    @Override
    public void cache(String key, T target) {
        if (target == null || StringUtils.isEmpty(key)) {
            return;
        }
        String cacheKey = this.getCacheKey(this.getSelfPrefix(), key);
        getRedisTemplate().opsForValue().set(cacheKey, target, this.getExpire(), TimeUnit.SECONDS);
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Cache: {}", cacheKey);
        }
    }

    @Override
    public T get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String cacheKey = this.getCacheKey(this.getSelfPrefix(), key);
        BoundValueOperations<String, T> operations = getRedisTemplate().boundValueOps(cacheKey);
        if (this.isFlush()) {
            operations.expire(this.getExpire(), TimeUnit.SECONDS);
        }
        T target = operations.get();
        if (this.getLogger().isDebugEnabled()) {
            if (target != null) {
                this.getLogger().debug("Hit: {}", cacheKey);
            } else {
                this.getLogger().debug("Miss: {}", cacheKey);
            }
        }
        return target;
    }

    @Override
    public void delete(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        String cacheKey = this.getCacheKey(this.getSelfPrefix(), key);
        getRedisTemplate().delete(cacheKey);
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Delete: {}", cacheKey);
        }
    }

    @Override
    public void deleteAll() {
        Set<String> keys = getRedisTemplate().keys(String.format("%s:%s:*", this.getPrefix(), this.getSelfPrefix()));
        getRedisTemplate().delete(keys);
        if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Delete all");
        }
    }
}
