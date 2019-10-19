package weekly.practice.d191019;

/**
 *
 */
public class GetIndexOf {

    public static void main(String[] args) {
        String s = "aaaaaaaaaaa";
        String m = "aaaf";
        System.out.println(getIndexOf(s, m));
    }

    public static int getIndexOf(String s, String m) {
        if (null == s || null == m || s.length() < m.length()) {
            return -1;
        }
        char[] sArray = s.toCharArray();
        char[] mArray = m.toCharArray();
        int start = 0, end = 0;
        while (end - start < mArray.length && end < sArray.length) {
            if (sArray[end] == mArray[end - start]) {
                end++;
                continue;
            }
            while (++start < sArray.length && sArray[start] != mArray[0]) {
            }
            end = start;
        }
        if (end - start == mArray.length) {
            return start;
        }
        return -1;
    }

}
