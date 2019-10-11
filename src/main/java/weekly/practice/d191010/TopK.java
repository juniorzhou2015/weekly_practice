package weekly.practice.d191010;

import java.util.Arrays;

public class TopK {

    public static void main(String[] args) {
        int[] array = {5, 6, 7, 8, 1, 2, 8, 3, 4, 5, 6, 1, 7};
        int[] topK = partitionMethod(array, 5);
        System.out.println(Arrays.toString(topK));
//        for (int num : topK) {
//            System.out.print(num + " ");
//        }
    }

    public static int[] partitionMethod(int[] nums, int k) {
        if (null == nums || 0 == nums.length || k > nums.length) {
            return null;
        }
//        rank(nums, 0, nums.length -1, k);
        int lo = 0, hi = nums.length - 1;
        int par = partition(nums, lo, hi);
        while (par != k - 1) {
            if (par > k - 1) {
                hi = par - 1;
                par = partition(nums, lo, hi);
            } else {
                lo = par + 1;
                par = partition(nums, lo, hi);
            }
        }
        int[] retNums = new int[k];
        for (int i = 0; i < k; i++) {
            retNums[i] = nums[i];
        }
        return retNums;
    }

//    private static void rank(int[] a, int lo, int hi, int k) {
//        if (lo > hi) {
//            return;
//        }
//        int par = partition(a, lo, hi);
//        if (par >= k) {
//            rank(a, lo, par - 1, k); //求前半部分第k大
//        } else {
//            rank(a, lo + 1, hi, k - par);
//        }
//    }

    private static int partition(int[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        int v = a[lo];
        while (true) {
            while (v < a[++i]) {
                if (i == hi) {
                    break;
                }
            }
            while (a[--j] < v) {
                // 可去掉
                if (j == lo) {
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

    private static void exch(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

}
