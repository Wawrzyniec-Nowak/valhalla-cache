package pl.wawek.valhalla.cache.evict;

import pl.wawek.valhalla.cache.CacheEntry;

import java.util.Collection;
import java.util.List;

/**
 * Interface for all eviction algorithms
 */
public interface CacheEviction {

    /**
     * Chooses the entries to evict from the @see cacheEntries
     * @param cacheEntries cached entries from which some will be evicted
     * @return evicted entries
     */
    List<CacheEntry> evict(Collection<CacheEntry> cacheEntries);
}
