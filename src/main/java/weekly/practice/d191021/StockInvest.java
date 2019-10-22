package weekly.practice.d191021;

/**
 * 给定一个数组代表股票每天的价格，请问只能买卖一次的情况下，最大化利润是多少？日期不重叠的情况下，可以买卖多次呢？
 * 输入: {100, 80, 120, 130, 70, 60, 100, 125} 只能买一次：65（60 买进，125 卖出） 可以买卖多次: 115（80买进，130卖出；60 买进，125卖出）
 * 提示：不用输出买卖的序列，只需要得到最大利润
 */
public class StockInvest {

    public static void main(String[] args) {
        int[] stock = {100, 80, 120, 130, 70, 60, 100, 125};
        System.out.println(maxProfitOneDay(stock));
        System.out.println(maxProfitOneDayPerfect(stock));
        System.out.println(maxProfitAll(stock));
    }

    /**
     * 穷举算法的时间复杂度是O(M^2)
     */

    /**
     * 时间复杂度小于O(N^2)？
     */
    public static int maxProfitOneDay(int[] stock) {
        if (null == stock || stock.length <= 1) {
            return -1;
        }
        int len = stock.length;
        int maxProfit = 0, minBuy = stock[0];
        for (int i = 0; i < len; i++) {
            if (minBuy < stock[i]) {
                continue;
            }
            int maxSell = 0, iterMaxProfit = 0;
            for (int j = i + 1; j < len; j++) {
                if (stock[i] < stock[j] && maxSell < stock[j]) {
                    iterMaxProfit = stock[j] - stock[i];
                    maxSell = stock[j];
                }
            }
            if (maxProfit < iterMaxProfit) {
                maxProfit = iterMaxProfit;
            }
        }
        return maxProfit;
    }

    /**
     * 时间复杂度O(N)
     */
    public static int maxProfitOneDayPerfect(int[] stock) {
        if (null == stock || stock.length <= 1) {
            return 0;
        }
        int min = stock[0];
        int maxDiff = stock[1] - min;
        for (int i = 2; i < stock.length; i++) {
            int pre = stock[i - 1];
            if (pre < min) {
                min = pre;
            }
            if (stock[i] - min > maxDiff) {
                maxDiff = stock[i] - min;
            }
        }
        return maxDiff;
    }

    /**
     * 时间复杂度怎么计算？
     */
    public static int maxProfitAll(int[] stock) {
        int maxProfit = 0;
        for (int i = 0, j = 0; i < stock.length; i = j) {
            int start = stock[i], maxIter = 0;
            for (j = i + 1; j < stock.length; j++) {
                if (start > stock[j]) {
                    if (i + 1 < j) {
                        maxProfit += maxIter - start;
                    }
                    break;
                }
                if (maxIter < stock[j]) {
                    maxIter = stock[j];
                }
                if (j + 1 == stock.length) {
                    maxProfit += maxIter - start;
                }
            }
        }
        return maxProfit;
    }

}
