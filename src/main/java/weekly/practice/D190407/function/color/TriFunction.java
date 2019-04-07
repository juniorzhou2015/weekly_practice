package weekly.practice.D190407.function.color;

/**
 * @InterfaceName: TriFunction
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 11:27
 **/
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    R apply(T t, U u, V v);

}
