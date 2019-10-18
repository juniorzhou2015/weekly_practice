package weekly.practice.d191015;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定m个不重复的字符 [a, b, c, d]，以及一个长度为n的字符串tbcacbdata，
 * 问能否在这个字符串中找到一个长度为m的连续子串，使得这个子串刚好由上面m个字符组成，顺序无所谓，
 * 返回任意满足条件的一个子串的起始位置，未找到返回-1。比如上面这个例子，acbd，3。
 */
public class FindSubArray {

    public static void main(String[] args) {
        String[] mStr = {"a", "b", "c", "d"};
        char[] m = {'a', 'b', 'c', 'd'};
        String n = "tbcacbdata";
        System.out.println(findSubArray1(mStr, n));
        System.out.println(findSubArray2(m, n));
    }

    /**
     * 字符编码值不确定时，用哈希表
     */
    public static int findSubArray1(String[] m, String n) {
        if (null == m || 0 == m.length || null == n) {
            return -1;
        }
        Map<String, Integer> map = new HashMap<>();
        for (String value : m) {
            map.put(value, 1);
        }
        char[] nArray = n.toCharArray();
        int start = 0, end = 0;
        while (end - start < m.length && end < nArray.length) {
            String cur2 = String.valueOf(nArray[end]);
            if (null != map.get(cur2) && 1 == map.get(cur2)) {
                end++;
                map.compute(cur2, (k, v) -> ++v);
                continue;
            }
            if (null == map.get(cur2)) {
                // 未出现过的字符
                for (String s : m) {
                    if (1 < map.get(s)) {
                        map.put(s, 1);
                    }
                }
                start = ++end;
            } else {
                // 已出现过的字符，即重复出现
                String cur1 = String.valueOf(nArray[start]);
                if (1 < map.get(cur1)) {
                    map.put(cur1, 1);
                }
                start++;
            }
        }
        if (end - start == m.length) {
            return start;
        }
        return -1;
    }
    /**
     * 字符编码值在0~255之间，用数组
     */
    public static int findSubArray2(char[] m, String n) {
        if (null == m || 0 == m.length || null == n) {
            return -1;
        }
        int[] array = new int[256];
        for (char c : m) {
            array[c] = 1;
        }
        int start = 0, end = 0;
        char[] chars = n.toCharArray();
        while (end - start + 1 <= m.length && end < n.length()) {
            char cur = chars[end];
            if (1 == array[cur]) {
                end++;
                array[cur]++;
                continue;
            }
            if (0 == array[cur]) {
                // 未出现过的字符
                for (char c : m) {
                    if (1 < array[c]) {
                        array[c] = 1;
                    }
                }
                start = ++end;
            } else {
                // 已出现过的字符，即重复出现
                if (1 < array[chars[start]]) {
                    array[chars[start]] = 1;
                }
                start++;
            }
        }
        if (end - start == m.length) {
            return start;
        }
        return -1;
    }

}
