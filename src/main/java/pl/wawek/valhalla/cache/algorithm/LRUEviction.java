package pl.wawek.valhalla.cache.algorithm;

import pl.wawek.valhalla.cache.CacheEntry;

import java.util.*;

/**
 * Implements LRU eviction algorithms
 */
public class LRUEviction implements CacheEviction {

    private Comparator<CacheEntry> cacheEntryComparator
            = (CacheEntry o1, CacheEntry o2) ->
            Long.valueOf(o2.getUsageTimestamp() - o1.getUsageTimestamp()).intValue();

    /**
     * Evicts the last reused entries. For this moment only the last one.
     * @param cachedEntries list of cached entries
     * @return list of evicted entries
     */
    @Override
    public synchronized List<CacheEntry> evict(Collection<CacheEntry> cachedEntries) {
        ArrayList<CacheEntry> cachedEntriesList = new ArrayList<>(cachedEntries);
        cachedEntriesList.sort(cacheEntryComparator);
        // remove only the last one
        int lastIndex = cachedEntries.size() - 1;
        return Collections.singletonList(cachedEntriesList.get(lastIndex));
    }

}
