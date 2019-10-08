package weekly.practice.d191008;

import java.util.ArrayList;
import java.util.List;

public class LongestSubArray {

    public static void main(String[] args) {
        int[] array = {5, 6, 7, 1, 2, 8, 3, 4, 5, 6, 1, 7};
        Integer[] longestSubArray = longestSubArray(array);
        for (Integer integer : longestSubArray) {
            System.out.print(integer + " ");
        }
    }

    public static Integer[] longestSubArray(int[] array) {
        if (null == array || 0 == array.length) {
            return null;
        }
        List<Integer> subList = new ArrayList<>();
        List<Integer> newSubIndex = new ArrayList<>();
        newSubIndex.add(0);
        for (int k = 1; k < array.length; k++) {
            if (array[k] != array[k - 1] + 1) {
                newSubIndex.add(k);
            }
        }
        List<Integer> tempSubList = new ArrayList<>();
        for (Integer iter : newSubIndex) {
            tempSubList.clear();
            int pre = array[iter];
            tempSubList.add(array[iter]);
            for (int k = iter + 1; k < array.length; k++) {
                if (array[k] < pre + 1) {
                    continue;
                }
                if (array[k] > pre + 1) {
                    break;
                }
                pre = array[k];
                tempSubList.add(array[k]);
            }
            if (subList.size() < tempSubList.size()) {
                subList.clear();
                subList.addAll(tempSubList);
            }
        }
        return subList.toArray(new Integer[subList.size()]);
    }

}
