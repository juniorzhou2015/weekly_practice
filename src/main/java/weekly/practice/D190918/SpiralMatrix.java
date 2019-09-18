package weekly.practice.D190918;

/**
 * @ClassName: SpiralMatrix
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-18 10:00
 **/
public class SpiralMatrix {

    public static int[][] createMatrix(int n) {
        int[][] matrix = new int[n][n];
        int up = 0, down = 1, left = 2, right = 3;
        int direction = right;
        int total = n * n;
        int i = 0, j = 0;
        for (int k = 1; k <= total; k++) {
            matrix[i][j] = k;
            if (right == direction) {
                if (j + 1 < n && 0 == matrix[i][j + 1]) {
                    j++;
                } else {
                    direction = down;
                    i++;
                }
            } else if (down == direction) {
                if (i + 1 < n && 0 == matrix[i + 1][j]) {
                    i++;
                } else {
                    direction = left;
                    j--;
                }
            } else if (left == direction) {
                if (j - 1 >= 0 && 0 == matrix[i][j - 1]) {
                    j--;
                } else {
                    direction = up;
                    i--;
                }
            } else if (up == direction) {
                if (i - 1 >= 0 && 0 == matrix[i - 1][j]) {
                    i--;
                } else {
                    direction = right;
                    j++;
                }
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        int n = 4;
        int[][] matrix = createMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

}
