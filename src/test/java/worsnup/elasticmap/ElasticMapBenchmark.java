package worsnup.elasticmap;

import org.openjdk.jmh.annotations.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class ElasticMapBenchmark {

    @Param({"32", "1024", "1048576"})
    public int mapSize;

    @Param({"1", "5", "15"})
    public int probeLimit;

    public int capacity;

    @Setup(Level.Trial)
    public void setup() {
        this.capacity = 16;
        while (this.capacity < this.mapSize) {
            this.capacity <<= 1;
        }
    }

    @Benchmark
    public void elasticMapComposite() {
        ElasticMap<Integer, Integer> map = new ElasticMap<>(this.capacity, 1.0, this.probeLimit);
        for (int i = 0; i < this.mapSize; i++) {
            map.put(i, i);
        }
        for (int i = 0; i < this.mapSize; i++) {
            map.get(i);
        }
        for (int i = 0; i < this.mapSize; i++) {
            map.remove(i);
        }
    }

    @Benchmark
    public void hashMapComposite() {
        Map<Integer, Integer> map = new HashMap<>(this.mapSize);
        for (int i = 0; i < this.mapSize; i++) {
            map.put(i, i);
        }
        for (int i = 0; i < this.mapSize; i++) {
            map.get(i);
        }
        for (int i = 0; i < this.mapSize; i++) {
            map.remove(i);
        }
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
