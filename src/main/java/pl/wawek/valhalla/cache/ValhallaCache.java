package pl.wawek.valhalla.cache;

import pl.wawek.valhalla.cache.algorithm.AlgorithmType;
import pl.wawek.valhalla.cache.algorithm.CacheEviction;
import pl.wawek.valhalla.cache.algorithm.LRUEviction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the valhalla cache.
 */
class ValhallaCache {

    private static final int CAPACITY = 1000;

    private CacheEviction eviction;

    private ValhallaCache() {
    }

    /**
     * Helper class responsible for lazy loading the singleton instance of the cache.
     */
    private static class LazyLoader {
        private static final ValhallaCache INSTANCE = new ValhallaCache();
    }

    /**
     * Initializes the cache on demand and sets the eviction algorithm.
     *
     * @param algorithm eviction algorithm
     * @return the cache instance
     */
    public static ValhallaCache getInstance(AlgorithmType algorithm) {
        ValhallaCache instance = LazyLoader.INSTANCE;
        switch (algorithm) {
            case LRU:
                instance.eviction = new LRUEviction();
                break;
            default:
                instance.eviction = new LRUEviction();
                break;
        }
        return instance;
    }

    private ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    /**
     * Checks if the key is in the cache.
     *
     * @param key the key which should be checked in cache
     * @return true if the key exists in the cache, false otherwise
     */
    boolean contains(String key) {
        return cache.containsKey(key);
    }

    /**
     * Fetches the cached object from the cache
     *
     * @param key the key for which the object is stored in cache
     * @return the cached object
     */
    Object get(String key) {
        return cache.get(key).getObject();
    }

    /**
     * Inserts the object which needs to be cached. If the size of the cache
     * has already exceeded the maximum capacity thn eviction algorithm is run.
     *
     * @param key           the key under which the object will be stored
     * @param objectToCache value which should be stored
     */
    void put(String key, Object objectToCache) {
        while (cache.size() >= CAPACITY) {
            List<CacheEntry> evicted = eviction.evict(cache.values());
            evicted.forEach(this::removeValue);
        }
        cache.put(key, new CacheEntry(objectToCache));
    }

    private void removeValue(CacheEntry entry) {
        for (Map.Entry<String, CacheEntry> mapEntry : cache.entrySet()) {
            if (mapEntry.getValue().equals(entry)) {
                cache.remove(mapEntry.getKey());
                break;
            }
        }
    }
}
