package bg.sofia.uni.fmi.mjt.cache;

class CacheData<K, V> {
    private K key;
    private V value;
    private int usesCount;

    CacheData(K key, V value) {
        this.key = key;
        this.value = value;
        this.usesCount = 0;
    }

    K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    V getValue() {
        return value;
    }

    void setValue(V value) {
        this.value = value;
    }

    int getUsesCount() {
        return usesCount;
    }

    void increaseUsesCount() {
        this.usesCount++;
    }
}
