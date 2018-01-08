package win.scolia.cloud.sso.util.cache;

public interface CacheUtils<T> {

    /**
     * 缓存对象
     * @param key 键
     * @param target 缓存目标
     */
    void cache(String key, T target);

    /**
     * 获取缓存
     * @param key 键
     * @return 缓存对象/null
     */
    T get(String key);

    /**
     * 根据键删除缓存对象
     * @param key 键
     */
    void delete(String key);

    /**
     * 删除所有的缓存
     */
    void deleteAll();
}
