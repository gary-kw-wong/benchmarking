package benchmark.false_sharing;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

public class BenchmarkFalseSharingImpactV3 {

    /**
     * From wiki of https://en.wikipedia.org/wiki/False_sharing, there is a description like this:
     * "memory is cached in lines of some small power of two word size (e.g., 64 aligned, contiguous bytes)."
     * while from https://www.infoq.com/presentations/JVM-Performance-Tuning-twitter/
     * we know that 1 Machine Word falls between the range of 64 bytes - 128 bytes.
     * <p>
     * which means that, in actual, 1 Cache Line is basically the same concept of 1 Machine Word. (i.e. cache line = machine word)
     */
    private static int[] bucket = new int[1000];
    private static int[] adder = new int[10];
    private static final int TIMES = 100_000_000;

    private static final int firstElement = 0;
    private static final int badAdjacentElement1 = 1;
    private static final int badAdjacentElement2 = 2;
    private static final int badAdjacentElement3 = 3;
    private static final int goodNextElement1 = 299;
    private static final int goodNextElement2 = 499;
    private static final int goodNextElement3 = 699;

    public BenchmarkFalseSharingImpactV3() {
        //bucket = new int[100];
        for (int i = 0; i < adder.length; i++) {
            adder[i] = i + 1;
        }
    }

    public void expensiveProcessing(int index) {
        for (int i = 0; i < TIMES; i++) {
            bucket[index] += adder[i % 10];
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void testSerialProcessing() {
        expensiveProcessing(firstElement);
        expensiveProcessing(badAdjacentElement1);
        expensiveProcessing(badAdjacentElement2);
        expensiveProcessing(badAdjacentElement3);
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void testParallelProcessingWithFalseSharing() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            expensiveProcessing(firstElement);
        }, "firstElemTh");
        Thread t2 = new Thread(() -> {
            expensiveProcessing(badAdjacentElement1);
        }, "badAdjacentElemTh1");
        Thread t3 = new Thread(() -> {
            expensiveProcessing(badAdjacentElement2);
        }, "badAdjacentElemTh2");
        Thread t4 = new Thread(() -> {
            expensiveProcessing(badAdjacentElement3);
        }, "badAdjacentElemTh3");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join(); // let the main thread to wait until 't1' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t2.join(); // let the main thread to wait until 't2' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t3.join(); // let the main thread to wait until 't1' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t4.join(); // let the main thread to wait until 't2' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void testParallelProcessingWithoutFalseSharing() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            expensiveProcessing(firstElement);
        }, "firstElemTh");
        Thread t2 = new Thread(() -> {
            expensiveProcessing(goodNextElement1);
        }, "goodNextElement1");
        Thread t3 = new Thread(() -> {
            expensiveProcessing(goodNextElement2);
        }, "goodNextElement2");
        Thread t4 = new Thread(() -> {
            expensiveProcessing(goodNextElement3);
        }, "goodNextElement3");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join(); // let the main thread to wait until 't1' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t2.join(); // let the main thread to wait until 't2' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t3.join(); // let the main thread to wait until 't1' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t4.join(); // let the main thread to wait until 't2' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.

    }
}
