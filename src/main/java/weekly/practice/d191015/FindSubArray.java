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
        String[] m = {"a", "b", "c", "d"};
        String n = "tbcacbdata";
        System.out.println(findSubArray(m, n));
    }

    public static int findSubArray(String[] m, String n) {
        if (null == m || 0 == m.length || null == n) {
            return -1;
        }
        Map<String, Boolean> map = new HashMap<>();
        for (int i = 0; i < m.length; i++) {
            map.put(String.valueOf(m[i]), false);
        }
        int start = 0, end = 0;
        while (end - start + 1 <= m.length && end < n.length()) {
            String cur = String.valueOf(n.charAt(end));
            if (map.containsKey(cur) && !map.get(cur)) {
                end++;
                map.put(cur, true);
                continue;
            }
            end = ++start;
            for (int i = 0; i < m.length; i++) {
                map.put(String.valueOf(m[i]), false);
            }
        }
        if (end - start == m.length) {
            return start;
        }
        return -1;
    }

}
