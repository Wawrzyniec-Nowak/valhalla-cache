package pl.wawek.valhalla.cache;

/**
 * Plain Java object which represents the value in the cache
 */
class CachedObject {

    private long usageTimestamp;
    private Object object;
    private long maxAge;

    /**
     * Creates the object
     * @param objectToCache value which should be stored in cache
     * @param maxAge max time which value should be cached
     */
    CachedObject(Object objectToCache, long maxAge) {
        this.usageTimestamp = System.currentTimeMillis();
        this.object = objectToCache;
        this.maxAge = maxAge;
    }

    /**
     * Checks if cached value expired
     * @return true if the time from last usage extended the maximum age of the value
     */
    boolean isExpired() {
        return System.currentTimeMillis() - usageTimestamp > maxAge;
    }

    /**
     * Returns the cached value and updates the last usage time
     * @return cached value
     */
    Object getObject() {
        this.usageTimestamp = System.currentTimeMillis();
        return object;
    }
}
