package weekly.practice.w191118_24;

public class TheSumOfNum {

    public static void main(String[] args) {
        System.out.println(getTheSum("A-1B--12C---3-A2"));
    }

    public static int getTheSum(String str) {
        if (null == str || 0 == str.length()) {
            return 0;
        }
        int res = 0, num = 0, cur = 0;
        boolean posi = true;
        char[] strChars = str.toCharArray();
        for (int i = 0; i < strChars.length; i++) {
            cur = strChars[i] - '0';
            if (0 <= cur && cur <= 9) {
                num = num * 10 + (posi ? cur : -cur);
            } else {
                if (0 != num) {
                    res += num;
                    num = 0;
                }
                if ('-' == strChars[i]) {
                    if (0 <= i - 1 && '-' == strChars[i - 1]) {
                        posi = !posi;
                    } else {
                        posi = false;
                    }
                } else {
                    posi = true;
                }
            }
        }
        res += num;
        return res;
    }

}
