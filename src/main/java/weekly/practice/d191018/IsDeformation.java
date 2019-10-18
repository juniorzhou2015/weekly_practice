package weekly.practice.d191018;

import java.util.HashMap;
import java.util.Map;

/**
 * 判断两个字符串是否已互为变形词
 */
public class IsDeformation {

    public static void main(String[] args) {
        String str1 = "123";
        String str2 = "321";
        System.out.println(isDeformation1(str1, str2));
        System.out.println(isDeformation2(str1, str2));
    }

    /**
     * 字符编码值在0~255之间
     */
    public static boolean isDeformation1(String str1, String str2) {
        if (null == str1 || 0 == str1.length() || null == str2 || 0 == str2.length() || str1.length() != str2.length()) {
            return false;
        }
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int[] count = new int[256];
        for (char char1 : chars1) {
            count[char1]++;
        }
        for (char char2 : chars2) {
            if (0 == count[char2]--) {
                return false;
            }
        }
        return true;
    }
    /**
     * 字符编码值不确定时
     */
    public static boolean isDeformation2(String str1, String str2) {
        if (null == str1 || 0 == str1.length() || null == str2 || 0 == str2.length() || str1.length() != str2.length()) {
            return false;
        }
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        Map<String, Integer> count = new HashMap<>();
        for (char char1 : chars1) {
            String str = String.valueOf(char1);
            count.compute(str, (k, v) -> null == v ? 1 : ++v);
        }
        for (char char2 : chars2) {
            String str = String.valueOf(char2);
            Integer c = count.get(str);
            if (null == c || 0 == c) {
                return false;
            }
        }
        return true;
    }

}
