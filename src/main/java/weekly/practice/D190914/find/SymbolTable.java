package weekly.practice.D190914.find;

/**
 * @ClassName: SymbolTable
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 17:23
 **/
public class SymbolTable {

    public static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    public static boolean more(Comparable a, Comparable b) {
        return a.compareTo(b) > 0;
    }

    public static boolean equal(Comparable a, Comparable b) {
        return a.compareTo(b) == 0;
    }

}
