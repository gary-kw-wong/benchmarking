### Background

As inspired by the article and the sample code prepared by Jainam M (captioned in "Reference" section) to demonstrate the -ve impact of **false sharing**, this sample codes is implemented to see if Java could also simulate the similar impact. 

### Testing Scenarios

There are 2 classes prepared in result for simulating and benchmarking the impact on "false sharing":

- BenchmarkFalseSharingImpact

- BenchmarkFalseSharingImpactV2

- BenchmarkFalseSharingImpactV3

BenchmarkFalseSharingImpact is basically the same implementation as of the sample codes provided by Jainam M, but it is in Java instead of written in C++.

BenchmarkFalseSharingImpactV2 is a slightly revised version of BenchmarkFalseSharingImpact. The reason of having it will be explained in deatils at **Conclusion** section below. 

BenchmarkFalseSharingImpactV3 is basically the same as BenchmarkFalseSharingImpactV2, but it is trying to demonstrate the impact on performance if there are more threads (4 threds) executing in parallel.

#### BenchmarkFalseSharingImpact Benchmark Stat

```
Benchmark                                                              Mode  Cnt        Score       Error  Units
BenchmarkFalseSharingImpact.testParallelProcessingWithFalseSharing     avgt    5  8557200.755 ± 97018.328  ns/op
BenchmarkFalseSharingImpact.testParallelProcessingWithoutFalseSharing  avgt    5  6626967.327 ± 97502.589  ns/op
BenchmarkFalseSharingImpact.testSerialProcessing                       avgt    5        2.096 ±     0.163  ns/op
```


#### BenchmarkFalseSharingImpactV2 Benchmark Stat

```
Benchmark                                                                Mode  Cnt          Score         Error  Units
BenchmarkFalseSharingImpactV2.testParallelProcessingWithFalseSharing     avgt    5  187011428.613 ± 7335189.457  ns/op
BenchmarkFalseSharingImpactV2.testParallelProcessingWithoutFalseSharing  avgt    5  175861751.228 ± 1980656.238  ns/op
BenchmarkFalseSharingImpactV2.testSerialProcessing                       avgt    5  344247214.667 ± 5382532.060  ns/op
```

#### BenchmarkFalseSharingImpactV3 Benchmark Stat

```
Benchmark                                                                Mode  Cnt          Score          Error  Units
BenchmarkFalseSharingImpactV3.testParallelProcessingWithFalseSharing     avgt    5  222052647.512 ± 44548674.211  ns/op
BenchmarkFalseSharingImpactV3.testParallelProcessingWithoutFalseSharing  avgt    5  191305261.299 ±  6237309.575  ns/op
BenchmarkFalseSharingImpactV3.testSerialProcessing                       avgt    5  690331698.667 ± 15074727.313  ns/op
```

### Conclusion

From the above statistics, we can see that the result in **BenchmarkFalseSharingImpact** is interesting. It is a bit out of my expectation, as which doesn't align to the pattern provided by Jainam M.

The statistic of **SerialProcessing** is probably due to the predictor built in modern CPU which helps to predict and pre-calculate the result of next step in an iterator.

2.096 ±     0.163  ns/op is a very ideal and perfect case, but it is not realistic to most enterprise application.

In order to make the result more realistic, **BenchmarkFalseSharingImpactV2** was implemented by changing the iteration in _expensiveProcessing(int index)_ **less predictable**.

From the benchmark result of this V2 class, the result of "serial processing" looks more make sense. However, the result of "ParallelProcessingWithFalseSharing" is not that worst, although it is still worst than "ParallelProcessingWithoutFalseSharing".

I could not figure out a reason for it at this moment, is it due to the cache coherency performance has been improved a lot already?

May be ... but at least from the result above, it looks like false sharing is "not that worst" while comparing with no threads being used ...

In order to have more understanding of whether the number of threads would have significant on the processing performance if false sharing occurs, ****BenchmarkFalseSharingImpactV3**** was prepared and helps us to have further understanding of the behaviour.

The statistic given out by BenchmarkFalseSharingImpactV3, it shows that if there are more threads and false sharing occurs, the negative impact to the overall performance will become more significant.  

### Testing Environment

Processor: Intel(R) Core(TM) i5-3330 CPU @ 3.00GHz 3.00 GHz [Ivy Bridge]

OS: 64-bit Windows 10

JDK: 11.0.5


### Reference 

https://parallelcomputing2017.wordpress.com/2017/03/17/understanding-false-sharing/

https://github.com/MJjainam/falseSharing/blob/master/parallelComputing.c

https://en.wikipedia.org/wiki/Branch_predictor
