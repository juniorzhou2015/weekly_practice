package weekly.practice.D190914.sort;

/**
 * @ClassName: Sort
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 15:20
 **/
public abstract class Sort {

    public abstract void sort(Comparable[] a);

    public static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    public static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

}
