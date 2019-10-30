package weekly.practice.d191019;

import java.util.Arrays;

public class HeapRank {

    public static void main(String[] args) {
        int[] a = {10, 6, 7, 5, 8, 9, 7, 3, 2};
        rank(a);
        System.out.println(Arrays.toString(a));
    }

    public static void rank(int[] a) {
        if (null == a || a.length <= 1) {
            return;
        }
        int len = a.length;
        // 构造大顶堆，堆有序
        /**
         * 为什么是从 len/2 开始构造子堆？
         */
        for (int k = len / 2; k >= 1; k--) {
            sink(a, k, len);
        }
        // 下沉排序，数组有序
        while (len > 1) {
            exch(a, 1, len--);
            sink(a, 1, len);
        }
    }

    private static void sink(int[] a, int k, int len) {
        while (2 * k <= len) {
            int j = 2 * k;
            if (j + 1 < len && less(a, j, j + 1)) {
                j++;
            }
            if (!less(a, k, j)) {
                break;
            }
            exch(a, k, j);
            k = j;
        }
    }

    private static void swim(int[] a, int k) {
        while (k > 1 && a[k / 2] < a[k]) {
            exch(a, k / 2, k);
            k = k / 2;
        }
    }

    private static boolean less(int[] a, int i, int j) {
        return a[--i] < a[--j];
    }

    private static void exch(int[] a, int i, int j) {
        int temp = a[--i];
        a[i] = a[--j];
        a[j] = temp;
    }

}
