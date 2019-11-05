package weekly.practice.d191022;

import java.util.Stack;

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
        int[] arr = {1, 2, 5, 7, 10};
        System.out.println(combination(arr, 15));
//        System.out.println(combinationRecursion1(arr, 50));
        System.out.println(combinationRecursion2(arr, 15));
        System.out.println(coins1(arr, 15));
        System.out.println(coins2(arr, 15));
        System.out.println(coins3(arr, 15));
        System.out.println(coins4(arr, 15));
    }

    /**
     * 时间复杂度怎么计算？
     */
    public static int combination(int[] arr, int n) {
        int count = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n / 2; j++) {
                for (int l = 0; l <= n / 5; l++) {
                    for (int p = 0; p <= n / 7; p++) {
                        for (int m = 0; m <= n / 10; m++) {
                            if (n == i + 2 * j + 5 * l + 7 * p + 10 * m) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    static int total1 = 0;
    static int countQ1 = 0;

    /**
     * 如何使用递归求解？
     */
    public static int combinationRecursion1(int[] arr, int n) {
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
        if (7 == n) {
            return 7;
        }
        if (10 == n) {
            return 11;
        }
        return combinationRecursion1(arr, n - 1)
                + combinationRecursion1(arr, n - 2)
                + combinationRecursion1(arr, n - 5)
                + combinationRecursion1(arr, n - 7)
                + combinationRecursion1(arr, n - 10);
    }

    /**
     * 回溯：尝试所有可能性，递归的一种
     * 时间复杂度是多少？
     * 《面试指南》中"换钱的方法数"有详细解答
     */
    public static int combinationRecursion2(int[] arr, int n) {
        Stack<Integer> solution = new Stack<>();
        return process(arr, 0, solution, n);
    }

    static int total = 0;
    static int countQ = 0;

    private static void dfs(int[] arr, int index, Stack<Integer> solution, Integer target) {
        if (total == target) {
            countQ++;
            System.out.println(solution);
            return;
        }
        if (total > target) {
            return;
        }
        for (int i = index; i < arr.length; i++) {
            total += arr[i];
            solution.add(arr[i]);
            dfs(arr, i, solution, target);
            solution.pop();
            total -= arr[i];
        }
    }

    /**
     * 时间复杂度和arr中钱的面值有关，最差情况下为O(aim^m)，m为arr长度
     */
    private static int process(int[] arr, int index, Stack<Integer> solution, int target) {
        if (0 == target) {
            System.out.println(solution);
            return 1;
        }
        if (0 > target) {
            return 0;
        }
        int res = 0;
        for (int i = index; i < arr.length; i++) {
            solution.add(arr[i]);
            res += process(arr, i, solution, target - arr[i]);
            solution.pop();
        }
        return res;
    }

    public static int coins1(int[] arr, int aim) {
        return process1(arr, 0, aim);
    }

    private static int process1(int[] arr, int index, int aim) {
        if (arr.length == index) {
            return 0 == aim ? 1 : 0;
        }
        int res = 0;
        for (int i = 0; arr[index] * i <= aim; i++) {
            res += process1(arr, index + 1, aim - arr[index] * i);
        }
        return res;
    }

    /**
     * O(N*aim^2)
     */
    public static int coins2(int[] arr, int aim) {
        int[][] map = new int[arr.length + 1][aim + 1];
        return process2(arr, 0, aim, map);
    }

    private static int process2(int[] arr, int index, int aim, int[][] map) {
        int res = 0;
        if (arr.length == index) {
            res = 0 == aim ? 1 : 0;
            map[index][aim] = 0 == res ? -1 : res;
            return res;
        }
        for (int i = 0; arr[index] * i <= aim; i++) {
            int indexNext = index + 1;
            int aimNext = aim - arr[index] * i;
            int resNext = map[indexNext][aimNext];
            res += 0 < resNext ? resNext :
                    -1 == resNext ? 0 : process2(arr, indexNext, aimNext, map);
        }
        map[index][aim] = 0 == res ? -1 : res;
        return res;
    }

    /**
     * O(N*aim^2)
     */
    public static int coins3(int[] arr, int aim) {
        int[][] dp = new int[arr.length][aim + 1];
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; arr[0] * j <= aim; j++) {
            dp[0][arr[0] * j] = 1;
        }
        int num = 0;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= aim; j++) {
                num = 0;
                for (int k = 0; k * arr[i] <= j; k++) {
                    num += dp[i - 1][j - k * arr[i]];
                }
                dp[i][j] = num;
            }
        }
        return dp[arr.length - 1][aim];
    }

    /**
     * O(N*aim)
     */
    public static int coins4(int[] arr, int aim) {
        int[][] dp = new int[arr.length][aim + 1];
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; arr[0] * j <= aim; j++) {
            dp[0][arr[0] * j] = 1;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= aim; j++) {
                /**
                 * 为什么上面两行运行没问题，下面那行运行就有问题？
                 */
//                dp[i][j] = dp[i - 1][j];
//                dp[i][j] += j - arr[i] >= 0 ? dp[i][j - arr[i]] : 0;
                dp[i][j] = dp[i - 1][j] + j - arr[i] >= 0 ? dp[i][j - arr[i]] : 0;
            }
        }
        return dp[arr.length - 1][aim];
    }
    /**
     * O(N*aim)
     * 空间压缩O(aim)
     */

}
