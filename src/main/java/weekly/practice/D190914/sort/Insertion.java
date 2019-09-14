package weekly.practice.D190914.sort;

/**
 * @ClassName: Insertion
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 15:32
 **/
public class Insertion extends Sort {

    @Override
    public void sort(Comparable[] a) {
        if (null == a || 0 == a.length) {
            return;
        }
        int len = a.length;
        for (int i = 0; i < len; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                exch(a, j, j - 1);
            }
        }
    }
}
