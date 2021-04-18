package benchmark.double_valueOf_vs_parseDouble;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

public class BenchmarkDoubleValueOfVsParseDouble {

    public static final String TEST_DOUBLE = String.valueOf(987654321.123456789d);

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchmark_Double_ValueOf() {
        double d = Double.valueOf(TEST_DOUBLE);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchmark_Double_parseDouble() {
        double d = Double.parseDouble(TEST_DOUBLE);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

}
