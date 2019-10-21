package weekly.practice.d191021;

import java.util.Arrays;

/**
 * 堆排序
 */
public class HeapRankPractice {

    public static void main(String[] args) {
        int[] a = {10, 9, 8, 5, 7, 6, 4, 3, 4, 2, 1};
        rank(a);
        System.out.println(Arrays.toString(a));
    }

    public static void rank(int[] a) {
        if (null == a || a.length <= 1) {
            return;
        }
        int len = a.length;
        for (int k = len / 2; k >= 1; k--) {
            sink(a, k, len);
        }
        while (len >= 1) {
            exch(a, 1, len--);
            sink(a, 1, len);
        }
    }

    private static void sink(int[] a, int k, int len) {
        while (2 * k <= len) {
            int j = 2 * k;
            if (j + 1 <= len && less(a, j, j + 1)) {
                j++;
            }
            if (less(a, j, k)) {
                break;
            }
            exch(a, k, j);
            k = j;
        }
    }

    private static void exch(int[] a, int i, int j) {
        int temp = a[--i];
        a[i] = a[--j];
        a[j] = temp;
    }

    private static boolean less(int[] a, int i, int j) {
        return a[--i] < a[--j];
    }

}
