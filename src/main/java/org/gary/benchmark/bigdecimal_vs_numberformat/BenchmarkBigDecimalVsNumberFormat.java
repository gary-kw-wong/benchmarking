package org.gary.benchmark.bigdecimal_vs_numberformat;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BenchmarkBigDecimalVsNumberFormat {

    private static final int TOTAL_RUN_TIMES = 1_000_000;

    private static final int roundingDecimalDigits = 7;

    //private final NumberFormat fmt;

    private static final ThreadLocal<NumberFormat> TL_FMT = ThreadLocal.withInitial(() -> {
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);
        fmt.setGroupingUsed(false);
        fmt.setMaximumFractionDigits(roundingDecimalDigits);
        fmt.setMinimumFractionDigits(roundingDecimalDigits);
        fmt.setRoundingMode(RoundingMode.HALF_EVEN);
        return fmt;
    });

    /*public BenchmarkBigDecimalVsNumberFormat(){
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);
        fmt.setGroupingUsed(false);
        fmt.setMaximumFractionDigits(roundingDecimalDigits);
        fmt.setMinimumFractionDigits(roundingDecimalDigits);
        fmt.setRoundingMode(RoundingMode.HALF_EVEN);

        this.fmt = fmt;
    }*/

    public static final double AVG_PX_9_WHOLE_NUMBER = 987654321.123456789d;
    public static final double AVG_PX_7_WHOLE_NUMBER = 7654321.123456789;
    public static final double AVG_PX_5_WHOLE_NUMBER = 54321.123456789d;
    public static final double AVG_PX_3_WHOLE_NUMBER = 321.123456789d;
    public static final double AVG_PX_1_WHOLE_NUMBER = 1.123456789d;
    public static final double AVG_PX_1_WHOLE_NUMBER_19_FRACTIONAL = 1.1234567890123456789d;
    public static final double AVG_PX_1_MAX_DOUBLE = Double.MAX_VALUE;

    //=========================================================
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5) // default is 10 sec each
    @Measurement(iterations = 5) // default is 10 sec each
    @BenchmarkMode(Mode.AverageTime)
    public void benchBigDecimal_9_WHOLE_NUMBER() {
        BigDecimal bd = new BigDecimal(AVG_PX_9_WHOLE_NUMBER);
        BigDecimal roundBd = bd.setScale(roundingDecimalDigits, RoundingMode.HALF_EVEN);
        String result = roundBd.toPlainString(); // toPlainString() avoid exponent field while decimal places >= 7
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchNumberFormatTL_9_WHOLE_NUMBER() {
        String result = TL_FMT.get().format(AVG_PX_7_WHOLE_NUMBER);
    }

    //=========================================================
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchBigDecimal_7_WHOLE_NUMBER() {
        BigDecimal bd = new BigDecimal(AVG_PX_9_WHOLE_NUMBER);
        BigDecimal roundBd = bd.setScale(roundingDecimalDigits, RoundingMode.HALF_EVEN);
        String result = roundBd.toPlainString(); // toPlainString() avoid exponent field while decimal places >= 7
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchNumberFormatTL_7_WHOLE_NUMBER() {
        String result = TL_FMT.get().format(AVG_PX_7_WHOLE_NUMBER);
    }

    //=========================================================
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchBigDecimal_5_WHOLE_NUMBER() {
        BigDecimal bd = new BigDecimal(AVG_PX_5_WHOLE_NUMBER);
        BigDecimal roundBd = bd.setScale(roundingDecimalDigits, RoundingMode.HALF_EVEN);
        String result = roundBd.toPlainString(); // toPlainString() avoid exponent field while decimal places >= 7
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchNumberFormatTL_5_WHOLE_NUMBER() {
        String result = TL_FMT.get().format(AVG_PX_5_WHOLE_NUMBER);
    }

    //=========================================================
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchBigDecimal_3_WHOLE_NUMBER() {
        BigDecimal bd = new BigDecimal(AVG_PX_3_WHOLE_NUMBER);
        BigDecimal roundBd = bd.setScale(roundingDecimalDigits, RoundingMode.HALF_EVEN);
        String result = roundBd.toPlainString(); // toPlainString() avoid exponent field while decimal places >= 7
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchNumberFormatTL_3_WHOLE_NUMBER() {
        String result = TL_FMT.get().format(AVG_PX_3_WHOLE_NUMBER);
    }

    //=========================================================
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchBigDecimal_1_WHOLE_NUMBER() {
        BigDecimal bd = new BigDecimal(AVG_PX_1_WHOLE_NUMBER);
        BigDecimal roundBd = bd.setScale(roundingDecimalDigits, RoundingMode.HALF_EVEN);
        String result = roundBd.toPlainString(); // toPlainString() avoid exponent field while decimal places >= 7
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchNumberFormatTL_1_WHOLE_NUMBER() {
        String result = TL_FMT.get().format(AVG_PX_1_WHOLE_NUMBER);
    }

    //=========================================================
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchBigDecimal_1_WHOLE_NUMBER_19_FRACTIONAL() {
        BigDecimal bd = new BigDecimal(AVG_PX_1_WHOLE_NUMBER_19_FRACTIONAL);
        BigDecimal roundBd = bd.setScale(roundingDecimalDigits, RoundingMode.HALF_EVEN);
        String result = roundBd.toPlainString(); // toPlainString() avoid exponent field while decimal places >= 7
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.AverageTime)
    public void benchNumberFormatTL_1_WHOLE_NUMBER_19_FRACTIONAL() {
        String result = TL_FMT.get().format(AVG_PX_1_WHOLE_NUMBER_19_FRACTIONAL);
    }

    //=========================================================
//    @Fork(value = 1000, warmups = 1000)
//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    public void benchBigDecimal_MAX_DOUBLE() {
//        BigDecimal bd = new BigDecimal(AVG_PX_1_MAX_DOUBLE);
//        BigDecimal roundBd = bd.setScale(roundingDecimalDigits, RoundingMode.HALF_EVEN);
//        String result = roundBd.toPlainString(); // toPlainString() avoid exponent field while decimal places >= 7
//    }
//
//    @Fork(value = 1000, warmups = 1000)
//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    public void benchNumberFormatTL_MAX_DOUBLE() {
//        String result = TL_FMT.get().format(AVG_PX_1_MAX_DOUBLE);
//    }

    //=========================================================

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

}
