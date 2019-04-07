package weekly.practice.D190407.function.math;

import java.util.function.DoubleFunction;

/**
 * @ClassName: TrapezoidArea
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 11:13
 **/
public class TrapezoidArea {

    public static void main(String[] args) {
        System.out.println(integrate((double x) -> x + 10, 3, 7));
    }

    public static double integrate(DoubleFunction<Double> f, double a, double b) {
        return (f.apply(a) + f.apply(b)) * (b - a) / 2;
    }

}
