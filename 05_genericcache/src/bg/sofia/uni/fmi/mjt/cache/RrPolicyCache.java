package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.enums.EvictionPolicy;

public class RrPolicyCache<K, V> extends BaseCache<K, V> {

    public RrPolicyCache() {
        super(EvictionPolicy.RANDOM_REPLACEMENT);
    }

    public RrPolicyCache(long capacity) {
        super(capacity, EvictionPolicy.RANDOM_REPLACEMENT);
    }

    @Override
    protected void removeItemByEvictionPolicy() {
    /* Since the underlying implementation is a HashMap,
       it's in no particular order, hence just getting
       the first item is "random" enough. */
        K toRemove = this.cache
                .keySet()
                .iterator()
                .next();
        this.cache.remove(toRemove);
    }

    @Override
    public long getUsesCount(K key) {
        throw new UnsupportedOperationException();
    }
}
