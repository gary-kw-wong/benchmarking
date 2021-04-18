### Background
In some situation, we have unavoidably needs to round up a double value as requested by client's interface system. In order to keep the precision during calculation in Java, the first thing comes to our mind for Java would be BigDecimal and this is what I see in an existing application.

However, BigDecimal would unavoidably introduce 2 object instance in the process. In addition, we are just doing rounding without further calculation, is there any other better choices?

How about NumberFormat? Would it gives out a better performance? (I know ... it's not thread-safe, then how about use it along with ThreadLocal?) 

Let's take a look of above benchmarking and below result.

### Result
After running the benchmark, the statistics are as below. 

```
Benchmark                                                                           Mode  Cnt    Score    Error  Units
BenchmarkBigDecimalVsNumberFormat.benchBigDecimal_1_WHOLE_NUMBER                    avgt    5  585.797 ± 13.666  ns/op
BenchmarkBigDecimalVsNumberFormat.benchBigDecimal_3_WHOLE_NUMBER                    avgt    5  563.156 ± 15.116  ns/op
BenchmarkBigDecimalVsNumberFormat.benchBigDecimal_5_WHOLE_NUMBER                    avgt    5  586.000 ± 17.934  ns/op
BenchmarkBigDecimalVsNumberFormat.benchBigDecimal_7_WHOLE_NUMBER                    avgt    5  520.839 ± 26.652  ns/op
BenchmarkBigDecimalVsNumberFormat.benchBigDecimal_9_WHOLE_NUMBER                    avgt    5  520.119 ± 19.897  ns/op
BenchmarkBigDecimalVsNumberFormat.benchBigDecimal_1_WHOLE_NUMBER_19_FRACTIONAL      avgt    5  589.783 ± 31.204  ns/op

BenchmarkBigDecimalVsNumberFormat.benchNumberFormatTL_1_WHOLE_NUMBER                avgt    5  388.442 ± 14.566  ns/op
BenchmarkBigDecimalVsNumberFormat.benchNumberFormatTL_3_WHOLE_NUMBER                avgt    5  457.614 ± 22.640  ns/op
BenchmarkBigDecimalVsNumberFormat.benchNumberFormatTL_5_WHOLE_NUMBER                avgt    5  512.535 ± 13.697  ns/op
BenchmarkBigDecimalVsNumberFormat.benchNumberFormatTL_7_WHOLE_NUMBER                avgt    5  554.012 ± 34.322  ns/op
BenchmarkBigDecimalVsNumberFormat.benchNumberFormatTL_9_WHOLE_NUMBER                avgt    5  547.886 ± 24.457  ns/op
BenchmarkBigDecimalVsNumberFormat.benchNumberFormatTL_1_WHOLE_NUMBER_19_FRACTIONAL  avgt    5  519.489 ± 28.684  ns/op
```

### Conclusion

The result is shedding us some light significantly: NumberFormat is outperforming BigDecimal.

While using BigDecimal, the duration is quite steady with input value of different length of fractional part, which is around 520 ns to 589 ns. 

While using NumberFormat even along with TreadLocal, the processing time would increase from 388 ns to at most 554 ns corresponding to the increment in whole number part. But it is still not worst then using BigDecimal. 

Of course, in real application, the result may be affected by many other external factors, but it still gives us a hints.

### Testing Environment

Processor: Intel(R) Core(TM) i5-3330 CPU @ 3.00GHz 3.00 GHz

OS: 64-bit Windows 10

JDK: 11.0.5
