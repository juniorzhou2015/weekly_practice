package weekly.practice.D190914.sort;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

/**
 * @ClassName: Quick
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 16:42
 **/
public class Quick extends Sort {

    public static void main(String[] args) {
        Integer[] array = {5, 6, 7, 8, 1, 2, 8, 3, 4, 5, 6, 1, 7};
        Quick quick = new Quick();
        quick.sort(array);
        System.out.println(Arrays.toString(array));
//        for (Integer num : array) {
//            System.out.print(num + " ");
//        }
    }

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
        sortIterate(a, 0, a.length - 1);
//        sortRecursion(a, 0, a.length - 1);
    }

    private void sortRecursion(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int par = partition(a, lo, hi);
        sortRecursion(a, lo, par - 1);
        sortRecursion(a, par + 1, hi);
    }

    private void sortIterate(Comparable[] a, int lo, int hi) {
        Stack<Integer> stack = new Stack<>();
        stack.push(hi);
        stack.push(lo);
        while (!stack.isEmpty()) {
            lo = stack.pop();
            hi = stack.pop();
            int par = partition(a, lo, hi);
            if (lo < par - 1) {
                stack.push(par - 1);
                stack.push(lo);
            }
            if (hi > par + 1) {
                stack.push(hi);
                stack.push(par + 1);
            }
        }
    }

    private int partition(Comparable[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        Comparable v = a[lo];
        while (true) {
            while (less(a[++i], v)) {
                if (i >= hi) {
                    break;
                }
            }
            while (less(v, a[--j])) {
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
