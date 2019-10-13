package weekly.practice.d191013;

public class SlidingWindow {

    /**
     * 某一个大文件被拆成了N个小文件，每个小文件编号从0至N-1，相应大小分别记为S(i)。给定磁盘空间为C
     * 试实现一个函数从N个文件中连续选出若干个文件拷贝到磁盘中，使得磁盘剩余空间最小。
     * 函数定义如下：
     * int MaximumCopy(std::vector<size_t> s, size_t C, size_t &start_index, size_t &end_index);
     * 函数返回值为剩余空间，如无解返回-1。
     * 其中start_index, end_index为文件的编号。
     * 如N=5，S = {1, 2, 3, 5, 4}，C = 7
     * 结果为 ：
     * start_index = 0,
     * end_index = 2,
     * return = 1
     */
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 5, 4};
        maximumCopy1(array, 7);
    }

    /**
     * 时间复杂度O(N^2)
     */
    public static void maximumCopy1(int[] s, int C) {
        if (null == s || 0 == s.length) {
            return;
        }
        int left = C, startIndex = 0, endIndex = 0;
        for (int i = 0; i < s.length; i++) {
            int j = i, tempLeft = C;
            for (; j < s.length; j++) {
                int value = s[j];
                if (value > tempLeft) {
                    j--;
                    break;
                }
                tempLeft -= value;
            }
            if (tempLeft < left) {
                startIndex = i;
                endIndex = j;
                left = tempLeft;
            }
        }
        System.out.println("start_index: " + startIndex);
        System.out.println("end_index: " + endIndex);
        System.out.println("left: " + left);
    }

    /**
     * 滑动窗口
     * 时间复杂度O(N)
     */
    public static void maximumCopy2(int[] s, int C) {
        if (null == s || s.length == 0) {
            return;
        }
        int left = C, startIndex = 0, endIndex = 0, i = 0, j = i + 1;
//        while (startIndex < s.length) {
//            int tempLeft = C;
//            int value = s[j];
//            if (value > tempLeft) {
//                j++;
//                break;
//            }
//            tempLeft -= value;
//            if (tempLeft < left) {
//                startIndex = i;
//                endIndex = j;
//                left = tempLeft;
//            }
//            j++;
//        }
        System.out.println("start_index: " + startIndex);
        System.out.println("end_index: " + endIndex);
        System.out.println("left: " + left);
    }


}
