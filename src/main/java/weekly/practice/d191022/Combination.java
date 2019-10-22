package weekly.practice.d191022;

/**
 * 有1，2，5，10等不同零钱各若干，问如果要组合成N元，有多少种不同的组合方式？假设有m种零钱，具体面值存在arr中，要找的钱为n。
 * 例如：arr=[1, 2, 5, 10], n=5, 则有4种组合方式,分别为：
 * 1,1,1,1,1
 * 1,1,1,2
 * 1,2,2
 * 5
 * 提示：不需要给出具体的组合，只需要找出组合的数量
 */
public class Combination {

    public static void main(String[] args) {
        int[] arr = {1, 2, 5, 10};
        System.out.println(combination(arr, 11));
        System.out.println(combinationRecursion(arr, 11));
    }

    /**
     * 时间复杂度怎么计算？
     */
    public static int combination(int[] arr, int n) {
        int count = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n / 2; j++) {
                for (int l = 0; l <= n / 5; l++) {
                    for (int m = 0; m <= n / 10; m++) {
                        if (n == i + 2 * j + 5 * l + 10 * m) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public static int combinationRecursion(int[] arr, int n) {
        if (n <= 0) {
            return 0;
        }
        if (1 == n) {
            return 1;
        }
        if (2 == n) {
            return 2;
        }
        if (5 == n) {
            return 4;
        }
        if (10 == n) {
            return 11;
        }
        return combinationRecursion(arr, n-1)
                + combinationRecursion(arr, n-2)
                + combinationRecursion(arr, n-5)
                + combinationRecursion(arr, n-10);
    }

}
