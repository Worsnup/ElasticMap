package worsnup.elasticmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ElasticMapTest {
    private ElasticMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        this.map = new ElasticMap<>();
    }

    @Test
    void testPutAndGet() {
        this.map.put("one", 1);
        this.map.put("two", 2);
        this.map.put("three", 3);

        assertEquals(1, this.map.get("one"));
        assertEquals(2, this.map.get("two"));
        assertEquals(3, this.map.get("three"));
    }

    @Test
    @SuppressWarnings("OverwrittenKey")
    void testPutOverwrite() {
        this.map.put("one", 1);
        this.map.put("one", 10);

        assertEquals(10, this.map.get("one"));
    }

    @Test
    void testRemove() {
        this.map.put("one", 1);
        this.map.put("two", 2);

        assertEquals(1, this.map.remove("one"));
        assertNull(this.map.get("one"));
        assertEquals(2, this.map.get("two"));
    }

    @Test
    void testSize() {
        assertEquals(0, this.map.size());
        this.map.put("one", 1);
        assertEquals(1, this.map.size());
        this.map.put("two", 2);
        assertEquals(2, this.map.size());
        this.map.remove("one");
        assertEquals(1, this.map.size());
    }

    @Test
    @SuppressWarnings("ConstantValue")
    void testIsEmpty() {
        assertTrue(this.map.isEmpty());
        this.map.put("one", 1);
        assertFalse(this.map.isEmpty());
    }

    @Test
    void testContainsKey() {
        this.map.put("one", 1);
        assertTrue(this.map.containsKey("one"));
        assertFalse(this.map.containsKey("two"));
    }

    @Test
    void testContainsValue() {
        this.map.put("one", 1);
        assertTrue(this.map.containsValue(1));
        assertFalse(this.map.containsValue(2));
    }

    @Test
    @SuppressWarnings({"ConstantValue", "RedundantOperationOnEmptyContainer"})
    void testClear() {
        this.map.put("one", 1);
        this.map.put("two", 2);
        this.map.clear();
        assertTrue(this.map.isEmpty());
        assertNull(this.map.get("one"));
        assertNull(this.map.get("two"));
    }

    @Test
    @SuppressWarnings("RedundantCollectionOperation")
    void testKeySet() {
        this.map.put("one", 1);
        this.map.put("two", 2);
        assertTrue(this.map.keySet().contains("one"));
        assertTrue(this.map.keySet().contains("two"));
    }

    @Test
    @SuppressWarnings("RedundantCollectionOperation")
    void testValues() {
        this.map.put("one", 1);
        this.map.put("two", 2);
        assertTrue(this.map.values().contains(1));
        assertTrue(this.map.values().contains(2));
    }

    @Test
    @SuppressWarnings("RedundantCollectionOperation")
    void testEntrySet() {
        this.map.put("one", 1);
        this.map.put("two", 2);
        assertEquals(2, this.map.entrySet().size());
    }

    @Test
    void testResize() {
        for (int i = 0; i < 20; i++) {
            this.map.put("key" + i, i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(i, this.map.get("key" + i));
        }
    }

    @Test
    void testPutAll() {
        Map<String, Integer> otherMap = Map.of("one", 1, "two", 2, "three", 3);
        this.map.putAll(otherMap);
        assertEquals(1, this.map.get("one"));
        assertEquals(2, this.map.get("two"));
        assertEquals(3, this.map.get("three"));
    }
}