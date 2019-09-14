package weekly.practice.D190914;

/**
 * @ClassName: MoveArrayElement
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-15 00:09
 **/
public class MoveArrayElement {

    /**
     * 经典方法，三次倒置数组中对应位置的元素;
     * 简单说一下原理：数组元素右移k个位置的结果是，原来在
     * 后面的k个元素跑到了数组前面，原来在前面的length-k
     * 个元素，跑到了数组的后面，并且前后两部分元素各自的顺序和
     * 移动前一致，而倒置整个数组元素就是让后面k个元素跑到前面去，
     * 让前面length-k个元素跑到后面去，但是倒置之后前后两部分
     * 元素的顺序跟移动之前不一样了，倒置了，所以要把两部分的元素
     * 倒置回来
     * @param array
     * @param k
     */
    public static void moveArrayElement3(int[] array, int k) {
        // 倒置所有元素
        reverse(array);
        // 倒置前k个元素
        reverse(array, 0, k - 1);
        // 倒置后length - k个元素
        reverse(array, k, array.length - 1);
    }

    /**
     * 倒置数组中begin和end之间的元素，包括begin和end
     * @param array
     * @param begin
     * @param end
     */
    private static void reverse(int[] array, int begin, int end) {
        int length = end - begin + 1;
        int half = length / 2;
        for(int i = 0; i < half; i++) {
            int temp = array[begin];
            array[begin] = array[end];
            array[end] = temp;
            begin++;
            end--;
        }

    }

    /**
     * 倒置数组中begin和end之间的元素，包括begin和end
     * @param array
     */
    private static void reverse(int[] array) {
        reverse(array, 0, array.length - 1);
    }
}
