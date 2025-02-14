# ElasticMap
Implementation of elastic hashing a la Krapivin et al.

### Paper
https://arxiv.org/abs/2501.02305


### Benchmarks on Large Map
```
Benchmark                                (mapSize)  (probeLimit)  Mode  Cnt         Score         Error  Units
ElasticMapBenchmark.elasticMapComposite    1048576            15  avgt    5  21756606.489    932414.410  ns/op
ElasticMapBenchmark.hashMapComposite       1048576            15  avgt    5  36257743.073   9559568.572  ns/op
```
