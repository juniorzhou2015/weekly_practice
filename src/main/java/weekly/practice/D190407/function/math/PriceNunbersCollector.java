package weekly.practice.D190407.function.math;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static weekly.practice.D190407.function.math.PrimeNumbers.isPrime;

/**
 * @ClassName: PriceNunbersCollector
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 12:16
 **/
public class PriceNunbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return () -> new HashMap<Boolean, List<Integer>>() {{
            put(Boolean.TRUE, Lists.newArrayList());
            put(Boolean.FALSE, Lists.newArrayList());
        }};
    }

    /**
     * 收集过程访问部分结果
     * @return
     */
    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean, List<Integer>> acc, Integer candidate) -> acc.get(isPrime(acc.get(Boolean.TRUE), candidate)).add(candidate);
    }

    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
            map1.get(Boolean.TRUE).addAll(map2.get(Boolean.TRUE));
            map1.get(Boolean.FALSE).addAll(map2.get(Boolean.FALSE));
            return map1;
        };
    }

    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
