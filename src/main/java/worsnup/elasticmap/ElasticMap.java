package worsnup.elasticmap;

import java.util.*;

public class ElasticMap<K, V> implements Map<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_PROBE_LIMIT = 10;

    @SuppressWarnings("unchecked")
    private static <K, V> Entry<K, V>[] newArray(int length) {
        return (Entry<K, V>[]) new Entry[length];
    }

    private final int probeLimit;
    private final double maxLoadFactor;
    private Entry<K, V>[] table;
    private int size;
    private int threshold;

    public ElasticMap() {
        this.table = ElasticMap.newArray(INITIAL_CAPACITY);
        this.probeLimit = DEFAULT_PROBE_LIMIT;
        this.maxLoadFactor = DEFAULT_MAX_LOAD_FACTOR;
        this.size = 0;
        this.threshold = (int) (INITIAL_CAPACITY * this.maxLoadFactor);
    }

    public ElasticMap(int initialCapacity, double maxLoadFactor, int probeLimit) {
        this.table = ElasticMap.newArray(initialCapacity);
        this.probeLimit = probeLimit;
        this.maxLoadFactor = maxLoadFactor;
        this.size = 0;
        this.threshold = (int) (initialCapacity * maxLoadFactor);
    }

    private static class Entry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }
    }

    private static int log2(int bits) {
        int log = 0;
        if ((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if (bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if (bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if (bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        return log + (bits >>> 1);
    }

    @Override
    public V get(Object key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int mask = this.table.length - 1;
        int num_levels = ElasticMap.log2(this.table.length);
        for (int level = 1; level <= num_levels; level++) {
            for (int attempt = 0; attempt < this.probeLimit; attempt++) {
                int index = (hash + level * attempt) & mask;
                Entry<K, V> entry = this.table[index];
                if (entry != null && Objects.equals(key, entry.key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (this.size >= this.threshold) {
            this.resize();
        }
        int hash = (key == null) ? 0 : key.hashCode();
        int mask = this.table.length - 1;
        int num_levels = ElasticMap.log2(this.table.length);
        for (int level = 1; level <= num_levels; level++) {
            for (int attempt = 0; attempt < this.probeLimit; attempt++) {
                int index = (hash + level * attempt) & mask;
                Entry<K, V> entry = this.table[index];
                if (entry == null) {
                    this.table[index] = new Entry<>(key, value);
                    this.size++;
                    return null;
                } else if (Objects.equals(key, entry.key)) {
                    V oldValue = entry.value;
                    entry.value = value;
                    return oldValue;
                }
            }
        }
        throw new IllegalStateException("Failed to insert key due to high load.");
    }

    @Override
    public V remove(Object key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int mask = this.table.length - 1;
        int num_levels = ElasticMap.log2(this.table.length);
        for (int level = 1; level <= num_levels; level++) {
            for (int attempt = 0; attempt < this.probeLimit; attempt++) {
                int index = (hash + level * attempt) & mask;
                Entry<K, V> entry = this.table[index];
                if (entry != null && Objects.equals(key, entry.key)) {
                    V oldValue = entry.value;
                    this.table[index] = null;
                    this.size--;
                    return oldValue;
                }
            }
        }
        return null;
    }

    private void resize() {
        Entry<K, V>[] oldTable = this.table;
        int newCapacity = oldTable.length << 1;
        this.table = ElasticMap.newArray(newCapacity);
        this.threshold = (int) (newCapacity * this.maxLoadFactor);
        this.size = 0;

        for (Entry<K, V> entry : oldTable) {
            if (entry != null) {
                this.put(entry.key, entry.value);
            }
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> entry : this.table) {
            if (entry != null && Objects.equals(entry.value, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        Arrays.fill(this.table, null);
        this.size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Entry<K, V> entry : this.table) {
            if (entry != null) {
                keys.add(entry.key);
            }
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (Entry<K, V> entry : this.table) {
            if (entry != null) {
                values.add(entry.value);
            }
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return Arrays.stream(this.table).filter(Objects::nonNull).collect(HashSet::new, Set::add, Set::addAll);
    }
}
