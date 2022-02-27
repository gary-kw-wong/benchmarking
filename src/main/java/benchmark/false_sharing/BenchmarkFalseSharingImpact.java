package benchmark.false_sharing;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

public class BenchmarkFalseSharingImpact {

    /**
     * From wiki of https://en.wikipedia.org/wiki/False_sharing, there is a description like this:
     * "memory is cached in lines of some small power of two word size (e.g., 64 aligned, contiguous bytes)."
     * while from https://www.infoq.com/presentations/JVM-Performance-Tuning-twitter/
     * we know that 1 Machine Word falls between the range of 64 bytes - 128 bytes.
     * <p>
     * which means that, in actual, 1 Cache Line is basically the same concept of 1 Machine Word. (i.e. cache line = machine word)
     */
    private static int[] array = new int[100];
    private static final int TIMES = 100_000_000;

    private static final int firstElement = 0;
    private static final int badAdjacentElement = 1;
    private static final int lastElement = 99;

    public BenchmarkFalseSharingImpact() {
        array = new int[100];
    }

    public void expensiveProcessing(int index) {
        for (int i = 0; i < TIMES; i++) {
            array[index] += 1;
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void testSerialProcessing() {
        expensiveProcessing(firstElement);
        expensiveProcessing(badAdjacentElement);
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
            expensiveProcessing(badAdjacentElement);
        }, "badAdjacentElemTh");

        t1.start();
        t2.start();
        t1.join(); // let the main thread to wait until 't1' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t2.join(); // let the main thread to wait until 't2' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
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
            expensiveProcessing(lastElement);
        }, "badAdjacentElemTh");

        t1.start();
        t2.start();
        t1.join(); // let the main thread to wait until 't1' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.
        t2.join(); // let the main thread to wait until 't2' is finished before termination; otherwise,the main thread may end at any time unexpectedly before child treads are finished.

    }
}
