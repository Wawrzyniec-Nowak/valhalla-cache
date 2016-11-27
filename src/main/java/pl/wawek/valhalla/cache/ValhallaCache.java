package pl.wawek.valhalla.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the valhalla cache.
 */
public class ValhallaCache {

    private ValhallaCache() {
    }

    /**
     * Helper class responsible for lazy loading the singleton instance of the cache.
     */
    private static class LazyLoader {
        private static final ValhallaCache INSTANCE = new ValhallaCache();
    }

    /**
     * Initializes the cache on demand.
     * @return the cache instance
     */
    public static ValhallaCache getInstance() {
        return LazyLoader.INSTANCE;
    }

    private ConcurrentHashMap<String, CachedObject> cache = new ConcurrentHashMap<>();

    /**
     * Checks if the key is in the cache.
     * @param key the key which should be checked in cache
     * @return true if the key exists in the cache, false otherwise
     */
    boolean contains(String key) {
        return cache.containsKey(key);
    }

    /**
     * Fetches the cached object from the cache
     * @param key the key for which the object is stored in cache
     * @return the cached object
     */
    Object get(String key) {
        return cache.get(key).getObject();
    }

    /**
     * Inserts the object which needs to be cached.
     * @param key the key under which the object will be stored
     * @param objectToCache value which should be stored
     * @param maxAge time in milliseconds for how long the value will be cached
     */
    void put(String key, Object objectToCache, long maxAge) {
        cache.put(key, new CachedObject(objectToCache, maxAge));
    }

    /**
     * Iterates over the cache and removes values which expired.
     */
    public void clearExpiredObjects() {
        cache.keySet().forEach(key -> {
            CachedObject cachedObject = cache.get(key);
            if (cachedObject.isExpired()) {
                cache.remove(key);
            }
        });
    }
}
