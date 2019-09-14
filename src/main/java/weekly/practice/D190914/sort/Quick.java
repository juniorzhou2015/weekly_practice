package weekly.practice.D190914.sort;

import weekly.practice.D190914.StdRandom;

import java.util.Random;

/**
 * @ClassName: Quick
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 16:42
 **/
public class Quick extends Sort {

    private static Random random;

    static {
        random = new Random();
    }

    @Override
    public void sort(Comparable[] a) {
        if (null == a || 0 == a.length) {
            return;
        }
        shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int par = partition(a, lo, hi);
        sort(a, lo, par - 1);
        sort(a, par + 1, hi);
    }

    private int partition(Comparable[] a, int lo, int hi) {
        int i = lo + 1, j = hi;
        Comparable v = a[lo];
        while (true) {
            while (less(a[i++], v)) {
                if (i >= hi) {
                    break;
                }
            }
            while (less(v, a[j--])) {
                // 可去掉
                if (j <= lo) {
                    break;
                }
            }
            if (j <= i) {
                break;
            }
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    private void shuffle(Comparable[] a) {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            int r = i + random.nextInt(len - i);
            exch(a, i, r);
        }
    }

}
