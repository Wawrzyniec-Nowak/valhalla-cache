package pl.wawek.valhalla.cache;

import java.util.UUID;

/**
 * Plain Java object which represents the value in the cache
 */
public class CacheEntry {

    private UUID id;
    private long usageTimestamp;
    private Object object;

    /**
     * Creates the object
     *
     * @param objectToCache value which should be stored in cache
     */
    CacheEntry(Object objectToCache) {
        id = UUID.randomUUID();
        this.usageTimestamp = System.currentTimeMillis();
        this.object = objectToCache;
    }

    /**
     * Returns the cached value and updates the last usage time
     *
     * @return cached value
     */
    Object getObject() {
        this.usageTimestamp = System.currentTimeMillis();
        return object;
    }

    public long getUsageTimestamp() {
        return usageTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheEntry that = (CacheEntry) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
