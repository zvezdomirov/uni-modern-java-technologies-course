package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.enums.EvictionPolicy;
import java.util.Comparator;

public class LfuPolicyCache<K, V> extends BaseCache<K, V> {
    public LfuPolicyCache() {
        super(EvictionPolicy.LEAST_FREQUENTLY_USED);
    }

    public LfuPolicyCache(long capacity) {
        super(capacity, EvictionPolicy.LEAST_FREQUENTLY_USED);
    }

    @Override
    protected void removeItemByEvictionPolicy() {
        this.cache.values().stream()
                .min(Comparator.comparingInt(CacheData::getUsesCount))
                .ifPresent(toRemove -> this.cache.remove(toRemove.getKey()));
    }
}
