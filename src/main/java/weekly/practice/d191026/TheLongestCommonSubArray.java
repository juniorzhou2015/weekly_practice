package weekly.practice.d191026;

/**
 * 求最长公共子串
 */
public class TheLongestCommonSubArray {


    public static void main(String[] args) {
        System.out.println(getLongestCommonSubArray("flasfjlkasjfklsd", "sfjklasjfkladsjfkl"));
    }

    public static int getLongestCommonSubArray(String a, String b) {
        if (null == a || null == b || 0 == a.length() || 0 == b.length()) {
            return 0;
        }
        char[] aArray = a.toCharArray();
        char[] bArray = b.toCharArray();
        int[][] matrix = new int[a.length()][b.length()];
        int length = 0;

        for (int j = 0; j < bArray.length; j++) {
            matrix[0][j] = aArray[0] == bArray[j] ? 1 : 0;
            if (length < matrix[0][j]) {
                length = matrix[0][j];
            }
        }

        for (int i = 0; i < aArray.length; i++) {
            matrix[i][0] = aArray[i] == bArray[0] ? 1 : 0;
            if (length < matrix[i][0]) {
                length = matrix[i][0];
            }
        }

        for (int i = 1; i < aArray.length; i++) {
            for (int j = 1; j < bArray.length; j++) {
                if (aArray[i] == bArray[j]) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    if (length < matrix[i][j]) {
                        length = matrix[i][j];
                    }
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
        return length;
    }

}
