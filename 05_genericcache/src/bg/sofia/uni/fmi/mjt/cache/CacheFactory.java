package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.enums.EvictionPolicy;

public interface CacheFactory {
    /**
     * Constructs a new Cache<K, V> with the specified maximum capacity and eviction policy
     *
     * @throws IllegalArgumentException if the given capacity is less than or equal to zero
     */
    static <K, V> Cache<K, V> getInstance(long capacity,
                                          EvictionPolicy policy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        if (policy == EvictionPolicy.LEAST_FREQUENTLY_USED) {
            return new LfuPolicyCache<>(capacity);
        } else if (policy == EvictionPolicy.RANDOM_REPLACEMENT) {
            return new RrPolicyCache<>(capacity);
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Constructs a new Cache<K, V> with maximum capacity
     * of 10_000 items and the specified eviction policy
     */
    static <K, V> Cache<K, V> getInstance(EvictionPolicy policy) {
        return getInstance(BaseCache.DEFAULT_MAX_CAPACITY, policy);
    }
}