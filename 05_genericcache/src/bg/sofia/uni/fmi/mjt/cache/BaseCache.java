package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.enums.EvictionPolicy;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseCache<K, V>
        implements Cache<K, V> {
    static final long DEFAULT_MAX_CAPACITY = 10_000;
    Map<K, CacheData<K, V>> cache;
    private EvictionPolicy evictionPolicy;
    private long maxCapacity;
    private int successHits;
    private int failedHits;

    BaseCache(long maxCapacity, EvictionPolicy evictionPolicy) {
        this.cache = new HashMap<>();
        this.evictionPolicy = evictionPolicy;
        this.maxCapacity = maxCapacity;
        this.successHits = 0;
        this.failedHits = 0;
    }

    BaseCache(EvictionPolicy evictionPolicy) {
        this(DEFAULT_MAX_CAPACITY, evictionPolicy);
    }

    @Override
    public V get(K key) {
        CacheData<K, V> foundData = this.cache.get(key);
        if (foundData != null) {
            V returnValue = foundData.getValue();
            if (returnValue == null) {
                failedHits++;
            } else {
                foundData.increaseUsesCount();
                successHits++;
            }
            return returnValue;
        } else {
            failedHits++;
            return null;
        }
    }

    @Override
    public void set(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        if (this.isFull()) {
            this.removeItemByEvictionPolicy();
        }
        CacheData<K, V> toInsert = this.cache.get(key);
        if (toInsert != null) {
            toInsert.increaseUsesCount();
            toInsert.setValue(value);
        } else {
            toInsert = new CacheData<>(key, value);
            toInsert.increaseUsesCount();
        }
        this.cache.put(key, toInsert);
    }

    protected abstract void removeItemByEvictionPolicy();

    private boolean isFull() {
        return this.cache.size() >= this.maxCapacity;
    }

    @Override
    public boolean remove(K key) {
        return this.cache.remove(key) != null;
    }

    @Override
    public long size() {
        return this.cache.size();
    }

    @Override
    public void clear() {
        this.cache.clear();
        this.successHits = 0;
        this.failedHits = 0;
    }

    @Override
    public double getHitRate() {
        if (successHits == 0) {
            return 0;
        }
        if (this.failedHits == 0) {
            return 1.0;
        }
        double totalHits = (double) this.successHits + this.failedHits;
        return this.successHits / totalHits;
    }

    @Override
    public long getUsesCount(K key) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "Parameter \"key\"   should not be null.");
        }
        CacheData<K, V> foundData = this.cache.get(key);
        if (foundData == null) {
            return 0;
        }
        return foundData
                .getUsesCount();
    }

    public EvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }
}
