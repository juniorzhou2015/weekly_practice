package weekly.practice.d191023;

/**
 * 36进制由0-9，a-z，共36个字符表示，最小为'0'
 * '0'~'9'对应十进制的0~9，'a'~'z'对应十进制的10~35
 * 例如：'1b' 换算成10进制等于 1 * 36^1 + 11 * 36^0 = 36 + 11 = 47
 * 要求按照加法规则计算出任意两个36进制正整数的和
 * 如：按照加法规则，计算'1b' + '2x' = '48'
 * 要求：不允许把36进制数字整体转为10进制数字，计算出10进制数字的相加结果再转回为36进制
 */
public class The36Addition {

    public static void main(String[] args) {
        System.out.println(add("1b", "zx"));
    }

    public static String chars = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String add(String a, String b) {
        int i = a.length() - 1, j = b.length() -1, flag = 0;
        StringBuilder sb = new StringBuilder();
        for (; i >= 0 && j >= 0; i--, j--) {
//            int aNum = get10Num(a.charAt(i));
//            int bNum = get10Num(b.charAt(j));
            int num = chars.indexOf (a.charAt(i)) + chars.indexOf(b.charAt(j)) + flag;
            if (num >= 36) {
                flag = 1;
                num -= 36;
            } else {
                flag = 0;
            }
            sb.append(chars.charAt(num));
        }
        for (; i >= 0; i--) {
            int num = chars.indexOf(a.charAt(i)) + flag;
            if (num >= 36) {
                flag = 1;
                num -= 36;
            } else {
                flag = 0;
            }
            sb.append(chars.charAt(num));
        }
        for (; j >= 0; j--) {
            int num = chars.indexOf(b.charAt(j)) + flag;
            if (num >= 36) {
                flag = 1;
                num -= 36;
            } else {
                flag = 0;
            }
            sb.append(chars.charAt(num));
        }
        if (1 == flag) {
            sb.append(chars.charAt(1));
        }
        return sb.reverse().toString();
    }

    private static int get10Num(char chr) {
        int num = 0;
        if (chr >= '0') {
            num = chr - '0';
        }
        if (chr >= 'a') {
            num = chr - 'a' + 10;
        }
        return num;
    }

}
