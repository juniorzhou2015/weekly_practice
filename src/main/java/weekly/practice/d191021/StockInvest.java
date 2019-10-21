package weekly.practice.d191021;

/**
 * 给定一个数组代表股票每天的价格，请问只能买卖一次的情况下，最大化利润是多少？日期不重叠的情况下，可以买卖多次呢？
 * 输入: {100, 80, 120, 130, 70, 60, 100, 125} 只能买一次：65（60 买进，125 卖出） 可以买卖多次: 115（80买进，130卖出；60 买进，125卖出）
 * 提示：不用输出买卖的序列，只需要得到最大利润
 */
public class StockInvest {

    public static void main(String[] args) {
        int[] a = {100, 80, 120, 130, 70, 60, 100, 125};
        System.out.println(maxProfitOneDay(a));
    }

    /**
     * 穷举算法的时间复杂度是O(n^2)
     */

    /**
     * 时间复杂度小于O(n^2)？
     */
    public static int maxProfitOneDay(int[] a) {
        if (null == a || a.length <= 1) {
            return -1;
        }
        int len = a.length;
        int maxProfit = 0, minBuy = a[0];
        for (int i = 0; i < len; i++) {
            if (minBuy < a[i]) {
                continue;
            }
            int maxSell = 0, iterMaxProfit = 0;
            for (int j = i + 1; j < len; j++) {
                if (a[i] < a[j] && maxSell < a[j]) {
                    iterMaxProfit = a[j] - a[i];
                    maxSell = a[j];
                }
            }
            if (maxProfit < iterMaxProfit) {
                maxProfit = iterMaxProfit;
            }
        }
        return maxProfit;
    }

}
