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
     * @param arr
     * @param index
     * @param solution
     * @param target
     * @return
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

    public static int coins1(int[] arr, int n) {
        return process1(arr, 0, n);
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

}
