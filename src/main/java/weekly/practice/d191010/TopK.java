package weekly.practice.d191010;

import java.util.Arrays;
import java.util.BitSet;
import java.util.PriorityQueue;

public class TopK {

    public static void main(String[] args) {
        int[] array1 = {20, 5, 6, 7, 8, 1, 2, 8, 3, 4, 5, 1, 8, 7, 6};
        int[] topK = partitionMethod(array1, 6);
        System.out.println(Arrays.toString(topK));
        int[] array2 = {20, 5, 6, 7, 8, 1, 2, 8, 3, 4, 5, 1, 8, 7, 6};
        PriorityQueue<Integer> topKSet = heapMethod(array2, 6);
        System.out.println(topKSet);
        int[] array3 = {20, 5, 6, 7, 8, 1, 2, 8, 3, 4, 5, 1, 8, 7, 6};
        topK = bitMapMethod(array3, 6);
        System.out.println(Arrays.toString(topK));
//        for (int num : topK) {
//            System.out.print(num + " ");
//        }
    }

    /**
     * 1、有重复元素时不能用TreeSet，所以要用PriorityQueue
     * TODO
     * 2、自己用数组实现小顶堆
     */
    public static PriorityQueue<Integer> heapMethod(int[] nums, int k) {
        if (null == nums || 0 == nums.length || k > nums.length) {
            return null;
        }
        /**
         * 最小TopK时需传入lambda表达式
         * (o1, o2) -> o2 - o1
         */
        PriorityQueue<Integer> queue = new PriorityQueue<>(k);
        for (int i = 0; i < k; i++) {
            queue.offer(nums[i]);
        }
        for (int i = k; i < nums.length; i++) {
            int value = nums[i];
            if (queue.peek() < value) {
                queue.poll();
                queue.offer(value);
            }
        }
        return queue;
    }

    /**
     * 1、时间复杂度为O(N)，最坏时间复杂度为O(N^2)，每次shuffle一下也可以的
     * TODO
     * 2、用BFPRT算法，最坏时间复杂度优化为O(N)
     */
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

    public static int[] bitMapMethod(int[] nums, int k) {
        if (null == nums || 0 == nums.length || k > nums.length) {
            return null;
        }
        BitSet bitSet = new BitSet();
        //将数组内容组bitmap
        for (int i = 0; i < nums.length; i++) {
            bitSet.set(nums[i]);
        }
        int[] ret = new int[k];
        for (int i = bitSet.length() - 1, j = k - 1; 0 <= i && 0 <= j; i--) {
            if (bitSet.get(i)) {
                ret[j--] = i;
            }
        }
        return ret;
    }


//    void set_bit(char[] bit_map, int n) {
//        bit_map[]
//        for (int i = 0; i < (n / 8); ++i) {
//            bit_map++;
//        }
//        //相应表示的位置1
//    *bit_map = ( * bit_map) |(0x01 << n % 8);
//    }
//
//    int get_max_value(int num[], int n) {
//        int temp = num[0];
//        for (int i = 1; i < n; ++i) {
//            if (num[i] > temp) {
//                temp = num[i];
//            }
//        }
//        return temp;
//    }
//
//    void bit_map_sort(int num[], int n) {
//        //首先得到数组中的最大值
//        int max = get_max_value(num, n);
//        //计算bitmap的长度,每一个字节可以表示8个数
//        int bit_map_len = 0;
//        if (max % 8 == 0)
//            bit_map_len = max / 8;
//        else
//            bit_map_len = max / 8 + 1;
//        //创建bitmap
//        char[] bit_map = new char[bit_map_len];
//        memset(bit_map, 0, bit_map_len);
//        for (int i = 0; i < n; ++i)
//            set_bit(bit_map, num[i]);
//        //输出数组
//        for (int i = 0; i < bit_map_len; ++i) {
//            for (int j = 0; j < 8; ++j) {
//                //如果该位是1，则输出
//                if ((bit_map[i] & (0x01 << j)) == (0x01 << j))
//                    cout << (i * 8 + j) << " ";
//            }
//        }
//        cout << endl;
//    }

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
