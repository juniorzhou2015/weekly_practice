package weekly.practice.d191008;

import java.util.ArrayList;
import java.util.List;

public class LongestConsecutive {

    public static void main(String[] args) {
        int[] array = {5, 6, 7, 8, 1, 2, 8, 3, 4, 5, 6, 1, 7};
        int[] longestSubArray = longestConsecutive(array);
        for (int num : longestSubArray) {
            System.out.print(num + " ");
        }
    }

    public static int[] longestConsecutive(int[] nums) {
        if (null == nums || 0 == nums.length) {
            return null;
        }
        List<Integer> newSubIndex = new ArrayList<>();
        newSubIndex.add(0);
        for (int k = 1; k < nums.length; k++) {
            if (nums[k] != nums[k - 1] + 1) {
                newSubIndex.add(k);
            }
        }
        int[] longestConsecutive = new int[nums.length];
        int[] tempArray = new int[nums.length];
        for (Integer cur : newSubIndex) {
            clearArray(tempArray);
            int index = 0;
            int pre = nums[cur];
            tempArray[index++] = nums[cur];
            for (int k = cur + 1; k < nums.length; k++) {
                if (nums[k] != pre + 1) {
                    break;
                }
                pre = nums[k];
                tempArray[index++] = nums[k];
            }
            if (getArraySize(longestConsecutive) < getArraySize(tempArray)) {
                clearArray(longestConsecutive);
                int len = getArraySize(tempArray);
                for (int k = 0; k < len; k++) {
                    longestConsecutive[k] = tempArray[k];
                }
            }
        }
        return getValidArray(longestConsecutive);
    }

    private static int getArraySize(int[] nums) {
        int len = 0;
        for (int num : nums) {
            if (0 == num) {
                break;
            }
            len++;
        }
        return len;
    }

    private static void clearArray(int[] nums) {
        for (int k = 0; k < nums.length; k++) {
            nums[k] = 0;
        }
    }

    private static int[] getValidArray(int[] nums) {
        int len = getArraySize(nums);
        int[] validArray = new int[len];
        for (int k = 0; k < len; k++) {
            validArray[k] = nums[k];
        }
        return validArray;
    }

//    public static void main(String[] args) {
//        int[] longestConsecutive = new int[3];
//        longestConsecutive[0] = 1;
//        for (int k : longestConsecutive) {
//            System.out.print(k + " ");
//        }
//        System.out.println();
//        clearArray(longestConsecutive);
//        for (int k : longestConsecutive) {
//            System.out.print(k + " ");
//        }
//    }

}
