package weekly.practice.D190407.function;

import weekly.practice.D190407.function.forkJoin.ForkJoinSumCalculator;

import java.util.function.Function;

/**
 * @ClassName: ParallelStreamsHarness
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 22:59
 **/
public class ParallelStreamsHarness {

    public static void main(String[] args) {
        System.out.println("ForkJoin sum done in " + measurePerf(ForkJoinSumCalculator::forkJoinSum, 1_000_000L) + " msecs");
    }

    public static <T, R> long measurePerf(Function<T, R> f, T input) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            R result = f.apply(input);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("result: " + result);
            if (duration < fastest) {
                fastest = duration;
            }
        }
        return fastest;
    }

}
