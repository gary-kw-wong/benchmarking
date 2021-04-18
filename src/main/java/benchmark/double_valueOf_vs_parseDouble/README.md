### Background

In latest Java version, autoboxing and auto-unboxing is in place.

In addition, there is optimization in Java for specific function calls (i.e. do_intrinsic) if we take a look of below links:
http://hg.openjdk.java.net/jdk8/jdk8/hotspot/file/87ee5ee27509/src/share/vm/classfile/vmSymbols.hpp#1581
http://hg.openjdk.java.net/jdk9/jdk9/hotspot/file/tip/src/share/vm/classfile/vmSymbols.hpp#1677

Does it mean the following code snippets has no difference at all?  

    // Approach 1: 
    `double d = Double.parseDouble(s);`
    
VS
    
    // Approach 2: 
    `double d = Double.valueOf(s);`

As I had ever seen `double d = Double.valueOf(s);` used in a critical path of an application a lot, I wonder about this usage.

So, let's take a look of this benchmark cases.

**Remark:**

Intrinsics are high optimized (mostly hand written assembler) code which are used instead of normal JIT compiled code. For example, caching some frequently 

### Result

```
Benchmark                                                                                       Mode  Cnt    Score    Error  Units
double_valueOf_vs_parseDouble.BenchmarkDoubleValueOfVsParseDouble.benchmark_Double_ValueOf      avgt    5  203.587 ± 10.908  ns/op
double_valueOf_vs_parseDouble.BenchmarkDoubleValueOfVsParseDouble.benchmark_Double_parseDouble  avgt    5  197.385 ± 10.663  ns/op
```

### Conclusion

From the above result, we can see that the difference between them are really subtle. 

However, if we take a look of the implementation for both methods in the Double class: 

    // Approach 1
    public static double parseDouble(String s) throws NumberFormatException {
        return FloatingDecimal.parseDouble(s);
    }

    // Approach 2
    public static Double valueOf(String s) throws NumberFormatException {
        return new Double(parseDouble(s));
    }

    @HotSpotIntrinsicCandidate
    public double doubleValue() {
        return value;
    }

Along with the above implementation, I would consider below 2 points:
- Double.valueOf(String) is actually providing the returned result of parseDouble(String) into a new Double instance. Then `doubleValue()` is called in the duration of auto-unboxing. Although `doubleValue()` is marked as _@HotSpotIntrinsicCandidate_ for optimization, but compare the number of steps with calling `parseDouble(String)` directly, it shows a bit too many.   
- `Double.parseDouble()` is still performing a bit better than `Double.valueOf()`

So, if we need a primitive double as result, we should merely call Double.parseDouble(String) in order to play-safe and achieve the best performance.

### Testing Environment

Processor: Intel(R) Core(TM) i5-3330 CPU @ 3.00GHz 3.00 GHz

OS: 64-bit Windows 10

JDK: 11.0.5
